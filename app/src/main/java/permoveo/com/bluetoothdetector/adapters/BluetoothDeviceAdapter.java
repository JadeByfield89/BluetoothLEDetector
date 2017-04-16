package permoveo.com.bluetoothdetector.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.bluetoothdetector.R;
import permoveo.com.bluetoothdetector.model.Device;

/**
 * Created by byfieldj on 4/14/17.
 */

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Device> mDevices;
    public static OnDeviceSelectedListener mListener;

    public BluetoothDeviceAdapter(Context context, ArrayList<Device> devices) {

        this.mContext = context;
        this.mDevices = devices;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d("BluetoothDeviceAdapter", "OnBindViewholder");
        holder.mDevice = mDevices.get(position);

        if (mDevices.get(position).getDeviceName() != null && !mDevices.get(position).getDeviceName().isEmpty()) {
            holder.mDeviceName.setText("Device Name: " + mDevices.get(position).getDeviceName());
        } else {
            holder.mDeviceName.setText("Device Name: Unknown ");

        }
        holder.mDeviceId.setText("Device Address: " + mDevices.get(position).getDeviceId());
        holder.mSignalStrength.setText("Signal Strength -> " + mDevices.get(position).getSignalStrength() + " RSSI");

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeviceSelected(holder.mDevice, position);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_row, parent, false);

        return new ViewHolder(view);
    }

    public void addDevice(Device device) {

        if (!mDevices.contains(device)) {
            mDevices.add(device);
            notifyDataSetChanged();

        }
    }

    @Override
    public int getItemCount() {


        return mDevices.size();
    }

    public void setListener(OnDeviceSelectedListener listener) {
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        Device mDevice;

        View parent;

        @Bind(R.id.tv_device_id)
        TextView mDeviceId;

        @Bind(R.id.tv_device_name)
        TextView mDeviceName;

        @Bind(R.id.tv_signal_strength)
        TextView mSignalStrength;

        public ViewHolder(View view) {
            super(view);

            this.parent = view;
            ButterKnife.bind(this, view);
        }


    }

    public interface OnDeviceSelectedListener {

        void onDeviceSelected(Device device, int position);
    }


}
