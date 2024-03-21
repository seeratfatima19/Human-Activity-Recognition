package sensor;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.*;

import com.example.har.MainActivity;
import com.example.har.Server;
public class PhoneSensor implements SensorEventListener {
    List<Sensor> sensors;
    SensorManager sensorManager;
    Button btnSensorList;
    static String str;
    static String check_str;
    DecimalFormat df = new DecimalFormat("000.00");
    List<Sensor> implementedSensors = new ArrayList<>();
    static int byteSensors[] = {0,0,0,0,0,0,0,0};
    Context context;

    List<CheckBox> checkBoxList = new ArrayList<>();
    List<Integer> selectedSensors = new ArrayList<>();

    List<Sensor> testSensors = new ArrayList<>();   //for testing purposes only
    // List of Lists (List of Sensors to keep the values for each sensor)
    ArrayList<List<Double>> listOfSensors
            = new ArrayList<List<Double>>();

    public PhoneSensor(SensorManager mgr, Button btn, Context context){
        this.sensors= mgr.getSensorList(Sensor.TYPE_ALL);
        this.sensorManager = mgr;
        this.str = "";
        this.context = context;
        this.btnSensorList = btn;
        //this.byteSensors = Collections.nCopies(8,0);
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_GRAVITY));
        this.implementedSensors.add(mgr.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));

        btnSensorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSensorsDialog();
            }
        });
    }
    private void showSensorsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Sensors");

        for(Sensor sensor: implementedSensors){

            boolean found = false;
            for(Sensor s: sensors){

                if(sensor == s ){ found = true;}
            }

            if(!found) implementedSensors.remove(sensor);
        }

        for(Sensor s1: implementedSensors){
            System.out.println("Implemented Sensors: " + s1.getName() + "\n");
        }

        System.out.println("Byte Sensor Size: " + byteSensors.length + "\n" );
        for(Integer i: byteSensors){

            System.out.println("Byte Sensor: " + i + " , " );
        }
        ScrollView scrollView = new ScrollView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);

        for (Sensor sensor : implementedSensors) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(sensor.getName());
            checkBox.setTag(sensor);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkBoxList.add((CheckBox) buttonView);
                        selectedSensors.add(sensor.getType());
                        testSensors.add(sensor);
                    } else {
                        checkBoxList.remove(buttonView);
                        selectedSensors.remove(sensor.getType());
                        testSensors.remove(sensor);
                    }
                }
            });
            linearLayout.addView(checkBox);
        }

        builder.setView(scrollView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedSensors.isEmpty()) {
                    Toast.makeText(context, "Please select at least one sensor", Toast.LENGTH_SHORT).show();
                } else {

                    for(Sensor s: testSensors){
                        System.out.println("Test Sensors: " +s + "\n");
                    }
                    startSensors(selectedSensors,sensorManager);

                }
                }

            });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void startSensors(List<Integer> selectedSensorsList, SensorManager mgr) {

    for(Integer s: selectedSensorsList){

            if(s == Sensor.TYPE_ACCELEROMETER){

                Accelerometer accelerometer = new Accelerometer(mgr);
                List<Double> accelerometerList = new ArrayList<Double>();
                accelerometerList.add((double)accelerometer.getX());
                accelerometerList.add((double)accelerometer.getY());
                accelerometerList.add((double)accelerometer.getZ());
                str += df.format(accelerometer.getX()) + "," + df.format(accelerometer.getY()) + "," + df.format(accelerometer.getZ()) + ",";
                listOfSensors.add(accelerometerList);
                System.out.println("A Str " + str + "\n");
                byteSensors[0] = 1;
                System.out.println("A Byte " + byteSensors[0] + "\n");

            }
            if(s == Sensor.TYPE_GYROSCOPE){
                Gyroscope  gyroscope = new Gyroscope(mgr);
                List<Double> gyroscopeList = new ArrayList<Double>();
                gyroscopeList.add((double)gyroscope.getValueX());
                gyroscopeList.add((double)gyroscope.getValueY());
                gyroscopeList.add((double)gyroscope.getValueZ());
                str += df.format(gyroscope.getValueX()) + ", " + df.format(gyroscope.getValueY()) + ", " + df.format(gyroscope.getValueZ()) + ", ";
                listOfSensors.add(gyroscopeList);
                System.out.println("G Str " + str + "\n");
                byteSensors[1] = 1;
                System.out.println("G Byte " + byteSensors[1] + "\n");

            }
            if(s == Sensor.TYPE_MAGNETIC_FIELD){
                Magnetometer magnet = new Magnetometer(mgr);
                List<Double> magnetList = new ArrayList<Double>();
                magnetList.add((double)magnet.getX());
                magnetList.add((double)magnet.getY());
                magnetList.add((double)magnet.getZ());
                str += df.format(magnet.getX()) + "," + df.format(magnet.getY()) + "," + df.format(magnet.getZ()) + ",";
                listOfSensors.add(magnetList);
                System.out.println("M Str " + str + "\n");
                byteSensors[2] = 1;
                System.out.println("M Byte " + byteSensors[2] + "\n");

            }
            if(s == Sensor.TYPE_PROXIMITY){
                Sensor proximity = mgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                setSensors(proximity);
            }
            if(s == Sensor.TYPE_LINEAR_ACCELERATION){
                Sensor linear_acc = mgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                setSensors(linear_acc);
            }
            if(s == Sensor.TYPE_STEP_COUNTER){
                Sensor stepCounter = mgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                setSensors(stepCounter);
            }
            if(s == Sensor.TYPE_GRAVITY){
                Sensor gravity = mgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
                setSensors(gravity);
            }
            if(s == Sensor.TYPE_ROTATION_VECTOR){
                Sensor rotation = mgr.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
                setSensors(rotation);
            }

        }

       /* for(Integer i: byteSensors){
            System.out.println("Byte Sensors: " + i + "\n");
        }*/

    }
    private void setSensors(Sensor s){
        sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        float distanceValue = 0.00F;
        String acceleration = "", gravityData = "", rotationData = "";
        float stepCount = 0.00F;
        String proximityData = "", stepCountData = "";
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            List<Double> proximityList = new ArrayList<Double>();
            distanceValue = Float.parseFloat(df.format(event.values[0]));
            proximityData= distanceValue + "," + "000.00"+ "," + "000.00"+ ",";
            proximityList.add((double) distanceValue);
            listOfSensors.add(proximityList);
            byteSensors[3] = 1;
            System.out.println("P Byte " + byteSensors[3] + "\n");
            //textViewAll.setText("Proximity Value: " + distanceValue);
        }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            List<Double> linearAccList = new ArrayList<Double>();
            float[] values = event.values;
            acceleration = "LinearAcceleration.csv,"+df.format(values[0]) + "," + df.format(values[1]) + "," + df.format(values[2]) + ",";
            linearAccList.add((double)values[0]);
            linearAccList.add((double)values[1]);
            linearAccList.add((double)values[2]);
            listOfSensors.add(linearAccList);
            byteSensors[4] = 1;
            System.out.println("LA Byte " + byteSensors[4] + "\n");
            //textViewAll.setText(accelerationData);
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            List<Double> stepCounterList = new ArrayList<Double>();
            stepCount = Float.parseFloat(df.format(event.values[0]));
             stepCountData= "StepCounter.csv,"+stepCount + ","+ "000.00"+ "," + "000.00"+ ",";
            stepCounterList.add((double) stepCount);
            listOfSensors.add(stepCounterList);
            byteSensors[5] = 1;
            System.out.println("SC Byte " + byteSensors[5] + "\n");
            //textViewAll.setText("Step Count: " + stepCount);
        }

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            List<Double> gravityList = new ArrayList<Double>();
            float[] values = event.values;
            gravityData = "Gravity.csv,"+df.format(values[0]) + "," + df.format(values[1]) + "," + df.format(values[2]) + ",";
            gravityList.add((double)values[0]);
            gravityList.add((double)values[1]);
            gravityList.add((double)values[2]);
            listOfSensors.add(gravityList);
            byteSensors[6] = 1;
            System.out.println("Gra Byte " + byteSensors[6] + "\n");
            //textViewAll.setText(gravityData);

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
            byteSensors[7] = 1;
            //System.out.println("RV Byte " + byteSensors[7] + "\n");
            rotationData = "RotationVector.csv,"+df.format(azimuthDegrees) + "," + df.format(pitchDegrees) + "," + df.format(rollDegrees)+ ",";
            //System.out.println(rotationData);
            //textViewAll.setText(rotationData);
        }
        //Server s = new Server();
        //s.write_to_server(listOfSensors);

        str= proximityData + acceleration + stepCountData + gravityData + rotationData + "$";
        check_str = str;
        str = "";
        System.out.println("Data =  " + check_str + "\n");
        Server.collectData = true;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static String getString(){

        return check_str;

    }

    public static String getByteSensors(){
        String stringActiveSensorList = String.join(",", Arrays.toString(byteSensors));
        String stringActiveSensorList1 = stringActiveSensorList.substring(1, stringActiveSensorList.length()-1);
        return stringActiveSensorList1;
    }

}
