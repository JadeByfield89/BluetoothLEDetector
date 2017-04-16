package permoveo.com.bluetoothdetector.model;

import java.io.Serializable;

/**
 * Created by byfieldj on 4/14/17.
 */

public class Device implements Serializable {


    private String mDeviceId;
    private String mDeviceName;
    private int mSignalStrength;


    public void setDeviceId(String id){
        this.mDeviceId = id;
    }

    public String getDeviceId(){
        return mDeviceId;
    }

    public void setDeviceName(String name){
        this.mDeviceName = name;
    }

    public String getDeviceName(){

        return mDeviceName;
    }

    public void setSignalStrength(int strength){
        this.mSignalStrength = strength;
    }

    public int getSignalStrength(){
        return mSignalStrength;
    }
}
