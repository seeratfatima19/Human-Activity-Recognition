package sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class PhoneSensor implements SensorEventListener {
    List<Sensor> sensors;
    SensorManager sensorManager;
    TextView textViewAll;

    // List of Lists (List of Sensors to keep the values for each sensor)
    ArrayList<List<Double>> listOfSensors
            = new ArrayList<List<Double>>();

    public PhoneSensor(SensorManager mgr, TextView textView){
        this.sensors= mgr.getSensorList(Sensor.TYPE_ALL);
        this.sensorManager = mgr;
        textViewAll = textView;

        for(Sensor s: sensors){
            System.out.println(s.getName());
            if(s.getType() == Sensor.TYPE_ACCELEROMETER){

                Accelerometer accelerometer = new Accelerometer(mgr, textView);
                List<Double> accelerometerList = new ArrayList<Double>();
                accelerometerList.add((double)accelerometer.getX());
                accelerometerList.add((double)accelerometer.getY());
                accelerometerList.add((double)accelerometer.getZ());
                listOfSensors.add(accelerometerList);

            }
            if(s.getType() == Sensor.TYPE_GYROSCOPE){
                Gyroscope  gyroscope = new Gyroscope(mgr, textView);
                List<Double> gyroscopeList = new ArrayList<Double>();
                gyroscopeList.add((double)gyroscope.getValueX());
                gyroscopeList.add((double)gyroscope.getValueY());
                gyroscopeList.add((double)gyroscope.getValueZ());
                listOfSensors.add(gyroscopeList);

            }
            if(s.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                Magnetometer magnet = new Magnetometer(mgr, textView);
                List<Double> magnetList = new ArrayList<Double>();
                magnetList.add((double)magnet.getX());
                magnetList.add((double)magnet.getY());
                magnetList.add((double)magnet.getZ());
                listOfSensors.add(magnetList);

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
            List<Double> proximityList = new ArrayList<Double>();
            float distanceValue = event.values[0];
            proximityList.add((double) distanceValue);
            listOfSensors.add(proximityList);
            textViewAll.setText("Proximity Value: " + distanceValue);
        }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            List<Double> linearAccList = new ArrayList<Double>();
            float[] values = event.values;
            String accelerationData = "Linear Acceleration:\n X: " + values[0] + "\nY: " + values[1] + "\nZ: " + values[2];
            linearAccList.add((double)values[0]);
            linearAccList.add((double)values[1]);
            linearAccList.add((double)values[2]);
            listOfSensors.add(linearAccList);
            textViewAll.setText(accelerationData);
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            List<Double> stepCounterList = new ArrayList<Double>();
            float stepCount = event.values[0];
            stepCounterList.add((double) stepCount);
            listOfSensors.add(stepCounterList);
            textViewAll.setText("Step Count: " + stepCount);
        }

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            List<Double> gravityList = new ArrayList<Double>();
            float[] values = event.values;
            String gravityData = "Gravity: \n X: " + values[0] + "\nY: " + values[1] + "\nZ: " + values[2];
            gravityList.add((double)values[0]);
            gravityList.add((double)values[1]);
            gravityList.add((double)values[2]);
            listOfSensors.add(gravityList);
            textViewAll.setText(gravityData);

        }

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            List<Double> rotationVectorList = new ArrayList<Double>();
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // Get the orientation angles from the rotation matrix
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);

            // Convert the radians to degrees
            float azimuthDegrees = (float) Math.toDegrees(orientation[0]);
            float pitchDegrees = (float) Math.toDegrees(orientation[1]);
            float rollDegrees = (float) Math.toDegrees(orientation[2]);

            rotationVectorList.add((double)azimuthDegrees);
            rotationVectorList.add((double)pitchDegrees);
            rotationVectorList.add((double)rollDegrees);
            listOfSensors.add(rotationVectorList);
            String rotationData = "Rotation Vector: \nAzimuth: " + azimuthDegrees + "\nPitch: " + pitchDegrees + "\nRoll: " + rollDegrees;
            textViewAll.setText(rotationData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
