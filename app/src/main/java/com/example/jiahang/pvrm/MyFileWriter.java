package com.example.jiahang.pvrm;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.ViewDebug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//TODO: import color red
//import static com.example.jiahang.pvrm.R.color.red;

/**
 * Created by zifeifeng on 10/20/17.
 */

public class MyFileWriter {
    private String filename;
    //FileOutputStream outputStream;
    File file;
    Context context;
    private static String current_speed;
    private static String current_bicept;
    private static String current_tricept;
    private static String current_load;
    private static String current_angle;


    private static float speed_sum = 0;
    private static int count=0;
    private static FileWriter mFileWriter;
    private static MyFileWriter mMyFileWriter;
    private static String mUserId;
    public static Handler mHandlerToFront;
    public static final String filepath = "bluetoothclass4";
    private static int speed_count = 0;
    private static int bicept_count=0;
    private static float bicept_sum= 0;
    private static float tricept_sum = 0;
    private static int triept_count=0;
    private static float avg_bicept=0;
    private static float avg_tricept=0;
    private void _create(String userid, String initialContent){
        filename = userid+".txt";

        mUserId = userid;
        Shared.putBoolean(context, Shared.HAS_BEEN_SAHRED, false);
        Shared.putString(context, Shared.SUBJECT_ID, userid);

        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+filepath);
        directory.mkdirs();
        file = new File(context.getExternalFilesDir(filepath), filename);
        try {
            mFileWriter = new FileWriter(file, true);
        }catch (IOException e ){
            Log.e("MyFileWriter", e.toString());
        }
//        try {
//            outputStream = new FileOutputStream(file);
//        }catch (IOException e){
//            Log.e("File Writer", "File not found");
//        }

        Log.e("File create", "File is created in "+file.getAbsolutePath());
        writeData(initialContent);
    }


    public static MyFileWriter get(String userid, String initialContent, Context mContext){
        if(mMyFileWriter == null){
            mMyFileWriter = new MyFileWriter(userid,mContext, initialContent);
        }
        else if(!mUserId.equals(userid) && userid!=null){
            mMyFileWriter.updateId(userid, initialContent);
        }
        //TODO: what if mUserId already exists? Then we must modify that existing file
        else if(mUserId.equals(userid) && userid != null){

            mMyFileWriter = new MyFileWriter(userid, mContext, initialContent);
        }
        return mMyFileWriter;
    }
    private MyFileWriter(String userid, Context context, String initialContent){
        this.context = context;
        _create(userid, initialContent);
    }

    public  void updateId(String userid, String initialContent){
        if(file!=null){
            try {
                mFileWriter.close();
            }catch (Exception e){

            }
        }
        file = null;
        filename = userid;
        _create(userid, initialContent);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    public void writeData(String str){
        try {
            mFileWriter.write(str);
            String[] tmp = str.split(",");
            if(tmp.length>4){
                count+=1;

                float tmp_speed = Math.abs(Float.parseFloat(tmp[2]));
                current_angle = tmp[0];
                current_load = tmp[1];
                current_speed = tmp[2];
                current_bicept = tmp[3];
                current_tricept = tmp[4];

                if(tmp_speed > 20){
                    speed_sum += tmp_speed;
                    speed_count+=1;
                }
                bicept_count = Math.abs(Float.parseFloat(tmp[3]))>avg_bicept? bicept_count+1:0;
                triept_count = Math.abs(Float.parseFloat(tmp[4]))>avg_tricept?triept_count+1:0;
            }
            else{
                count = 0;
                bicept_count = 0;
                triept_count = 0;
                avg_bicept = Shared.getFloat(context, Shared.AVG_BICEPT, 0);
                avg_tricept = Shared.getFloat(context, Shared.AVG_TRICEPT, 0);
                speed_sum = 0;
                speed_count =0;
            }
            Log.e("FileWriter", str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Write File", e.toString());
        }finally {
            try {
                mFileWriter.flush();
            }catch (IOException e){
                Log.e("Filewriter flush", e.toString());
            }
        }
    }

    public void closeFileWriter() {
        try {
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilepath(){
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "Android/data/com.example.jiahang.pvrm/files/bluetoothclass4" + File.separator + mUserId+".txt";
    }

    public static float getAvgNumber(){
        Log.e("speedssssss sum_count", ""+speed_sum+"  "+speed_count);
        float res = speed_count<20? 0: speed_sum /speed_count;
        return res;
    }
    public static boolean biceptActive(){
        return bicept_count>15000;
    }
    public static boolean triceptActive(){
        return triept_count>15000;
    }

    public static float getAvgbicept(){
        return bicept_sum/count;
    }
    public static float getAvgtricept(){
        return tricept_sum/count;
    }

    public static String[] getCurrentData() {
        return new String[]{current_tricept, current_bicept, current_angle, current_load, current_speed};
    }
    public static void flush(){
        try {
            mFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
