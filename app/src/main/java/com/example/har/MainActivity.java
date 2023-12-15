package com.example.har;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
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
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textViewA, textViewG,textViewM, IP;
    EditText IPtext;

    Button btnIP, btndis;
    SensorManager sensormgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind views
        textViewA = (TextView) findViewById(R.id.acceleroView);
        textViewG = (TextView) findViewById(R.id.gyroView);
        textViewM = (TextView) findViewById(R.id.magnetoView);

        //binding Server IPs views and button
        IP = findViewById(R.id.ServerIp);
        IPtext=findViewById(R.id.IP);
        btnIP = findViewById(R.id.buttonIP);
        btndis = findViewById(R.id.btndisconnect);


        // sensor code ahead
         sensormgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //taking data from all sensors

        if (sensormgr != null) {
            Accelerometer accelerometer = new Accelerometer(sensormgr, textViewA, this);
            Gyroscope  gyroscope = new Gyroscope(sensormgr, textViewG, this);
            Magnetometer magnet = new Magnetometer(sensormgr, textViewM, this);
        } else {
            System.out.println("Sensor Manager is null");
            Toast.makeText(this, "Sensor mgr is null", Toast.LENGTH_SHORT).show();
        }

        final HttpURLConnection[] urlConnection = new HttpURLConnection[1];

        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myIP = IPtext.getText().toString();
                Log.d("Your ip", myIP);
                try{
                    URL url = new URL("http://"+myIP);
                    urlConnection[0] = (HttpURLConnection) url.openConnection();

                    if(urlConnection[0] != null){
                        showToast("Server connected to: "+myIP);
                    }
                    try{
                       urlConnection[0].setRequestMethod("POST");

                       //enabling input output streams
                       urlConnection[0].setDoInput(true);
                       urlConnection[0].setDoOutput(true);

                       //getting output stream
                        OutputStream fout = new BufferedOutputStream(urlConnection[0].getOutputStream());
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fout));

                       // WriteMyData();


                    }
                    catch(IOException e){
                        //exception handling
                    }

                }catch (IOException e){
                    //handling exceptions
                }
            }


        });

        btndis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlConnection[0].disconnect();
                showToast("Server Disconnected");
            }
        });


    }

    private void showToast(String ip){
        Toast.makeText(this, ip, Toast.LENGTH_SHORT).show();
    }



}