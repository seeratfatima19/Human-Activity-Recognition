package sensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

public class PhoneSensor {
    List<Sensor> sensors;
    public PhoneSensor(SensorManager mgr){
        this.sensors= mgr.getSensorList(Sensor.TYPE_ALL);

        // checking
        for(Sensor s: sensors){
            System.out.println(s.getName());
        }
    }

}
