package com.example.jiahang.pvrm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.jiahang.pvrm.MainActivity.EXTRA_BICOUNT;
import static com.example.jiahang.pvrm.ClinicianDataActivity.EXTRA_PATIENT_TYPE;
import static com.example.jiahang.pvrm.MainActivity.EXTRA_POSITION;
import static com.example.jiahang.pvrm.MainActivity.EXTRA_RESISTANT;
import static com.example.jiahang.pvrm.MainActivity.EXTRA_TRICOUNT;
import static com.example.jiahang.pvrm.MainActivity.EXTRA_VELOCITY;

/**
 * Created by zifeifeng on 10/10/17.
 */

public class SummaryActivity extends Activity {
    private TextView motion_range_tv;
    private TextView max_v_tv;
    private TextView max_r_tv;
    private TextView bimuscle_status_tv;
    private TextView trimuscle_status_tv;
    private TextView extra_tv;
    private Button shared_btn;
    private Button reset_btn;
    private Button next_btn;
    private String[] attr;
    private String extraInfo;
    private float motion_range;
    private float maxr;
    private float maxv;
    String subjectID;
    String categorySelected;
    String heightFeet;
    String heightInch;
    String forearmLength;
    String subjectWeight;
    String subjectDOB;
    String subjectTestDate;
    String subjectGender;
    String bi_status;
    String tri_status;
    int bi_count;
    int tri_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        initUI();
        Intent intent = getIntent();
        if(intent != null){
            getIntentData(intent);
        }
        updateUI();
    }

    private void getIntentData(Intent intent){
        motion_range = intent.getFloatExtra(EXTRA_POSITION, 0);
        maxr = intent.getFloatExtra(EXTRA_RESISTANT, 0);
        maxv = intent.getFloatExtra(EXTRA_VELOCITY, 0);
//        subjectID = intent.getStringExtra(EXTRA_SUBJECT_ID);
        categorySelected = intent.getStringExtra(EXTRA_PATIENT_TYPE);
//        heightFeet  = intent.getStringExtra(EXTRA_HEIGHT_FEET);
//        heightInch = intent.getStringExtra(EXTRA_HEIGHT_INCH);
//        forearmLength = intent.getStringExtra(EXTRA_FOREARM_LENGTH);
//        subjectWeight = intent.getStringExtra(EXTRA_SUBJECT_WEIGHT);
//        subjectDOB =  intent.getStringExtra(EXTRA_SUBJECT_DOB);
//        subjectTestDate=intent.getStringExtra(EXTRA_SUBJECT_TEST_DATE);
        bi_count = intent.getIntExtra(EXTRA_BICOUNT, 0);
        tri_count = intent.getIntExtra(EXTRA_TRICOUNT, 0);
    }

    private void updateUI(){
        motion_range_tv.setText(""+motion_range);
        max_r_tv.setText(""+maxr);
        String s = String.format("%.2f", maxv);
        max_v_tv.setText(s);
        bi_status= bi_count >=100? "Active":"Passive";
        tri_status = tri_count>=100? "Active":"Passive";
        int mas;
        bimuscle_status_tv.setText(bi_status);
        trimuscle_status_tv.setText(tri_status);
        if(categorySelected.equals(getResources().getStringArray(R.array.patient_category)[1])) {//spasticity
            if(maxr < 7){
                mas = 1;
            }
            else if(maxr < 14){
                mas = 2;
            }
            else if(maxr < 21){
                mas = 3;
            }
            else if(maxr < 28){
                mas = 4;
            }
            else{
                mas = 5;
            }
            extraInfo =  "The patient's category is " +categorySelected+ " \nMAS: "+mas + "\nMTS: "+mas;
            extra_tv.setVisibility(View.VISIBLE);
            extra_tv.setText(extraInfo);
        }
        else if(categorySelected.equals(getResources().getStringArray(R.array.patient_category)[2])){
            if(maxr < 9){
                mas = 1;
            }
            else if(maxr < 18){
                mas = 2;
            }
            else if(maxr < 27){
                mas = 3;
            }
            else{
                mas = 4;
            }
            extraInfo =  "The patient's category is " +categorySelected+ " \nUPDRS: "+mas;
            extra_tv.setVisibility(View.VISIBLE);
            extra_tv.setText(extraInfo);
        }
        else{
            extraInfo = "Healthy!";
            extra_tv.setVisibility(View.GONE);
        }
    }
    private void initUI(){
        motion_range_tv = (TextView) findViewById(R.id.datasummary_rm_tv);
        max_r_tv = (TextView) findViewById(R.id.datasummary_maxv_tv);
        max_v_tv = (TextView) findViewById(R.id.datasummary_maxr_tv);
        bimuscle_status_tv = (TextView) findViewById(R.id.datasummary_bistatus_tv);
        trimuscle_status_tv = (TextView) findViewById(R.id.datasummary_tristatus_tv);
        extra_tv =(TextView) findViewById(R.id.datasummary_extrainfo_tv);
        shared_btn = (Button) findViewById(R.id.datasummary_share_btn);
        reset_btn=(Button) findViewById(R.id.datasummary_reset_btn);
        next_btn=(Button) findViewById(R.id.datasummary_newsub_btn);
        shared_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFileWriter.get(null,null,null).closeFileWriter();
                shareByEmail();
                Shared.putBoolean(getApplication(), Shared.HAS_BEEN_SAHRED, true);
            }
        });
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SummaryActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryActivity.this, ClinicianDataActivity.class);
                startActivity(intent);
            }
        });

    }
    //    <string name="shared_content">Patient ID: %1$s \n Gender: %2$s \n, Date of Birth: %3$s \n,
//    Category: %4$s, Height: %5$s, Weight: %6$s, Forearm Length: %7$s, Tested Date: %8$s,
//    Motion Range: %9$d\n, Max Velocity: %10$d\n, Max Resistance: %11$d \n, Biceps Status: %12$s,
//    Triceps Status: %13$s\n, Extra Information: %14$s</string>
    private String getSharedString(){
        //return getString(R.string.shared_content, )
        // String sharing = getString(R.string.shared_content, subjectID, subjectGender, subjectDOB, categorySelected,
        //         heightFeet+"feet "+heightInch, subjectWeight,forearmLength, subjectTestDate, ""+motion_range, maxv, ""+maxr, bi_status, tri_status, extraInfo);

        String sharing = "Patient ID: "+subjectID+"\nGender: "+ subjectGender+"\nDate of Birth: "+subjectDOB+"\nCategory: "
                + categorySelected + "\n Height: " + heightFeet + heightInch +" inch\nWeight: " + subjectWeight+", \nForearm Length: "
                +forearmLength +"\nTested Date: "+subjectTestDate +"\nMotion Range:  "+ motion_range + "\nMax Velocity:" + maxv +"\nMax Resistance: " + maxr+"\nBiceps Status: "
                + bi_status +"\nTriceps Status: " + tri_status+"\nExtra Information: " + extraInfo;
        return sharing;
    }

    private void shareByEmail(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822").putExtra(Intent.EXTRA_EMAIL, new String[]{"77bisko@gmail.com"}).putExtra(android.content.Intent.EXTRA_SUBJECT, "Patient Data");
        String targetFilePath = MyFileWriter.getFilepath();
        Log.e("share File", targetFilePath);
        Uri attachmentUri = Uri.parse(targetFilePath);
        emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
        startActivity(emailIntent);
    }
}