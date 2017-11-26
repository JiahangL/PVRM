package com.example.jiahang.pvrm;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//importcom.example.jiahang.pvrm.connect.AcceptThread;
import com.example.jiahang.pvrm.connect.ConnectThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_ARM_ANGLE;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_FATIGUED;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_FEELING_PAIN;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_HAVE_INFECTION;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_HIGH_STRESS;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_INJURED_ARM;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_MISSED_MEDICATION;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_PRESENCE_OF_CLONUS;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_PRESENCE_OF_TREMOR;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_SKIP_FAST;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_TAKING_NEW_MEDICATION;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_TESTED_ARM;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_WRIST_ANGLE;
import static com.example.jiahang.pvrm.Shared.ACTIVITY_TRACKER;
import static com.example.jiahang.pvrm.Shared.SHARED_SKIP_FAST;
import static com.example.jiahang.pvrm.Shared.TO_UNFINISH;

import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_SUBJECT_ID;
import static com.example.jiahang.pvrm.ClinicianDataActivity.EXTRA_PATIENT_TYPE;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_HEIGHT_FT;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_HEIGHT_IN;
import static com.example.jiahang.pvrm.InstructionActivity.EXTRA_FOREARM_LENGTH;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_WEIGHT;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_DATE_OF_BIRTH;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_TEST_DATE;
import static com.example.jiahang.pvrm.PatientQuestionnaireActivity.EXTRA_PATIENT_GENDER;

/**
 * Created by Rex on 2015/5/27.
 */
public class MainActivity extends AppCompatActivity implements MyDataHandler.PopupFragment {

    public static final String EXTRA_POSITION = "com.example.jiahang.pvrm.position";
    public static final String EXTRA_VELOCITY = "com.example.jiahang.pvrm.velocity";
    public static final String EXTRA_RESISTANT = "com.example.jiahang.pvrm.resistant";
    public static final String EXTRA_BICOUNT = "com.example.jiahang.pvrm.bicount";
    public static final String EXTRA_TRICOUNT = "com.example.jiahang.pvrm.tricount";
    public static final String EXTRA_CALLER = "com.example.jiahang.pvrm.caller";
    public static final String EXTRA_UNFINISH = "com.example.jiahang.pvrm.unfinish";
    private static final String DIALOG_FAIL = "fail";
    private static final String DIALOG_SUCCEED = "succeed";
    public static final int REQUEST_CODE = 0;
    private List<BluetoothDevice> mDeviceList = new ArrayList<>();
    private List<BluetoothDevice> mBondedDeviceList = new ArrayList<>();

    private BlueToothController mController = BlueToothController.get(this);
    private ListView mListView;
    private DeviceAdapter mAdapter;
    private Toast mToast;
    // private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private Handler mUIHandler;
    String subjectID = null;
    String categorySelected;
    String heightFeet;
    String heightInch;
    String forearmLength;
    String subjectWeight;
    String subjectDOB;
    String subjectTestDate;
    String subjectGender;

    private Toolbar myToolbar;
    private Button find_device_btn;
    private Button goback_btn;
    private Button connect_btn;
    private Button next_btn;
    private Button calibration_btn;

    private float maxPosition = 0;
    private float maxVelocity = 0;
    private float maxResistant = 0;
    BluetoothDevice device;
    MyFileWriter mMyFileWriter;
    int bi_sample_counter = 0;
    int tri_sample_counter = 0;
    private static final float THRESHOLD = 1.0f;
    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCountdownTimerAfterStart;
    File report_file;
    FileOutputStream outputStream;
    private Boolean unfinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initActionBar();
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        unfinished = i.getBooleanExtra(EXTRA_UNFINISH, false);
        forearmLength = i.getStringExtra(EXTRA_FOREARM_LENGTH);
        subjectID = i.getStringExtra(EXTRA_SUBJECT_ID);

        initUI();

        registerBluetoothReceiver();
        mController.turnOnBlueTooth(this, REQUEST_CODE);

        Shared.putString(getApplicationContext(), EXTRA_SUBJECT_ID, subjectID);
        if (!unfinished) {
            Shared.putInt(getApplicationContext(), ACTIVITY_TRACKER, 0);
            Shared.putBoolean(getApplicationContext(), TO_UNFINISH, true);
        }
        mMyFileWriter = MyFileWriter.get(subjectID, composeInitialContent(), this);
        mUIHandler = new MyDataHandler(MainActivity.this);
        mConnectThread = ConnectThread.get(device, mController.getAdapter(), mUIHandler);
        mCountDownTimer = new CountDownTimer(5 * 1000, 250) { //apple
            int secondsLeft = 0;

            @Override
            public void onTick(long ms) {

                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    calibration_btn.setText(secondsLeft + " second left before start");
                }
            }

            @Override
            public void onFinish() {
//                startService(stopRingtoneIntent);
                say("q");
                next_btn.setEnabled(true);
                mCountdownTimerAfterStart.start();
            }
        };
        mCountdownTimerAfterStart = new CountDownTimer(5 * 1000, 1000) {//apple
            int secondsLeft = 0;

            @Override
            public void onTick(long ms) {
                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    calibration_btn.setText(secondsLeft + " second left ");
                }
            }

            @Override
            public void onFinish() {
                say("s");
                Toast.makeText(MainActivity.this, "Finish recording", Toast.LENGTH_SHORT).show();
                calibration_btn.setText("Finish recording");
                next_btn.setEnabled(true);

            }
        };

    }


    private void registerBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        //start to discover devices
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //finish discovering devices
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //found the device
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //change the scan mode
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //bounded status change
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(mReceiver, filter);
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                setProgressBarIndeterminateVisibility(true);
                //initialize the list
                mDeviceList.clear();
                mAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //add the device that we found
                mDeviceList.add(device);
                mAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    setProgressBarIndeterminateVisibility(true);
                } else {
                    setProgressBarIndeterminateVisibility(false);
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (remoteDevice == null) {
                    showToast("no device");
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                if (status == BluetoothDevice.BOND_BONDED) {
                    showToast("Bonded " + remoteDevice.getName());
                    succeed();
                    calibration_btn.setEnabled(true);

                } else if (status == BluetoothDevice.BOND_BONDING) {
                    showToast("Bonding " + remoteDevice.getName());
                } else if (status == BluetoothDevice.BOND_NONE) {
                    showToast("Not bond " + remoteDevice.getName());
                    fail();
                }
            }
        }
    };


    private void initUI() {
        mListView = (ListView) findViewById(R.id.device_list);
        mAdapter = new DeviceAdapter(mDeviceList, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(bindedDeviceClick);
       // find_device_btn = (Button) findViewById(R.id.find_device);
        connect_btn = (Button) findViewById(R.id.bounded_device);
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBondedDeviceList = mController.getBondedDeviceList();
                //J mBondedDeviceList.addAll(mBluetoothAdapter.getBondedDevices());
                mAdapter.refresh(mBondedDeviceList);
                mListView.setOnItemClickListener(bindedDeviceClick);
            }
        });
        myToolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(myToolbar);
      /*  find_device_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.refresh(mDeviceList);
                mController.findDevice();
                mListView.setOnItemClickListener(bindDeviceClick);
            }
        });*/
        goback_btn = (Button) findViewById(R.id.btn_back);
        next_btn = (Button) findViewById(R.id.btn_forward);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PreRecordActivity.class);
                i.putExtra(EXTRA_SUBJECT_ID, subjectID);
                i.putExtra(EXTRA_FOREARM_LENGTH, forearmLength);
                i.putExtra(EXTRA_SKIP_FAST, getIntent().getStringExtra(EXTRA_SKIP_FAST));
                startActivity(i);
            }
        });

        calibration_btn = (Button) findViewById(R.id.btn_calibration_collect);
        if (unfinished && Shared.getInt(getApplicationContext(), ACTIVITY_TRACKER, 0) != 0) {
            calibration_btn.setVisibility(View.GONE);
        }
        calibration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyFileWriter.writeData("\ncalibrate_imu/load cell\n");
                calibration_btn.setText("Calibrating");
                mCountDownTimer.start();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnectThread != null) {
            //  mConnectThread.cancel();
            mConnectThread = null;
        }
        //start_btn_pressed = false;
        unregisterReceiver(mReceiver);
    }


    private void showToast(String text) {

        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.enable_visiblity) {
            //mController.enableVisibly(this);
            mMyFileWriter.writeData("hello\n");
        }
        if (id == R.id.find_device) {
            //look for device
            mAdapter.refresh(mDeviceList);
            mController.findDevice();
            mListView.setOnItemClickListener(bindDeviceClick);
        }
        if (id == R.id.bonded_device) {
            //look for the bounded device
            mBondedDeviceList = mController.getBondedDeviceList();
            //J mBondedDeviceList.addAll(mBluetoothAdapter.getBondedDevices());
            mAdapter.refresh(mBondedDeviceList);
            mListView.setOnItemClickListener(bindedDeviceClick);
        } else if (id == R.id.disconnect) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        } else if (id == R.id.say_hi_to_test) {
            say("*");
        } else if (id == R.id.say_hi_to_real) {
            say("q");
        }

        return super.onOptionsItemSelected(item);
    }

    private void say(String word) {

        if (mConnectThread != null) {
            try {
                mConnectThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Main Activity", "mConnect Thread is null");
        }
    }

    private AdapterView.OnItemClickListener bindDeviceClick = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = mDeviceList.get(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                device.createBond();
            }
        }
    };

    private AdapterView.OnItemClickListener bindedDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            device = mBondedDeviceList.get(i);
            connect_btn.setText("Connecting...");
            connect_btn.setBackgroundColor(getResources().getColor(R.color.inactive_color));
            Log.e("OnItemClick", i + " position is bounded");
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
            // TODO:  might have problem with connect thread
            mConnectThread = ConnectThread.get(device, mController.getAdapter(), mUIHandler);
                try {
                    mConnectThread.start();
                }catch (Exception w){
                    Log.e("Main connect thread", w.toString());
                    mConnectThread.cancel();
                }
        }
    };


    private void initActionBar() {

        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        setProgressBarIndeterminate(true);
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void succeed() {
        connect_btn.setText("Connect");
        connect_btn.setBackgroundColor(getResources().getColor(R.color.primary_color));
        Bundle bundle = new Bundle();
        String tmp = " device ";
        if (device != null) {
            tmp = device.getName();
        }
        bundle.putString(PositiveResultFragment.ARG_RESULT, "Connection to " + tmp + " Succeed");
        if (unfinished) {
            int step = Shared.getInt(getApplicationContext(), ACTIVITY_TRACKER, 1);
            Log.e("The step is ", "" + step);
            Intent i;
            switch (step) {
                case 1:
                    i = new Intent(MainActivity.this, PreRecordActivity.class);
                    break;
                case 2:
                    i = new Intent(MainActivity.this, DataRecordActivity.class);
                    break;
                default:
                    i = new Intent(MainActivity.this, PreRecordActivity.class);

            }
            i.putExtra(EXTRA_SUBJECT_ID, subjectID);
            i.putExtra(EXTRA_FOREARM_LENGTH, forearmLength);

            i.putExtra(EXTRA_SKIP_FAST, Shared.getString(getApplicationContext(), SHARED_SKIP_FAST));
            startActivity(i);
            return;
        }
        PositiveResultFragment dialog = new PositiveResultFragment();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), DIALOG_SUCCEED);
        calibration_btn.setEnabled(true);
    }

    @Override
    public void fail() {
        Bundle bundle = new Bundle();
        bundle.putString(NegativeResultFragment.ARG_RESULT, "Connection Fail");
        NegativeResultFragment dialog = new NegativeResultFragment();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), DIALOG_FAIL);
        connect_btn.setBackgroundColor(getResources().getColor(R.color.primary_color));
        connect_btn.setText("Connect");
        mConnectThread.cancel();
    }


    private String composeInitialContent() {
        Intent i = getIntent();
        if (unfinished)
            return "";
        subjectID = i.getStringExtra(EXTRA_SUBJECT_ID);
        categorySelected = i.getStringExtra(EXTRA_PATIENT_TYPE);
        heightFeet = i.getStringExtra(EXTRA_HEIGHT_FT);
        heightInch = i.getStringExtra(EXTRA_HEIGHT_IN);
        forearmLength = i.getStringExtra(EXTRA_FOREARM_LENGTH);
        subjectWeight = i.getStringExtra(EXTRA_WEIGHT);
        subjectDOB = i.getStringExtra(EXTRA_DATE_OF_BIRTH);
        subjectTestDate = i.getStringExtra(EXTRA_TEST_DATE);
        subjectGender = i.getStringExtra(EXTRA_PATIENT_GENDER);
        String sharing = "";
       // String sharing = "Patient ID: " + subjectID + "\n Gender: " + subjectGender + "\n Date of Birth: " + subjectDOB + "\n + "\n Height: " + heightFeet +"ft. " +  heightInch + " in\nWeight: " + subjectWeight + "\n  + "\nTested Date: " + subjectTestDate + "\n";
        sharing = sharing+
                "\nSubject ID: "+i.getStringExtra( EXTRA_SUBJECT_ID )
                +"\nCategory: " + categorySelected
                +"\nForearm Length(inches): "+ forearmLength
        +"\nTestDate: "+i.getStringExtra( EXTRA_TEST_DATE )
        +"\nWeight(lbs): "+i.getStringExtra( EXTRA_WEIGHT )
        +"\nExtra tested arm: "+i.getStringExtra( EXTRA_TESTED_ARM )
                +"\nGender: "+i.getStringExtra( EXTRA_PATIENT_GENDER )
                +"\nWrist Angel(deg): "+i.getStringExtra( EXTRA_WRIST_ANGLE )
                +"\nDOB: "+i.getStringExtra( EXTRA_DATE_OF_BIRTH )
                +"\nHeight ft: "+i.getStringExtra( EXTRA_HEIGHT_FT )
                +"\nHeight inch: "+i.getStringExtra( EXTRA_HEIGHT_IN )
                +"\nArm Angle(deg): "+i.getStringExtra( EXTRA_ARM_ANGLE )
                +"\nSkip fast: "+i.getStringExtra(EXTRA_SKIP_FAST)
                +"\nPresence of Clonus: " +i.getStringExtra(EXTRA_PRESENCE_OF_CLONUS)
                +"\nPresence of Tremor: "+i.getStringExtra( EXTRA_PRESENCE_OF_TREMOR )
                +"\nHigh Stress: "+i.getStringExtra( EXTRA_HIGH_STRESS )
                +"\nFatigued: "+i.getStringExtra( EXTRA_FATIGUED )
                +"\nHave Infection: "+i.getStringExtra( EXTRA_HAVE_INFECTION )
                +"\nMissed Medication: "+i.getStringExtra( EXTRA_MISSED_MEDICATION )
                +"\nTaking New Medication: "+i.getStringExtra( EXTRA_TAKING_NEW_MEDICATION )
                +"\nInjured Arm: "+i.getStringExtra( EXTRA_INJURED_ARM )
                +"\nFeeling Pain: "+i.getStringExtra( EXTRA_FEELING_PAIN )
               +"\n";
        return sharing;
    }


}