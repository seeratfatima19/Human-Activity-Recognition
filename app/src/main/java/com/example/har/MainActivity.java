package com.example.har;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import sensor.PhoneSensor;
import sensor.WatchSensor;

public class MainActivity extends AppCompatActivity {

    TextView textViewA, textViewG,textViewM, IP, textConn;
    EditText IPtext, UserId;

    Button btnIP, btndis, searchButton;
    private Socket client;
    private PrintWriter printwriter;
    BluetoothAdapter mBluetoothAdapter;
    // seerat
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler = new Handler();
    private BleDeviceAdapter bleDeviceAdapter;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 20 seconds.
    private static final long SCAN_PERIOD = 20000;
    SensorManager sensormgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //checking api level
        int apiLevel = Build.VERSION.SDK_INT;
        Log.d("MyApp", "API Level: " + apiLevel);
        // bind views

        textConn = findViewById(R.id.connection);

        //binding Server IPs views and button
        IP = findViewById(R.id.ServerIp);
        IPtext=findViewById(R.id.IP);
        btnIP = findViewById(R.id.buttonIP);
        btndis = findViewById(R.id.btndisconnect);
        UserId = findViewById(R.id.UserId);

        // sensor code ahead
         sensormgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //taking data from all sensors
        ArrayList<List<Double>> sensorDataList= null;

        if (sensormgr != null) {
            //class to get data from the sensors
            PhoneSensor sensors = new PhoneSensor(sensormgr);
            //this function returns data of all sensors in a list of lists
            //sensorDataList = sensors.get_all_data();

        } else {
            System.out.println("Sensor Manager is null");
            Toast.makeText(this, "Sensor mgr is null", Toast.LENGTH_SHORT).show();
        }



        //connecting server and disconnecting on button clicks
        //creating server object and passing context to it so it can be used to create toasts
        //sensor data is also passed to write it on server

        Server srvr = new Server(this);
        srvr.connect_disconnect(btnIP, IPtext, btndis, textConn, UserId);
        //this method will take both buttons of connecting and disconnecting, so server can be connected accordingly
        //the ip of the server is passed to it as well




        // this is bluetooth code written by seerat. Anyone who touches it without her permission
        // will be cursed by the gods

        //creating bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        bleDeviceAdapter = new BleDeviceAdapter(this, devices);
        recyclerView.setAdapter(bleDeviceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //bluetooth connections
        searchButton = findViewById(R.id.search);


        //if search for device button is pressed then:
        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                searchButton.setText("Searching...");
                devices.clear();
                bleDeviceAdapter.updateDevices(devices);

                BluetoothLeScanner  bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                ScanSettings settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                bluetoothLeScanner.startScan(null, settings, new ScanCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        BluetoothDevice device = result.getDevice();
                        if (!devices.contains(device)) {
                            devices.add(device);
                            bleDeviceAdapter.notifyItemInserted(devices.size() - 1);
                        }
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        for (ScanResult result : results) {
                            BluetoothDevice device = result.getDevice();
                            if (!devices.contains(device)) {
                                devices.add(device);
                                bleDeviceAdapter.notifyItemInserted(devices.size() - 1);
                            }
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        Log.e("BLE", "Scan failed with error code: " + errorCode);
                    }
                });
/*
                if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
                {
                    // Prompt user to enable Bluetooth
                    try {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    }
                    catch (SecurityException e){
                        //catch exception
                    }

                }

*/


            }
        });

    }


}


