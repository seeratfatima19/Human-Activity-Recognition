package bluetooth;


import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class MyGattCallback extends BluetoothGattCallback {
    private static final String TAG = "MyGattCallback";
    private final Queue<BluetoothGattCharacteristic> mCharacteristicsToRead = new LinkedList<>();
    private Handler handler = new Handler();

    //  uuids for the services

    private static final UUID ACCELEROMETER_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    private static final UUID MAGNETOMETER_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    private static final UUID GYROSCOPE_SERVICE_UUID = UUID.fromString("0000ff10-0000-1000-8000-00805f9b34fb");
    private static final UUID HEART_RATE_SERVICE_UUID= UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb");
    private static final UUID ANOTHER_SERVICE_UUID = UUID.fromString("0000ff00-0000-1000-8000-00805f9b34fb");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

    // uuid for sensor characteristic
    private static final UUID ACCELEROMETER_DATA_CHARACTERISTIC_UUID = UUID.fromString("00002A5D-0000-1000-8000-00805F9B34FB");
    private static final UUID MAGNETOMETER_DATA_CHARACTERISTIC_UUID = UUID.fromString("00002a0e-0000-1000-8000-00805f9b34fb");
    private static final UUID GYROSCOPE_DATA_CHARACTERISTIC_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
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
        Log.d(TAG, "In OnServices discovered: " + status);

        if (status == BluetoothGatt.GATT_SUCCESS) {
            // Get the list of services
            List<BluetoothGattService> services = gatt.getServices();
            Log.d(TAG, "Services: " + services.size());
            // Loop through the services
            for (BluetoothGattService service : services) {
                Log.d(TAG, "Service: " + service.getUuid());

                if(service.getUuid().equals(ACCELEROMETER_SERVICE_UUID) || service.getUuid().equals(MAGNETOMETER_SERVICE_UUID) || service.getUuid().equals(GYROSCOPE_SERVICE_UUID) || service.getUuid().equals(HEART_RATE_SERVICE_UUID)) {

                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        Log.d(TAG, "Characteristic: " + characteristic.getUuid());
                            // Enable notifications
                            gatt.setCharacteristicNotification(characteristic, true);
                        if ((characteristic.getProperties() & (BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY)) != 0)
                        {
                            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
                            if(descriptor != null) {
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                gatt.writeDescriptor(descriptor);
                            }
                            else {
                                Log.e(TAG, "Descriptor is null");
                            }
                            mCharacteristicsToRead.add(characteristic);

                        }
                        else {
                            Log.e(TAG, "Characteristic does not have read or notify property");
                        }


                    }
                }

            }

            readNextCharacteristic(gatt);
        }
        else {
            Log.e(TAG, "Service discovery failed. status: " + status);
        }
    }

    ///
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        if (status == BluetoothGatt.GATT_SUCCESS) {
            // Get the sensor data from the characteristic
            byte[] data = characteristic.getValue();
            String string_data = new String(data, StandardCharsets.US_ASCII);
            // Process the sensor data as needed
            Log.d(TAG, "Sensor data: " + string_data);
        }
        else
        {
            Log.e(TAG, "Characteristic read failed. status: " + status);
        }

        readNextCharacteristic(gatt);


    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        // Handle characteristic value changes here
        // Get the sensor data from the characteristic
        mCharacteristicsToRead.add(characteristic);
        readNextCharacteristic(gatt);
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

    @SuppressLint("MissingPermission")
    private void readNextCharacteristic(BluetoothGatt gatt) {
        if (!mCharacteristicsToRead.isEmpty()) {
            BluetoothGattCharacteristic characteristic = mCharacteristicsToRead.poll();
            gatt.readCharacteristic(characteristic);
        }
    }


}
