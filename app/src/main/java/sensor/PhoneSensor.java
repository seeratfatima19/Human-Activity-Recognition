package sensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PhoneSensor {
    List<Sensor> sensors;
    public PhoneSensor(SensorManager mgr, TextView textView){
        this.sensors= mgr.getSensorList(Sensor.TYPE_ALL);

        // checking
        for(Sensor s: sensors){
            System.out.println(s.getName());
        }

        if (mgr != null) {
            Accelerometer accelerometer = new Accelerometer(mgr, textView);
            //Gyroscope  gyroscope = new Gyroscope(sensormgr, textViewG, this);
            //Magnetometer magnet = new Magnetometer(sensormgr, textViewM, this);
        } else {
            System.out.println("Sensor Manager is null");
           // Toast.makeText(this, "Sensor mgr is null", Toast.LENGTH_SHORT).show();
        }
    }

}
