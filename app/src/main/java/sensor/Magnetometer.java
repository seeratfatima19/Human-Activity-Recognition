package sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.har.DataCollection;
import com.example.har.MainActivity;

public class Magnetometer implements SensorEventListener {
    //TextView textView;
    MainActivity mainActivity;
    float x,y,z;
    int i = 0;
    Sensor magnetometer;
    Magnetometer(SensorManager mgr)
    {
        //this.textView = textView;
        this.magnetometer = mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetometer!=null)
        {
            mgr.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {

            //Toast.makeText(textView.getContext(), "Magnetometer not found",Toast.LENGTH_SHORT).show();
            System.out.println("Magnetometer not found");
        }
    }

    float getX(){
        return x;
    }
    float getY(){
        return y;
    }
    float getZ(){
        return z;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            //textView.setText("Magnetometer vals:\n X: "+event.values[0]+"\nY: "+event.values[1]+"\nZ: "+event.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
