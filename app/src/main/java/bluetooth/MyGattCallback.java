package bluetooth;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class MyGattCallback extends BluetoothGattCallback {
    private static final String TAG = "MyGattCallback";

    //  uuids for the services
    private static final UUID ACCELEROMETER_SERVICE_UUID = UUID.fromString("0000181A-0000-1000-8000-00805F9B34FB");
    private static final UUID MAGNETOMETER_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB");
    private static final UUID GYROSCOPE_SERVICE_UUID = UUID.fromString("00001820-0000-1000-8000-00805F9B34FB");

    // uuid for sensor characteristic
    private static final UUID ACCELEROMETER_DATA_CHARACTERISTIC_UUID = UUID.fromString("00002A5D-0000-1000-8000-00805F9B34FB");
    private static final UUID MAGNETOMETER_DATA_CHARACTERISTIC_UUID = UUID.fromString("00002A5E-0000-1000-8000-00805F9B34FB");
    private static final UUID GYROSCOPE_DATA_CHARACTERISTIC_UUID = UUID.fromString("00002A5F-0000-1000-8000-00805F9B34FB");
    private final Context context;
    private final BluetoothDevice device;
    // Implement the GATT server callback methods here
    public MyGattCallback(Context context, BluetoothDevice device) {
        this.context = context;
        this.device = device;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i(TAG, "Connected to GATT server.");
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.i(TAG, "Disconnected from GATT server.");
        }
        else {
            Log.e(TAG, "Connection state change failed. status: " + status + ", newState: " + newState);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                // Check if the service is a sensor service (e.g., heart rate, accelerometer)
                if (service.getUuid().equals(ACCELEROMETER_SERVICE_UUID
                )) {
                    // Get the sensor characteristic (e.g., heart rate measurement, accelerometer data)
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(ACCELEROMETER_DATA_CHARACTERISTIC_UUID);
                    if (characteristic != null) {
                        // Read the sensor data
                        gatt.readCharacteristic(characteristic);
                    }
                }
                if (service.getUuid().equals(MAGNETOMETER_SERVICE_UUID
                )) {
                    // Get the sensor characteristic (e.g., heart rate measurement, accelerometer data)
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(MAGNETOMETER_DATA_CHARACTERISTIC_UUID);
                    if (characteristic != null) {
                        // Read the sensor data
                        gatt.readCharacteristic(characteristic);
                    }
                }
                if(service.getUuid().equals(GYROSCOPE_SERVICE_UUID)){
                    // Get the sensor characteristic (e.g., heart rate measurement, accelerometer data)
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(GYROSCOPE_DATA_CHARACTERISTIC_UUID);
                    if (characteristic != null) {
                        // Read the sensor data
                        gatt.readCharacteristic(characteristic);
                    }
                }
            }
        }
    }

    ///
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // Get the sensor data from the characteristic
            byte[] data = characteristic.getValue();
            // Process the sensor data as needed
            Log.d(TAG, "Sensor data: " + data);
        }
        else
        {
            Log.e(TAG, "Characteristic read failed. status: " + status);
        }


    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        // Handle characteristic value changes here
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        // Handle descriptor read here
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        // Handle descriptor write here
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        // Handle reliable write completion here
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        // Handle read remote RSSI here
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        // Handle MTU change here
    }

}
