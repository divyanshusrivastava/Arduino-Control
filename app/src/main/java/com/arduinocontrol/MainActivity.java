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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private  String TAG = "* MAIN *";
    private final int REQUEST_ENABLE_BT = 1;

    private final  BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
    private ArrayAdapter<String> deviceNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    private ListView deviceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //

        /**
         * UI Components
        **/
        deviceListView = (ListView) findViewById(R.id.device_list);
        deviceListView.setAdapter(deviceNames);

        ///////////////////
        IntentFilter btIntentFilter = new IntentFilter();
        btIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        ///////////////////

        registerReceiver(btBroadcastsReceiver, btIntentFilter);
    }


    private final BroadcastReceiver btBroadcastsReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            }

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.d(TAG, "Discovery Started");
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Log.d(TAG, "Discovery Finished");
            }
        }
    };

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
