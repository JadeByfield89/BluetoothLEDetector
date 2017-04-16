package permoveo.com.bluetoothdetector.constants;

import java.util.UUID;

/**
 * Created by byfieldj on 4/15/17.
 */

public class BluetoothConstants {

    public static final String BT_SERVICE_DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb";
    public static final UUID DEVICE_INFO_UUID = UUID.fromString(BT_SERVICE_DEVICE_INFORMATION);


    public static final String BT_SERVICE_GENERIC_ATTRIBUTES = "00001801-0000-1000-8000-00805f9b34fb";
    public static final UUID GENERIC_ATTRIBUTES_UUID = UUID.fromString(BT_SERVICE_GENERIC_ATTRIBUTES);

    public static final String BT_SERVICE_GENERIC_ACCESS = "00001800-0000-1000-8000-00805f9b34fb";
    public static final UUID GENERIC_ACCESS_UUID = UUID.fromString(BT_SERVICE_GENERIC_ACCESS);


    public static final String BT_CHAR_BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";
    public static final UUID BATTERY_LEVEL_UUID = UUID.fromString(BT_CHAR_BATTERY_LEVEL);

    public static final String BT_CHAR_DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb";
    public static final UUID DEVICE_NAME_UUID = UUID.fromString(BT_CHAR_DEVICE_NAME);


    public static final String BT_CHAR_PRIVACY_FLAG = "00002a02-0000-1000-8000-00805f9b34fb";
    public static final UUID PRIVACY_FLAG_UUID = UUID.fromString(BT_CHAR_PRIVACY_FLAG);

    public static final String BT_CHAR_RECONNECTION_ADDRESS = "00002a03-0000-1000-8000-00805f9b34fb";
    public static final UUID RECONNECTION_ADDRESS_UUID = UUID.fromString(BT_CHAR_RECONNECTION_ADDRESS);

    public static final String BT_CHAR_PPCP = "00002a04-0000-1000-8000-00805f9b34fb";
    public static final UUID PPCP_UUID = UUID.fromString(BT_CHAR_PPCP);

    public static final String BT_CHAR_APPEARANCE = "00002a01-0000-1000-8000-00805f9b34fb";
    public static final UUID APPEARANCE_UUID = UUID.fromString(BT_CHAR_APPEARANCE);


    public static final String BT_CHAR_MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb";
    public static final UUID MANUFACTURER_NAME_UUID = UUID.fromString(BT_CHAR_MANUFACTURER_NAME);

    public static final String BT_CHAR_MODEL_NUMBER = "00002a24-0000-1000-8000-00805f9b34fb";
    public static final UUID MODEL_NUMBER_UUID = UUID.fromString(BT_CHAR_MODEL_NUMBER);

    public static final String BT_CHAR_SERIAL_NUMBER = "00002a25-0000-1000-8000-00805f9b34fb";
    public static final UUID SERIAL_NUMBER_UUID = UUID.fromString(BT_CHAR_SERIAL_NUMBER);

    public static final String BT_CHAR_FIRMWARE_REVISION = "00002a26-0000-1000-8000-00805f9b34fb";
    public static final UUID FIRMWARE_REVISION_UUID = UUID.fromString(BT_CHAR_FIRMWARE_REVISION);

    public static final String BT_LOCAL_TIME = "00002a0f-0000-1000-8000-00805f9b34fb";
    public static final UUID BT_LOCAL_TIME_UUID = UUID.fromString(BT_LOCAL_TIME);


}
