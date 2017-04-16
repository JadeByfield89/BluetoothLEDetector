package permoveo.com.bluetoothdetector.interfaces;

import permoveo.com.bluetoothdetector.model.Device;

/**
 * Created by byfieldj on 4/14/17.
 */

public interface OnDeviceConnectionListener {

    void onBluetoothDeviceConnected(Device device);
    void onBluetoohDeviceError();
}
