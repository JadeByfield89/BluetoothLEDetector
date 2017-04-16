package permoveo.com.bluetoothdetector.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.bluetoothdetector.R;
import permoveo.com.bluetoothdetector.adapters.BluetoothDeviceAdapter;
import permoveo.com.bluetoothdetector.fragments.DeviceDetailFragment;
import permoveo.com.bluetoothdetector.model.Device;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private static final long SCAN_PERIOD = 60000;
    private static final int PERMISSION_REQUEST_LOCATION = 7;

    private ArrayList<Device> mDevices = new ArrayList<Device>();
    private ArrayList<BluetoothDevice> mFilteredDevices = new ArrayList<BluetoothDevice>();

    private BluetoothDeviceAdapter mAdapter;


    @Bind(R.id.recycler)
    RecyclerView mDevicesList;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            // This prevents showing duplicate devices in the list
                            if (!mFilteredDevices.contains(device)) {
                                Device foundDevice = new Device();

                                foundDevice.setDeviceName(device.getName());
                                foundDevice.setDeviceId(device.getAddress());
                                foundDevice.setSignalStrength(rssi);
                                mAdapter.addDevice(foundDevice);
                                mFilteredDevices.add(device);

                                Log.d("MainActivity", "Device RSSI -> " + rssi);
                                Log.d("MainActivity", "Found Device!");


                            }


                        }
                    });
                }

                ;
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        mDevicesList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BluetoothDeviceAdapter(this, mDevices);

        mDevicesList.setAdapter(mAdapter);
        mAdapter.setListener(new BluetoothDeviceAdapter.OnDeviceSelectedListener() {
            @Override
            public void onDeviceSelected(Device device, int position) {
                showDeviceDetailFragment(device, position);
            }
        });


        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Android M Permission check 
            // Android 6.0 and above requires Location permissions to be turned ON for Bluetooth LE scans to work properly
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Location permission needed!");

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);

                        //scanLeDevice(true);
                    }

                });
                builder.show();
            }
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(true);
        }


        // Make this device discoverable to other devices
       /* if (mBluetoothAdapter.getScanMode() !=
                android.bluetooth.BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            android.content.Intent discoverableIntent =
                    new android.content.Intent(
                            android.bluetooth.BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(
                    android.bluetooth.BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                    7000); // You are able to set how long it is discoverable.
            startActivity(discoverableIntent);
        }*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            scanLeDevice(true);
        } else {
            Toast.makeText(this, "Please Enable Bluetooth in Settings to use this application", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeviceDetailFragment(Device device, int position) {

        DeviceDetailFragment fragment = DeviceDetailFragment.newInstance(device, mFilteredDevices.get(position));
        fragment.show(getSupportFragmentManager(), "detailFragment");
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Toast.makeText(MainActivity.this, "Bluetooth Scan Stopped", Toast.LENGTH_LONG).show();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
