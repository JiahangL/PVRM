package com.example.jiahang.pvrm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2015/5/27.
 */
public class BlueToothController {
    private static BlueToothController mController;
    private BluetoothAdapter mAapter;
    public static BlueToothController get(Context context){
        if(mController == null){
            mController = new BlueToothController();
        }
        return mController;
    }
    private BlueToothController() {
        mAapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getAdapter() {
        return mAapter;
    }
    /**
     * Turn on bluetooth
     * @param activity
     * @param requestCode
     */
    public void turnOnBlueTooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
//        mAdapter.enable();
    }

    /**
     * enable bluetooth to be visible by other device
     * @param context
     */
    public void enableVisibly(Context context) {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }

    /**
     * Find Device
     */
    public void findDevice() {
        assert (mAapter != null);
        mAapter.startDiscovery();
    }

    /**
     * get bounded device
     * @return
     */
    public List<BluetoothDevice> getBondedDeviceList() {
        return new ArrayList<>(mAapter.getBondedDevices());
    }
}
