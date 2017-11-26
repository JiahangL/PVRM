package com.example.jiahang.pvrm.connect;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static com.example.jiahang.pvrm.connect.Constant.MSG_GOT_DATA;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mHandler = handler;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("ConnectedThread stream", "tmpIn/Out might be null");
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }


    //    public void updateHandler(Handler handler){
//        this.mHandler = handler;
//    }
    public void run() {
        byte[] buffer = new byte[1024];
        int begin = 0;
        int bytes = 0;
        String a = "";
        int lastHashtag = 0;
        while (true) {
            try {
                bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);//bytes counts the valid elements in buffe// r
            } catch (IOException e) {
                Log.e("blahcblahc", e.toString());
                break;
            }
            String tmp = new String(buffer);

            for (int i = begin; i < bytes; i++) {

                if (buffer[i] == "#".getBytes()[0]) {
                    lastHashtag = i;
                    byte[] buffer1 = Arrays.copyOfRange(buffer, begin, i);//the first byte read from buffer
                    String str = new String(buffer1);
                    mHandler.obtainMessage(MSG_GOT_DATA, str).sendToTarget();
                    begin = i + 1;//update begin so that when the next hashtag is read it knows where to start
                    if (i == bytes - 1) {//when the buffer becomes full and the last byte is #
                        bytes = 0;
                        begin = 0;
                    }
                } else {
                    if (i == buffer.length - 1) {//when the buffer becomes full and the last byte is a number
                        begin = 0;
                        int j = 0;
                        for (; j < buffer.length - lastHashtag - 1; j++) {
                            buffer[j] = buffer[lastHashtag + 1 + j];
                        }
                        bytes = j;
                        lastHashtag = 0;
                    }
                }
            }


        }
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("ConnectedThread write", "output Stream can't write data");
            Log.e("CONNECTED THREAD", e.toString());
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}
