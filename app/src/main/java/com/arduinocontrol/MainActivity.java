package com.arduinocontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MainActivity extends Activity {

    private  String TAG = "* MAIN *";

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
    private ArrayAdapter<String> deviceNames;

    private final BroadcastReceiver btBroadcastsReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (devices.indexOf(foundDevice) == -1) {
                    devices.add(foundDevice);
                    listDevices();
                }
            }

            if (BluetoothAdapter.ACTION_REQUEST_ENABLE.equals(action)) {
                Log.d(TAG, "Request Enable BT Accepted");
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.d(TAG, "Discovery Started");
                Crouton.makeText(MainActivity.this, R.string.bt_discovery_started, Style.INFO).show();
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Log.d(TAG, "Discovery Finished");
                Crouton.makeText(MainActivity.this, R.string.bt_discovery_complete, Style.CONFIRM).show();
            }
        }
    };

    AdapterView.OnItemClickListener onDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bluetoothAdapter.cancelDiscovery();
            BluetoothDevice selectedDevice = devices.get(position);

            Intent intent = new Intent(MainActivity.this, ConnectedActivity.class);
            intent.putExtra("MAC_ADDRESS", selectedDevice.getAddress());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Components
        ListView deviceListView = (ListView) findViewById(R.id.device_list);
        deviceNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceNames);
        deviceListView.setOnItemClickListener(onDeviceClick);


        //Request to switch on BT on start up
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        }

        ///////////////////
        IntentFilter btIntentFilter = new IntentFilter();
        btIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        ///////////////////

        registerReceiver(btBroadcastsReceiver, btIntentFilter);


        // Start Discovering after user allows access to bt
        if ( bluetoothAdapter != null) {
            listDevices();
            bluetoothAdapter.startDiscovery();
        } else {
            //TODO : Check this without bt support
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if ( bluetoothAdapter != null) {
                listDevices();
                bluetoothAdapter.startDiscovery();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(btBroadcastsReceiver);
    }

    private void listDevices() {
        deviceNames.clear();
        for (BluetoothDevice device : devices) {
            if (device.getName() != null) {
                deviceNames.add(device.getName());
            }
        }
    }
}