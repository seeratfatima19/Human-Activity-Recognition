package sensor;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.hardware.Sensor;
import android.widget.Toast;

import com.example.har.DataCollection;
import com.example.har.MainActivity;

public class Gyroscope implements SensorEventListener {

    TextView textView;
    MainActivity mainActivity;
    float x,y,z, valueX, valueZ, valueY;
    int i= 0;
    Sensor gyroscope;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private final float[] deltaRotationVector = new float[4];
    Gyroscope(SensorManager mgr, TextView textView)
    {
        this.textView = textView;
        this.gyroscope = mgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //this.mainActivity = mainActivity;
        if(gyroscope!=null)
        {
            mgr.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {

            Toast.makeText(textView.getContext(), "Gyroscope not found",Toast.LENGTH_SHORT).show();
            System.out.println("Gyroscope not found");
        }
    }

    float getValueX(){
        return valueX;
    }

    float getValueY(){
        return valueY;
    }

    float getValueZ(){
        return valueZ;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE)
        {
            if(timestamp!=0)
            {
                final float dT = (event.timestamp - timestamp) * NS2S;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                float omegaMagnitude = (float) Math.sqrt(x*x + y*y + z*z);

                if(omegaMagnitude>0.1)
                {
                    x /= omegaMagnitude;
                    y /= omegaMagnitude;
                    y /= omegaMagnitude;
                }

                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);

                deltaRotationVector[0] = sinThetaOverTwo * x;
                deltaRotationVector[1] = sinThetaOverTwo * y;
                deltaRotationVector[2] = sinThetaOverTwo * z;
                deltaRotationVector[3] = cosThetaOverTwo;

            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            float[] remappedRotationMatrix = new float[9];
            SensorManager.remapCoordinateSystem(deltaRotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotationMatrix);
            float[] orientations = new float[3];
            SensorManager.getOrientation(remappedRotationMatrix, orientations);
            for(int i=0;i<3;i++)
            {
                orientations[i] = (float) (Math.toDegrees(orientations[i]));
            }
            valueX = orientations[0];
            valueY = orientations[1];
            valueZ = orientations[2];
            textView.setText("Gyroscope vals:\n X: "+orientations[0]+"\nY: "+orientations[1]+"\nZ: "+orientations[2]);

        }
        else {
            System.out.println("Sensor type is not gyroscope");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
