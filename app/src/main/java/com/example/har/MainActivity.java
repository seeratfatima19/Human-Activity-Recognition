package com.example.har;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import sensor.PhoneSensor;

public class MainActivity extends AppCompatActivity {

    TextView textViewA, textViewG,textViewM, IP, textConn;
    EditText IPtext;

    Button btnIP, btndis, search;
    private Socket client;
    private PrintWriter printwriter;



    SensorManager sensormgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind views
        //textViewA = (TextView) findViewById(R.id.acceleroView);
        //textViewG = (TextView) findViewById(R.id.gyroView);
        //textViewM = (TextView) findViewById(R.id.magnetoView);
        textConn = findViewById(R.id.connection);

        //binding Server IPs views and button
        IP = findViewById(R.id.ServerIp);
        IPtext=findViewById(R.id.IP);
        btnIP = findViewById(R.id.buttonIP);
        btndis = findViewById(R.id.btndisconnect);


        // sensor code ahead
         sensormgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //taking data from all sensors
        ArrayList<List<Double>> sensorDataList= null;

        if (sensormgr != null) {
            PhoneSensor sensors = new PhoneSensor(sensormgr);
            sensorDataList = sensors.get_all_data();

        } else {
            System.out.println("Sensor Manager is null");
            Toast.makeText(this, "Sensor mgr is null", Toast.LENGTH_SHORT).show();
        }



        //connecting server and disconnecting on button clicks
        //creating server object and passing context to it so it can be used to create toasts
        //sensor data is also passed to write it on server

        Server srvr = new Server(this);
        srvr.connect_disconnect(btnIP, IPtext, btndis, sensorDataList, textConn);
        //this method will take both buttons of connecting and disconnecting, so server can be connected accordingly
        //the ip of the server is passed to it as well




        //bluetooth connections
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("Searching...");
            }
        });

    }


}


