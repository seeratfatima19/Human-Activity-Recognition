package sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PhoneSensor implements SensorEventListener {
    List<Sensor> sensors;
    SensorManager sensorManager;
    TextView textViewAll;
    public PhoneSensor(SensorManager mgr, TextView textView){
        this.sensors= mgr.getSensorList(Sensor.TYPE_ALL);
        this.sensorManager = mgr;
        textViewAll = textView;
       /* StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensors ) {
            System.out.println(currentSensor.getName());
            sensorText.append(currentSensor.getName()).append(
                    System.getProperty("line.separator"));
        }

        textView.append(sensorText);*/
        // checking
        for(Sensor s: sensors){
            System.out.println(s.getName());
            //textView.append(s.toString() + "\n\n");
            if(s.getType() == Sensor.TYPE_ACCELEROMETER){
                Accelerometer accelerometer = new Accelerometer(mgr, textView);
            }
            if(s.getType() == Sensor.TYPE_GYROSCOPE){
                Gyroscope  gyroscope = new Gyroscope(mgr, textView);
            }
            if(s.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                Magnetometer magnet = new Magnetometer(mgr, textView);
            }
            if(s.getType() == Sensor.TYPE_PROXIMITY){
               setSensors(s);
            }
            if(s.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                setSensors(s);
            }
            if(s.getType() == Sensor.TYPE_STEP_COUNTER){
                setSensors(s);
            }
            if(s.getType() == Sensor.TYPE_GRAVITY){
                setSensors(s);
            }
            if(s.getType() == Sensor.TYPE_ROTATION_VECTOR){
                setSensors(s);
            }

        }

    }

    private void setSensors(Sensor s){
        sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distanceValue = event.values[0];
            textViewAll.setText("Proximity Value: " + distanceValue);
        }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float[] values = event.values;
            String accelerationData = "Linear Acceleration:\n X: " + values[0] + "\nY: " + values[1] + "\nZ: " + values[2];
            textViewAll.setText(accelerationData);
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            float stepCount = event.values[0];
            textViewAll.setText("Step Count: " + stepCount);
        }

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float[] values = event.values;
            String gravityData = "Gravity: \n X: " + values[0] + "\nY: " + values[1] + "\nZ: " + values[2];
            textViewAll.setText(gravityData);

        }

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // Get the orientation angles from the rotation matrix
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);

            // Convert the radians to degrees
            float azimuthDegrees = (float) Math.toDegrees(orientation[0]);
            float pitchDegrees = (float) Math.toDegrees(orientation[1]);
            float rollDegrees = (float) Math.toDegrees(orientation[2]);

            String rotationData = "Rotation Vector: \nAzimuth: " + azimuthDegrees + "\nPitch: " + pitchDegrees + "\nRoll: " + rollDegrees;
            textViewAll.setText(rotationData);
        }
}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
