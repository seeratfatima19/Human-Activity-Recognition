package com.example.har;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

public class Accelerometer implements SensorEventListener
{
    TextView textView;
    Accelerometer(SensorManager mgr, TextView textView)
    {
        this.textView = textView;
        Sensor accelerometer = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // check is sensor is working or not
        if(accelerometer!=null)
        {
            mgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {

            Toast.makeText(textView.getContext(), "Accelerometer not found",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER)
        {
            textView.setText("Accelerometer vals:\n X: "+event.values[0]+"\nY: "+event.values[1]+"\nZ: "+event.values[2]);
        }
        else {
            System.out.println("Sensor type is not accelerometer");
        }

    }
}
