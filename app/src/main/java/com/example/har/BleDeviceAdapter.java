package com.example.har;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class BleDeviceAdapter extends RecyclerView.Adapter<BleDeviceAdapter.BleDeviceViewHolder> {
    private List<BluetoothDevice> bleDevices;
    private Context context;

    public BleDeviceAdapter(Context context, List<BluetoothDevice> bleDevices) {
        this.context = context;
        this.bleDevices = bleDevices;
    }

    public void updateDevices(List<BluetoothDevice> devices) {

        Log.d("BLE Adapter","update devices called");
        this.bleDevices = devices;
        notifyDataSetChanged();
    }

    public List<BluetoothDevice> getBleDevices() {
        return bleDevices;
    }

    public void addDevice(BluetoothDevice device) {
        if (!bleDevices.contains(device)) {
            bleDevices.add(device);
            notifyItemInserted(bleDevices.size() - 1);
        }
    }

    @NonNull
    @Override
    public BleDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BleDeviceViewHolder(LayoutInflater.from(context).inflate(R.layout.ble_device_item, parent, false));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull BleDeviceViewHolder holder, int position) {
        BluetoothDevice device = bleDevices.get(position);
        holder.deviceNameTextView.setText(device.getName());
        holder.deviceAddressTextView.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return bleDevices.size();
    }

    public static class BleDeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceNameTextView;
        public TextView deviceAddressTextView;

        public BleDeviceViewHolder(View itemView) {
            super(itemView);
            deviceNameTextView = itemView.findViewById(R.id.device_name);
            deviceAddressTextView = itemView.findViewById(R.id.device_address);
        }
    }
}