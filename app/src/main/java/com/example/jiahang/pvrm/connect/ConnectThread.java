package com.example.jiahang.pvrm.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.example.jiahang.pvrm.MyDataHandler;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Rex on 2015/5/30.
 */
public class ConnectThread extends Thread {
    private static final UUID MY_UUID = Constant.CONNECTTION_UUID;
    private static BluetoothSocket mmSocket;
    private static BluetoothDevice mmDevice;
    private static ConnectThread mConnectThread;
    private static Handler mHandler;

    public static ConnectThread get(BluetoothDevice device, BluetoothAdapter adapter, Handler handler){
        if (device ==null&&mHandler==null) //this is the first time to enter the main activity, and no device is connected
            return null;
        if(mConnectThread  == null){ //find the connect device in the first time
            mConnectThread = new ConnectThread(device, adapter, handler);
        }
        return mConnectThread;
    }

    private BluetoothAdapter mBluetoothAdapter;

    private static ConnectedThread mConnectedThread;
    private String TAG = "connect thread";
    private final String mSocketType = "RFCOMM";

    private ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, Handler handler) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mBluetoothAdapter = adapter;
        mHandler = handler;
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e("Connect Threadtmp", e.toString());
        }
        mmSocket = tmp;

    }

    public void run(){
        mBluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            BluetoothSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
            } catch (IOException e) {
                Log.e("Connect Thread0", connectException.toString());
                mHandler.obtainMessage(Constant.MSG_ERROR).sendToTarget();
                return;
            }
        }
        manageConnectedSocket(mmSocket);
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        mHandler.obtainMessage(Constant.MSG_CONNECTED_TO_SERVER,mmSocket.getRemoteDevice().getName()).sendToTarget();//apple
        if(mConnectedThread == null)
            mConnectedThread = new ConnectedThread(mmSocket, mHandler);
        mConnectedThread.start();
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
            if(mConnectThread!=null)
            mConnectThread.interrupt();
            mConnectThread=null;
        } catch (IOException e) { }
    }

    public void sendData(byte[] data) {
        if( mConnectedThread!=null){
            mConnectedThread.write(data);
            Log.e("send data", new String(data));
        }
        else{
            Log.e("ConnectThread send Data", "mConnectedThread == null");
        }
    }
}