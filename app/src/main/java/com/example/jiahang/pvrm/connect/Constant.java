package com.example.jiahang.pvrm.connect;


 import java.util.UUID;

 /**
 * Created by Rex on 2015/5/30.
 */
 import java.util.UUID;

/**
 * Created by Rex on 2015/5/30.
 */
public class Constant {
    public static final UUID CONNECTTION_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * start listening
     */

    public static final int MSG_START_LISTENING = 1;

    /**
     * finish listening
     */
    public static final int MSG_FINISH_LISTENING = 2;

    /**
     * got a client
     */
    public static final int MSG_GOT_A_CLINET = 3;

    /**
     * connect to server
     */
    public static final int MSG_CONNECTED_TO_SERVER = 4;

    /**
     * get data/ message from another device
     */
    public static final int MSG_GOT_DATA = 5;

    /**
     * have error
     */
    public static final int MSG_ERROR = -1;
}