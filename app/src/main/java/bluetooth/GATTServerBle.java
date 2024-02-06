package bluetooth;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.UUID;
public class GATTServerBle extends Service{

    private final static String TAG = GATTServerBle.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothGattService mBluetoothGattService;


    /* adding gatt server callback implementation */
    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            // Handle connection state changes here
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
            // Handle service addition status here
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            // Handle read requests here
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            // Handle write requests here
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            super.onExecuteWrite(device, requestId, execute);
            // Handle execute write requests here
        }

        @Override
        public void onCharacteristicChanged(BluetoothDevice device, BluetoothGattCharacteristic characteristic) {
            // Handle characteristic change notifications here
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            // Handle descriptor read requests here
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            // Handle descriptor write requests here
        }

        @Override
        public void onDescriptorRead(BluetoothDevice device, int requestId, int status, BluetoothGattDescriptor descriptor) {
            super.onDescriptorRead(device, requestId, status, descriptor);
            // Handle descriptor read status here
        }

        @Override
        public void onDescriptorWrite(BluetoothDevice device, int requestId, int status) {
            super.onDescriptorWrite(device, requestId, status);
            // Handle descriptor write status here
        }

        @Override
        public void onReliableWriteCompleted(BluetoothDevice device, int status) {
            super.onReliableWriteCompleted(device, status);
            // Handle reliable write status here
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void onServiceChanged(BluetoothDevice device, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Service changed successfully");
            } else {
                Log.d(TAG, "Service change failed, status: " + status);
            }
        }
    };

    // local binder class created to bind gatt server to service
    private final IBinder mBinder = new LocalBinder();

    private class LocalBinder extends Binder {
        GATTServerBle getService() {
            return GATTServerBle.this;
        }
    }


    // implemented method of service class necessary for binding
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothGattServer = bluetoothAdapter.openGattServer(this, mGattServerCallback);
        mBluetoothGattService = new BluetoothGattService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"), BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // uuid for heart rate service
        BluetoothGattCharacteristic heartRateCharacteristic = new BluetoothGattCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"), BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);
        mBluetoothGattService.addCharacteristic(heartRateCharacteristic);

        // Accelerometer measurement characteristic
        BluetoothGattCharacteristic accelerometerCharacteristic = new BluetoothGattCharacteristic(UUID.fromString("00002a53-0000-1000-8000-00805f9b34fb"), BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ);
        mBluetoothGattService.addCharacteristic(accelerometerCharacteristic);

        // Gyroscope measurement characteristic
        BluetoothGattCharacteristic gyroscopeCharacteristic = new BluetoothGattCharacteristic(UUID.fromString("00002a5f-0000-1000-8000-00805f9b34fb"), BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ);
        mBluetoothGattService.addCharacteristic(gyroscopeCharacteristic);

        // Magnetometer measurement characteristic
        BluetoothGattCharacteristic magnetometerCharacteristic = new BluetoothGattCharacteristic(UUID.fromString("00002a5e-0000-1000-8000-00805f9b34fb"), BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_READ);
        mBluetoothGattService.addCharacteristic(magnetometerCharacteristic);

        mBluetoothGattServer.addService(mBluetoothGattService);
    }

}
