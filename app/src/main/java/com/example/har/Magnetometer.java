package com.example.har;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;

public class Magnetometer {
    TextView textView;
    Sensor magnetometer;
    Magnetometer(SensorManager mgr, TextView textView)
    {
        this.textView = textView;
        this.magnetometer = mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
}
