package com.example.jiahang.pvrm;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.jiahang.pvrm.connect.Constant;

/**
 * Created by zifeifeng on 11/7/17.
 */

public class MyDataHandler extends Handler {
    Context mContext;
    private MyFileWriter mMyFileWriter;
    private PopupFragment mPopupFragment;
    interface PopupFragment{
        void succeed();
        void fail();
    }
    public MyDataHandler(Context context){
        mContext = context;
        mPopupFragment = (PopupFragment) mContext;
        mMyFileWriter = MyFileWriter.get(null, null, context);
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Constant.MSG_START_LISTENING:
                break;
            case Constant.MSG_FINISH_LISTENING:
                break;
            case Constant.MSG_GOT_DATA:
                String str = String.valueOf(msg.obj);
                mMyFileWriter.writeData(str);
                break;
            case Constant.MSG_ERROR:
                Toast.makeText(mContext,"error: "+String.valueOf(msg.obj), Toast.LENGTH_SHORT);
                mPopupFragment.fail();
                break;
            case Constant.MSG_CONNECTED_TO_SERVER:
                String name = String.valueOf(msg.obj);
                Toast.makeText(mContext, "Connected to Server "+name, Toast.LENGTH_SHORT);
                Log.e("Handler", "connect successful");
                mPopupFragment.succeed();
                break;
        }
    }
}
