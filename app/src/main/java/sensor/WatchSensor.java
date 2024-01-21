package sensor;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.*;
import android.util.Log;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

public class WatchSensor extends BroadcastReceiver {

    private static final String TAG = "BluetoothDiscovery";

    // Reference to the BluetoothAdapter for further actions
    private BluetoothAdapter bluetoothAdapter;

    // Constructor to pass the BluetoothAdapter
    public WatchSensor(BluetoothAdapter adapter) {
        this.bluetoothAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // A Bluetooth device has been found
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            try {
                // Display device information (you may want to filter devices based on name, address, etc.)
                Log.d(TAG, "Device found: " + device.getName() + " - " + device.getAddress());
            }catch(SecurityException e) {
                //catch security exception
            }
            // Connect to the smartwatch (replace with your logic)
            connectToSmartwatch(device);
        }
    }

    // Implement your logic to connect to the smartwatch
    private void connectToSmartwatch(BluetoothDevice device) {
        // Add your logic here to establish a connection with the smartwatch
        // This could involve using a dedicated SDK or BluetoothSocket
        // For simplicity, let's assume you have a connect method in your BluetoothUtils class
         BluetoothUtils.connect(device);
    }
}


class BluetoothUtils {

    // UUID for SPP (Serial Port Profile)
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Establish a connection with the Bluetooth device
    public static BluetoothSocket connect(BluetoothDevice device) {
        BluetoothSocket socket = null;

        try {
            try {
                // Create a socket using the SPP UUID
                socket = device.createRfcommSocketToServiceRecord(SPP_UUID);


            // Connect to the device
            socket.connect();
            }
            catch(SecurityException e) {
                //catch exception
            }
        } catch (IOException e) {
            // Handle connection errors
            e.printStackTrace();
            try {
                // Attempt to close the socket in case of failure
                socket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }

        return socket;
    }
}


