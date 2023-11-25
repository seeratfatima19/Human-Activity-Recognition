package com.example.har;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textViewA, textViewG,textViewM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind views
        textViewA = (TextView) findViewById(R.id.acceleroView);
        textViewG = (TextView) findViewById(R.id.gyroView);
        textViewM = (TextView) findViewById(R.id.magnetoView);


        // sensor code ahead
        SensorManager sensormgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //taking data from all sensors

        if (sensormgr != null) {
            Accelerometer accelerometer = new Accelerometer(sensormgr, textViewA);
            Gyroscope  gyroscope = new Gyroscope(sensormgr, textViewG);
        } else {
            System.out.println("Sensor Manager is null");
            Toast.makeText(this, "Sensor mgr is null", Toast.LENGTH_SHORT).show();
        }
    }

}