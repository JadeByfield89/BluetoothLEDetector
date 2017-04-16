package permoveo.com.bluetoothdetector.fragments;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.bluetoothdetector.R;
import permoveo.com.bluetoothdetector.constants.BluetoothConstants;
import permoveo.com.bluetoothdetector.model.Device;

/**
 * Created by byfieldj on 4/15/17.
 */

public class DeviceDetailFragment extends DialogFragment {

    @Bind(R.id.progress)
    ProgressBar mProgress;

    @Bind(R.id.tv_progress_text)
    TextView mProgressText;

    @Bind(R.id.tv_device_name)
    TextView mDeviceName;

    @Bind(R.id.tv_manufacturer_name)
    TextView mManufacturerName;
    String mManufacturerNameText;

    @Bind(R.id.tv_model_number)
    TextView mModelNumber;
    String mModelNumberText;


    @Bind(R.id.tv_serial_number)
    TextView mSerialNumber;
    String mSerialNumberText;


    @Bind(R.id.tv_local_time)
    TextView mLocalTime;
    String mLocalTimeText;


    @Bind(R.id.tv_battery_level)
    TextView mBatteryLevel;
    String mBatteryLevelText;


    private BluetoothDevice mBluetoothDevice;
    private static final String EXTRA_DEVICE = "device";
    private static final String EXTRA_BLUETOOTH_DEVICE = "bt_device";


    String mDeviceNameText = "";
    private BluetoothGatt mBluetoothGatt;


    @Override
    public void onStart() {
        super.onStart();

        final View decorView = getDialog()
                .getWindow()
                .getDecorView();

        // Let's give this fragment some personality with a Wave animation once it is started
        YoYo.with(Techniques.Wave).duration(600).playOn(decorView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        mBluetoothDevice = getArguments().getParcelable(EXTRA_BLUETOOTH_DEVICE);

        if (mBluetoothDevice != null) {
            Log.d("DeviceDetailFragment", "Bluetooth Device not null!");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_detail_fragment, container, false);
        ButterKnife.bind(this, view);


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                Log.d("DeviceDetailFragment", "OnCreateView, starting Gatt connection");
                mBluetoothGatt = mBluetoothDevice.connectGatt(getContext(), false, btleGattCallback);


            }
        });


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d("DeviceDetailFragment", "OnPause, disconnecting GATT connection");
        mBluetoothGatt.disconnect();
    }

    public static DeviceDetailFragment newInstance(Device device, BluetoothDevice btdevice) {


        Bundle args = new Bundle();

        args.putSerializable(EXTRA_DEVICE, device);
        args.putParcelable(EXTRA_BLUETOOTH_DEVICE, btdevice);
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d("DeviceDetailFragment", "OnCharacteristicRead");


            if (characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_DEVICE_NAME)) {

                Log.d("DeviceDetailFragment", "Characteristic -> " + characteristic.getStringValue(0));
                mDeviceNameText += "\n\n Device Name: " + characteristic.getStringValue(0);


            } else if (characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_SERIAL_NUMBER)) {

                mSerialNumberText += "Serial Number: " + String.valueOf(characteristic.getStringValue(0));


            } else if (characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_MODEL_NUMBER)) {

                mModelNumberText += "Model Number: " + String.valueOf(characteristic.getStringValue(0));


            } else if (characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_BATTERY_LEVEL)) {

                mBatteryLevelText += "Battery Level: " + String.valueOf(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0) + "%");


            }

            else if (characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_MANUFACTURER_NAME)) {

                mBatteryLevelText += "Manufacturer Name: " + String.valueOf(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0) + "%");


            }

            else if (characteristic.getUuid().toString().equals(BluetoothConstants.BT_LOCAL_TIME)) {

                mBatteryLevelText += "Local Time: " + String.valueOf(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));


            }


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mDeviceName.setText(mDeviceNameText);
                    mModelNumber.setText(mModelNumberText);
                    mManufacturerName.setText(mManufacturerNameText);
                    mSerialNumber.setText(mSerialNumberText);
                    mLocalTime.setText(mLocalTimeText);
                    mBatteryLevel.setText(mBatteryLevelText);


                }
            });


        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            Log.d("DeviceDetailFragment", "onConnectionStateChange");

            // this will get called when a device connects or disconnects

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("DeviceDetailFragment", "Device connected!");

                gatt.discoverServices();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressText.setText("Getting device information");

                    }
                });


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("DeviceDetailFragment", "Device disconnected!");
                gatt.disconnect();

            }


        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            Log.d("DeviceDetailFragment", "onServicesDiscovered");
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mProgress.setVisibility(View.INVISIBLE);
                    mProgressText.setVisibility(View.INVISIBLE);

                    mDeviceName.setVisibility(View.VISIBLE);
                    mManufacturerName.setVisibility(View.VISIBLE);
                    mModelNumber.setVisibility(View.VISIBLE);
                    mSerialNumber.setVisibility(View.VISIBLE);
                    mLocalTime.setVisibility(View.VISIBLE);
                    mBatteryLevel.setVisibility(View.VISIBLE);






                }
            });

            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                Log.d("DeviceDetailFragment", "Device service - > " + service.getUuid().toString());
                Log.d("DeviceDetailFragment", "Found  " + characteristics.size() + " characteristics!");


                //getDeviceInformationCharacteristics();


                for (BluetoothGattCharacteristic characteristic : characteristics) {

                    Log.d("DeviceDetailFragment", "Device characteristic - > " + characteristic.getUuid().toString());

                    gatt.readCharacteristic(characteristic);

                }

            }
        }
    };

    private void getGenericAccessCharacteristics() {
        Log.d("DeviceDetailFragment", "getGenericAccessCharacteristics");

        BluetoothGattService service = mBluetoothGatt.getService(BluetoothConstants.GENERIC_ACCESS_UUID);

        if (service == null) {
            Log.d("DeviceDetailFragment", "Generic Access Service not found on this device!");
        } else {
            BluetoothGattCharacteristic device_name = service.getCharacteristic(BluetoothConstants.DEVICE_NAME_UUID);
            BluetoothGattCharacteristic peripheral_privacy_flag = service.getCharacteristic(BluetoothConstants.PRIVACY_FLAG_UUID);


            // Check for the Device Name characteristic

            if (device_name == null) {
                Log.d("DeviceDetailFragment", "Device Name Characteristic not found on this device!");

            } else {

                mBluetoothGatt.readCharacteristic(device_name);

            }


            // Check for the Peripheral Privacy Flag characteristic
            if (peripheral_privacy_flag == null) {
                Log.d("DeviceDetailFragment", "Peripheral Privacy Flag Characteristic not found on this device!");

            } else {

                mBluetoothGatt.readCharacteristic(peripheral_privacy_flag);

            }
        }
    }

    private void getDeviceInformationCharacteristics() {
        Log.d("DeviceDetailFragment", "getDeviceInformationCharacteristics");

        BluetoothGattService service = mBluetoothGatt.getService(BluetoothConstants.DEVICE_INFO_UUID);

        if (service == null) {
            Log.d("DeviceDetailFragment", "Device Information Service not found on this device!");

        } else {

            // Manufacturer Name
            BluetoothGattCharacteristic manufacturer_name = service.getCharacteristic(BluetoothConstants.MANUFACTURER_NAME_UUID);
            if (manufacturer_name == null) {
                Log.d("DeviceDetailFragment", "Manufacturer Name characteristic not found on this device!");

            } else {
                mBluetoothGatt.readCharacteristic(manufacturer_name);

            }

            // Serial Number
            BluetoothGattCharacteristic serial_number = service.getCharacteristic(BluetoothConstants.SERIAL_NUMBER_UUID);
            if (serial_number == null) {
                Log.d("DeviceDetailFragment", "Serial Number characteristic not found on this device!");

            } else {
                mBluetoothGatt.readCharacteristic(serial_number);

            }


            // Model Number
            BluetoothGattCharacteristic model_number = service.getCharacteristic(BluetoothConstants.MODEL_NUMBER_UUID);
            if (serial_number == null) {
                Log.d("DeviceDetailFragment", "Model Number characteristic not found on this device!");

            } else {
                mBluetoothGatt.readCharacteristic(model_number);

            }


        }


    }
}
