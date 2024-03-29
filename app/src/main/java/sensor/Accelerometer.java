package sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.har.DataCollection;
import com.example.har.MainActivity;

public class Accelerometer implements SensorEventListener
{
    //TextView textView;
    MainActivity mainActivity;
    float x,y,z;
    int i = 0;
    Sensor accelerometer;
    Accelerometer(SensorManager mgr)
    {
        //this.textView = textView;
        this.accelerometer = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //this.mainActivity = mainActivity;
        // check is sensor is working or not

        if(accelerometer!=null)
        {
            mgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {

            //Toast.makeText(textView.getContext(), "Accelerometer not found",Toast.LENGTH_SHORT).show();
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
    public float getZ(){
        return z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER)
        {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            //textView.setText("Accelerometer vals:\n X: "+event.values[0]+"\nY: "+event.values[1]+"\nZ: "+event.values[2]);

        }
        else {
            System.out.println("Sensor type is not accelerometer");
        }

    }
}
