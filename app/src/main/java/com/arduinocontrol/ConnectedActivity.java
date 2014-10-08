package com.arduinocontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import de.keyboardsurfer.android.widget.crouton.Crouton;



public class ConnectedActivity extends Activity {

    final String TAG = "* CONNECTED *";
    final  BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice server;
    String mac_address = null;
    float threshold;
    boolean streamPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connected);

        /////////////////////////////////
        Button pingUp = (Button) findViewById(R.id.pingup);
        Button pingDown = (Button) findViewById(R.id.pingdown);

        findViewById(R.id.close_connection).setEnabled(false);

        ToggleButton toggleStream = (ToggleButton) findViewById(R.id.toggle_stream);
        toggleStream.setOnCheckedChangeListener(onPauseStreamToggle);

        Button closeConnection = (Button) findViewById(R.id.close_connection);
        closeConnection.setOnClickListener(onCloseConnectionClick);

        SeekBar threshold = (SeekBar) findViewById(R.id.threshold);
        threshold.setOnSeekBarChangeListener(onThresholdChange);
        //////////////////////////////////

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mac_address = extras.getString("MAC_ADDRESS");
        }

        if (mac_address != null){
            server = bluetoothAdapter.getRemoteDevice(mac_address);
            setTitle(server.getName());
        }

        //BluetoothSocket btSocket = server.createInsecureRfcommSocketToServiceRecord();
    }

    SeekBar.OnSeekBarChangeListener onThresholdChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            threshold = (float) progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    CompoundButton.OnCheckedChangeListener onPauseStreamToggle = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            streamPaused = !isChecked;
        }
    };

    View.OnClickListener onCloseConnectionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ConnectedActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    protected void OnDestroy(){
        Crouton.cancelAllCroutons();
    }

}
