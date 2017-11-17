package com.example.jiahang.pvrm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
 import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiahang.pvrm.connect.ConnectThread;

import java.io.UnsupportedEncodingException;

import javax.xml.datatype.Duration;
import static android.R.attr.editable;
import static android.R.attr.visibility;
import static android.util.Log.e;
import static android.view.View.GONE;
import static com.example.jiahang.pvrm.Shared.ACTIVITY_TRACKER;
import static com.example.jiahang.pvrm.Shared.HAS_BEEN_SAHRED;
import static com.example.jiahang.pvrm.Shared.RECORD_STEP_TRACKER;
import static com.example.jiahang.pvrm.Shared.TO_UNFINISH;
import static com.example.jiahang.pvrm.Shared.getInt;

/**
 * Created by zifeifeng on 11/11/17.
 */

public class DataRecordActivity extends AppCompatActivity {
    ImageButton[] btns = new ImageButton[12];
    int[] counters = new int[24];
    int currentClick;
    Button btn_share;
    TextView textview_speed,  textview_bicep_active,  textview_tricep_active,  textview_flexion_title,  textview_extension_title;
    // EditText et_mts, et_mas;
    int done_step;
    int step;
    int fastTest = 12;
    CheckBox mSkipFast;
    boolean[] has_done = new boolean[24];
    ConnectThread mConnectThread;
    MyFileWriter mMyFileWriter;
    String userid;
    CountDownTimer mCountdownTimerBeforeStart;
    CountDownTimer mCountdownTimerAfterStart;
    public static String FROM_PRERECORD_ACTIVITY ="prerecordactivity";
    private static String TEST_TERM;
    int lowestBounce = 5;
    int lowerbounce = 40;
    int higherbounce = 100;

    int status = 0;
    String f_mts, f_mas, e_mas, e_mts;
    String[] status_array = {"Flexion", "Extension"};
    //done step is the number of the steps that is done == the step number that need to be done next
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_record);
        Shared.putInt(getApplicationContext(), ACTIVITY_TRACKER, 2);
        if(Shared.getBoolean(getApplicationContext(), TO_UNFINISH, false) &&!getIntent().getBooleanExtra(FROM_PRERECORD_ACTIVITY, false)) {
            done_step = step = Shared.getInt(getApplicationContext(), RECORD_STEP_TRACKER, 0);
        }
        else{
            done_step = step = 0;
        }
        status = done_step >= 12? 1:0;
        for(int i = 0; i <24; i++){
            has_done[i] = false;
        }
        for(int i = 0; i < done_step; i++){
            has_done[i] = true;
        }
        e("done step", ""+done_step);
        initUI();

        mMyFileWriter = MyFileWriter.get(null, null, null);
        mConnectThread = ConnectThread.get(null, null, null); //use the connect thread to send command
        userid = getIntent().getStringExtra(PatientQuestionnaireActivity.EXTRA_SUBJECT_ID);
        //mMyFileWriter = MyFileWriter.get(null, null, null);

        mCountdownTimerBeforeStart =  new CountDownTimer(2*1000, 250) {
            int secondsLeft = 0;
            @Override
            public void onTick(long ms) {

                if (Math.round((float)ms / 1000.0f) != secondsLeft)
                {
                    secondsLeft = Math.round((float)ms / 1000.0f);
                    btn_share.setText(""+secondsLeft+ " left before start ");
                }
            }

            @Override
            public void onFinish() {
                mMyFileWriter.writeData("\ntrail"+step%12+"-"+status_array[status]+"\n");
                say("q");//if there is not enough storage space, can't send data
                mCountdownTimerAfterStart.start();
            }
        };

        mCountdownTimerAfterStart = new CountDownTimer(3*1000, 250) {
            int secondsLeft = 0;
            @Override
            public void onTick(long ms) {
                if (Math.round((float)ms / 1000.0f) != secondsLeft)
                {
                    secondsLeft = Math.round((float)ms / 1000.0f);
                    btn_share.setText(""+secondsLeft + " left");

                }
            }

            @Override
            public void onFinish() {
                say("s");

                float speed = mMyFileWriter.getAvgNumber();
                Log.e("speedssss final", ""+speed);
                int remainder= step%12;
                btn_share.setText("SHARE");

                boolean active_tri = mMyFileWriter.triceptActive();
                boolean active_bi =mMyFileWriter.biceptActive();
                textview_bicep_active.setText(active_bi? "Active": "Passive");
                textview_tricep_active.setText(active_tri?"Active": "Passive");
                boolean b = (remainder < 3 && speed>lowestBounce&&speed<lowerbounce)||(remainder>=3 &&remainder<=5 && (speed < higherbounce&&speed>lowerbounce)) || (remainder<=8 && remainder>=6 && speed>higherbounce) ||remainder>8;
                if(!b) {
                    Bundle bundle = new Bundle();
                    bundle.putString(NegativeResultFragment.ARG_RESULT, "Please adapt the speed");
                    NegativeResultFragment dialog = new NegativeResultFragment();
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "speed");
                    mMyFileWriter.writeData("\nbad\n");
                }
                else if(active_bi||active_tri) {
                    mMyFileWriter.writeData("\nbad\n");
                }
                else {
                    has_done[done_step] = true;
                    done_step = done_step == currentClick+12*status ? done_step + 1 : done_step;
                    Shared.putInt(getApplicationContext(), RECORD_STEP_TRACKER, done_step);
                    step++;
                    Toast.makeText(getApplicationContext(), "Update steps"+"steps is " + step+" done step is "+done_step, Toast.LENGTH_LONG).show();
                    status = done_step >= 12? 1:0;
                }
                updateUI();
            }

        };
    }

    private View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            for (int i = 0; i < 12; i++) {
                if (btns[i].getId() == id) {
                    currentClick = i;
                    step = i + 12;
                    change_btns_with_hasdone();
                    btns[currentClick].setImageResource(getCorrectResource(i, 2));
                    break;
                }
            }
            mCountdownTimerBeforeStart.start();

        }
    };
    private void initUI(){
        textview_speed = (TextView) findViewById(R.id.ID_textview_speed);

        mSkipFast = (CheckBox) findViewById(R.id.checkBox_skipfast);
        mSkipFast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    for(int i = 6; i < 9; i++){
                        has_done[i] = true;
                        has_done[i+12] = true;
                        btns[i].setVisibility(GONE);
                    }
                }
                else{
                    for(int i = 6; i < 9; i++){
                        has_done[i] = false;
                        has_done[i+12] = false;
                            btns[i].setVisibility(View.VISIBLE);
                            }
                        }
                    }
        });

        textview_bicep_active = (TextView) findViewById(R.id.ID_textview_bicep_active);
        textview_tricep_active = (TextView) findViewById(R.id.ID_textview_tricep_active);
        textview_flexion_title = (TextView) findViewById(R.id.ID_textview_flexion_title);
        textview_extension_title = (TextView) findViewById(R.id.ID_textview_extension_title);

        textview_flexion_title.setOnClickListener( section_listener);
        textview_extension_title.setOnClickListener(section_listener);

        btns[0] = (ImageButton) findViewById(R.id.ID_imagebutton_slow_1);
        btns[1] = (ImageButton) findViewById(R.id.ID_imagebutton_slow_2);
        btns[2] = (ImageButton) findViewById(R.id.ID_imagebutton_slow_3);
        btns[3] = (ImageButton) findViewById(R.id.ID_imagebutton_medium_1);
        btns[4] = (ImageButton) findViewById(R.id.ID_imagebutton_medium_2);
        btns[5] = (ImageButton) findViewById(R.id.ID_imagebutton_medium_3);
        btns[6] = (ImageButton) findViewById(R.id.ID_imagebutton_fast_1);
        btns[7] = (ImageButton) findViewById(R.id.ID_imagebutton_fast_2);
        btns[8] = (ImageButton) findViewById(R.id.ID_imagebutton_fast_3);
        btns[9] = (ImageButton) findViewById(R.id.ID_imagebutton_preference_1);
        btns[10] = (ImageButton) findViewById(R.id.ID_imagebutton_preference_2);
        btns[11]= (ImageButton) findViewById(R.id.ID_imagebutton_preference_3);
        for(int i = 0; i < 12; i++) {
            btns[i].setEnabled(false);
            btns[i].setOnClickListener(ButtonClickListener);

        }
        btn_share = (Button) findViewById(R.id.ID_button_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareByEmail();
                Shared.putBoolean(getApplicationContext(), HAS_BEEN_SAHRED, true);
                Shared.putBoolean(getApplicationContext(), TO_UNFINISH, false);
            }
        });

        change_btns_with_hasdone();
        btns[done_step%12].setImageResource(getCorrectResource(done_step, 2));
        if(1==status){
            textview_flexion_title.setBackgroundColor(getResources().getColor(R.color.inactive_color));
            textview_extension_title.setBackgroundColor(getResources().getColor(R.color.primary_color));
        }

        initializeInstruction();
    }

    private void initializeInstruction(){

    }

    private int getCorrectResource(int i, int type){
        if(type == 0) {//the color is grey
            if (i % 3 == 0) {
                return (R.drawable.ic_looks_one_teal_24dp);
            } else if (i % 3 == 1) {
                return (R.drawable.ic_looks_two_teal_24dp);
            } else {
                return (R.drawable.ic_looks_3_teal_24dp);
            }
        }
        if(type == 1) {//the color is green
            if (i % 3 == 0) {
                return (R.drawable.ic_looks_one_finish_24dp);
            } else if (i % 3 == 1) {
                return (R.drawable.ic_looks_two_finish_24dp);
            } else {
                return (R.drawable.ic_looks_3_finish_24dp);
            }
        }
        else{//the color is red
            if (i % 3 == 0) {
                return (R.drawable.ic_looks_one_red_24dp);
            } else if (i % 3 == 1) {
                return (R.drawable.ic_looks_two_red_24dp);
            } else {
                return (R.drawable.ic_looks_3_red_24dp);
            }
        }
    }
    private void say(String word) {
        if( mConnectThread != null) {
            try {
                mConnectThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{
            e("Main Activity", "mConnect Thread is null");
        }
    }
    private void updateUI(){
        change_btns_with_hasdone();
        if(done_step<12){
            btns[done_step].setImageResource(getCorrectResource(done_step, 2));
        }
        else{
            if(status == 0){
                textview_flexion_title.setBackgroundColor(getResources().getColor(R.color.inactive_color));
                textview_extension_title.setBackgroundColor(getResources().getColor(R.color.primary_color));
                status=1;
                change_btns_with_hasdone();
            }
            btns[done_step%12].setImageResource(getCorrectResource(done_step,2));
        }
    }
    private void shareByEmail(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822").putExtra(Intent.EXTRA_EMAIL, new String[]{"77bisko@gmail.com"}).putExtra(android.content.Intent.EXTRA_SUBJECT, "Patient Data");
        String targetFilePath = MyFileWriter.getFilepath();
        e("share File", targetFilePath);
        Uri attachmentUri = Uri.parse(targetFilePath);
        emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
        startActivity(emailIntent);
    }

    private void changeButton(){
        if(status==1){//not in this state
            textview_flexion_title.setBackgroundColor(getResources().getColor(R.color.primary_color));
            textview_extension_title.setBackgroundColor(getResources().getColor(R.color.inactive_color));
            status = 0;
        }
        else{
            if(done_step< fastTest) return;
            textview_flexion_title.setBackgroundColor(getResources().getColor(R.color.inactive_color));
            textview_extension_title.setBackgroundColor(getResources().getColor(R.color.primary_color));
            status = 1;
        }
    }
    private View.OnClickListener section_listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            changeButton();
            change_btns_with_hasdone();
            if(status==1 && done_step>12)
                btns[done_step%12].setImageResource(getCorrectResource(done_step,2));
            else if(status ==0 && done_step<12){
                btns[done_step].setImageResource(getCorrectResource(done_step,2));
            }
        }
    };
    private void change_btns_with_hasdone(){
        if(mSkipFast.isChecked()){
            if(done_step==6){
                done_step+=3;
            }
        }
        for( int i = 0; i < 12; i++){
            if(has_done[i+12*status]) {
                btns[i].setImageResource(getCorrectResource(i, 1));
                btns[i].setEnabled(true);
            }
            else {
                btns[i].setImageResource(getCorrectResource(i, 0));
                btns[i].setEnabled(false);
            }
        }

        btns[done_step%12].setEnabled(true);
    }
}


