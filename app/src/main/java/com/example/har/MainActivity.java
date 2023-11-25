package com.example.har;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textViewA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewA = (TextView) findViewById(R.id.acceleroView);

        SensorManager sensormgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensormgr != null) {
            Accelerometer accelerometer = new Accelerometer(sensormgr, textViewA);
        } else {
            System.out.println("Sensor Manager is null");
            Toast.makeText(this, "Sensor mgr is null", Toast.LENGTH_SHORT).show();
        }
    }

}