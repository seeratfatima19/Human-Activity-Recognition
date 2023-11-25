package com.example.har;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

public class Magnetometer implements SensorEventListener {
    TextView textView;
    Sensor magnetometer;
    Magnetometer(SensorManager mgr, TextView textView)
    {
        this.textView = textView;
        this.magnetometer = mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetometer!=null)
        {
            mgr.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {

            Toast.makeText(textView.getContext(), "Magnetometer not found",Toast.LENGTH_SHORT).show();
            System.out.println("Magnetometer not found");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            textView.setText("Magnetometer vals:\n X: "+event.values[0]+"\nY: "+event.values[1]+"\nZ: "+event.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
