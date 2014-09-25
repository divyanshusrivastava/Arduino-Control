package com.arduinocontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends Activity {

    private  String TAG = "* MAIN *";

    List<BluetoothDevice> pairedDevices;
    List<BluetoothDevice> availableDevices;

    ArrayAdapter<String> deviceNames;
    ListView deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
            UI Components
        */
        deviceList = R.id.device_list;


        IntentFilter btIntentFilter = new IntentFilter();
        btIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
