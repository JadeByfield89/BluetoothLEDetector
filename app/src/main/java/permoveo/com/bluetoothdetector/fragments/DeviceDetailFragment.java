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
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.bluetoothdetector.R;
import permoveo.com.bluetoothdetector.constants.BluetoothConstants;
import permoveo.com.bluetoothdetector.model.Device;
import permoveo.com.bluetoothdetector.util.BleWrapperUiCallbacks;
import permoveo.com.bluetoothdetector.util.Bluetooth;

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
    String mManufacturerNameText = "Manufacturer Name: ";

    @Bind(R.id.tv_model_number)
    TextView mModelNumber;
    String mModelNumberText = "Model Number: ";


    @Bind(R.id.tv_serial_number)
    TextView mSerialNumber;
    String mSerialNumberText = "Serial Number: ";


    @Bind(R.id.tv_local_time)
    TextView mLocalTime;
    String mLocalTimeText = "Local Time: ";


    @Bind(R.id.tv_battery_level)
    TextView mBatteryLevel;
    String mBatteryLevelText = "Battery Level: ";


    private BluetoothDevice mBluetoothDevice;
    private static final String EXTRA_DEVICE = "device";
    private static final String EXTRA_BLUETOOTH_DEVICE = "bt_device";

    private List<BluetoothGattCharacteristic> mCharacteristics;
    private int mCurrentChara = 0;


    String mDeviceNameText = "Device Name: ";
    private BluetoothGatt mBluetoothGatt;

    private Bluetooth mBluetooth;


    private BleWrapperUiCallbacks mCallbacks = new BleWrapperUiCallbacks() {
        @Override
        public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List<BluetoothGattService> services) {
            Log.d("DeviceDetailFragment", "uiAvailableServices");
            Log.d("DeviceDetailFragment", "Found " + services.size() + " services!");


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mProgressText.setText("Getting device information");

                }
            });

            boolean foundDeviceInfoService = false;

            // Look for the device information service
            for (BluetoothGattService service : services) {
                Log.d("DeviceDetailFragment", "Service UUID -> " + service.getUuid());


                // Device information service found
                if (service.getUuid().toString().equals(BluetoothConstants.BT_SERVICE_DEVICE_INFORMATION)) {
                    Log.d("DeviceDetailFragment", "Device information service found!");
                    foundDeviceInfoService = true;

                    // Get the characteristics for this service
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    Log.d("DeviceDetail", "Found  " + characteristics.size() + " characteristics!");

                    mBluetooth.getCharacteristicsForService(service);

                }
            }

            if (!foundDeviceInfoService) {
                Log.d("DeviceDetailFragment", "Device Info service not found");
                //Toast.makeText(getContext(), "Device Information service not found on this device!", Toast.LENGTH_LONG).show();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressText.setVisibility(View.INVISIBLE);
                        mProgress.setVisibility(View.INVISIBLE);

                        mDeviceName.setVisibility(View.VISIBLE);
                        mManufacturerName.setVisibility(View.VISIBLE);
                        mModelNumber.setVisibility(View.VISIBLE);
                        mSerialNumber.setVisibility(View.VISIBLE);
                        mLocalTime.setVisibility(View.VISIBLE);
                        mBatteryLevel.setVisibility(View.VISIBLE);

                        mDeviceName.setText(mDeviceNameText);
                        mModelNumber.setText(mModelNumberText);
                        mManufacturerName.setText(mManufacturerNameText);
                        mSerialNumber.setText(mSerialNumberText);
                        mLocalTime.setText(mLocalTimeText);
                        mBatteryLevel.setText(mBatteryLevelText);

                    }
                });
            }

        }

        @Override
        public void uiCharacteristicForService(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, List<BluetoothGattCharacteristic> chars) {
            Log.d("DeviceDetailFragment", "uiCharacteristicForService");

            for (BluetoothGattCharacteristic characteristic : chars) {

                // mBluetooth.requestCharacteristicValue(chara);
                Log.d("DeviceDetailFragment", "Characteristic -> " + characteristic.getUuid());

                mBluetooth.requestCharacteristicValue(characteristic);


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressText.setVisibility(View.INVISIBLE);
                        mProgress.setVisibility(View.INVISIBLE);

                        mDeviceName.setVisibility(View.VISIBLE);
                        mManufacturerName.setVisibility(View.VISIBLE);
                        mModelNumber.setVisibility(View.VISIBLE);
                        mSerialNumber.setVisibility(View.VISIBLE);
                        mLocalTime.setVisibility(View.VISIBLE);
                        mBatteryLevel.setVisibility(View.VISIBLE);

                        mDeviceName.setText(mDeviceNameText);
                        mModelNumber.setText(mModelNumberText);
                        mManufacturerName.setText(mManufacturerNameText);
                        mSerialNumber.setText(mSerialNumberText);
                        mLocalTime.setText(mLocalTimeText);
                        mBatteryLevel.setText(mBatteryLevelText);

                        Toast.makeText(getContext(), "Device Information Found!", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        @Override
        public void uiDeviceConnected(BluetoothGatt gatt, BluetoothDevice device) {
            Log.d("DeviceDetailFragment", "uiDeviceConnected");


            mBluetooth.startServicesDiscovery();

        }

        @Override
        public void uiDeviceDisconnected(BluetoothGatt gatt, BluetoothDevice device) {
            Log.d("DeviceDetailFragment", "uiDeviceDisconnected");

        }

        @Override
        public void uiCharacteristicsDetails(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic characteristic) {
            Log.d("DeviceDetailFragment", "uiCharacteristicDetails");

        }

        @Override
        public void uiNewValueForCharacteristic(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic characteristic, String strValue, int intValue, byte[] rawValue, String timestamp) {

            if (characteristic != null && characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_DEVICE_NAME)) {

                mDeviceNameText = "Device Name: " + characteristic.getStringValue(0);


            } else if (characteristic != null && characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_SERIAL_NUMBER)) {

                mSerialNumberText = "Serial Number: " + String.valueOf(characteristic.getStringValue(0));


            } else if (characteristic != null && characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_MODEL_NUMBER)) {


                mModelNumberText = "Model Number: " + characteristic.getStringValue(0);


            } else if (characteristic != null && characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_BATTERY_LEVEL)) {

                mBatteryLevelText = "Battery Level: " + String.valueOf(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0) + "%");


            } else if (characteristic != null && characteristic.getUuid().toString().equals(BluetoothConstants.BT_CHAR_MANUFACTURER_NAME)) {

                mManufacturerNameText = "Manufacturer Name: " + characteristic.getStringValue(0);


            } else if (characteristic != null && characteristic.getUuid().toString().equals(BluetoothConstants.BT_LOCAL_TIME)) {

                mBatteryLevelText = "Local Time: " + String.valueOf(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));


            }


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    mDeviceName.setVisibility(View.VISIBLE);
                    mManufacturerName.setVisibility(View.VISIBLE);
                    mModelNumber.setVisibility(View.VISIBLE);
                    mSerialNumber.setVisibility(View.VISIBLE);
                    mLocalTime.setVisibility(View.VISIBLE);
                    mBatteryLevel.setVisibility(View.VISIBLE);

                    mDeviceName.setText(mDeviceNameText);
                    mModelNumber.setText(mModelNumberText);
                    mManufacturerName.setText(mManufacturerNameText);
                    mSerialNumber.setText(mSerialNumberText);
                    mLocalTime.setText(mLocalTimeText);
                    mBatteryLevel.setText(mBatteryLevelText);

                }
            });
        }
    };


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


        mBluetooth = Bluetooth.getInstance(getActivity(), mCallbacks);
        mBluetooth.setDependencies(getActivity(), mCallbacks);
        mBluetooth.initialize();


        Log.d("DeviceDetailFragment", "OnCreateView, starting Gatt connection");
        mBluetooth.connect(mBluetoothDevice.getAddress());


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d("DeviceDetailFragment", "OnPause, disconnecting GATT connection");
        mBluetooth.disconnect();
        mBluetooth.close();

    }

    public static DeviceDetailFragment newInstance(Device device, BluetoothDevice btdevice) {


        Bundle args = new Bundle();

        args.putSerializable(EXTRA_DEVICE, device);
        args.putParcelable(EXTRA_BLUETOOTH_DEVICE, btdevice);
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
