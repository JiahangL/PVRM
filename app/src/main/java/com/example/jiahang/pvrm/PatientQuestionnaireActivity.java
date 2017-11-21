package com.example.jiahang.pvrm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.jiahang.pvrm.ClinicianDataActivity.EXTRA_CLINICIAN_NAME;
//import static com.example.jiahang.pvrm.ClinicianDataActivity.EXTRA_PATIENT_GENDER;

public class PatientQuestionnaireActivity extends AppCompatActivity {

    public static final String EXTRA_TEST_DATE = "com.example.jiahang.pvrm.test_date";
    public static final String EXTRA_WEIGHT = "com.example.jiahang.pvrm.weight";
    public static final String EXTRA_TESTED_ARM = "com.example.jiahang.pvrm.tested_arm";
    public static final String EXTRA_PATIENT_GENDER = "com.example.jiahang.pvrm.patient_gender";
    public static final String EXTRA_WRIST_ANGLE = "com.example.jiahang.pvrm.wrist_angle";
    public static final String EXTRA_DATE_OF_BIRTH = "com.example.jiahang.pvrm.date_of_birth";
    public static final String EXTRA_HEIGHT_FT = "com.example.jiahang.pvrm.height_ft";
    public static final String EXTRA_HEIGHT_IN = "com.example.jiahang.pvrm.height_in";
    public static final String EXTRA_ARM_ANGLE = "com.example.jiahang.pvrm.arm_angle";

    public static final String EXTRA_SUBJECT_ID = "com.example.jiahang.pvrm.subject_id";
    public static final String EXTRA_PRESENCE_OF_TREMOR = "com.example.jiahang.pvrm.presence_of_tremor";
    public static final String EXTRA_HIGH_STRESS = "com.example.jiahang.pvrm.high_stress";
    public static final String EXTRA_FATIGUED = "com.example.jiahang.pvrm.fatigued";
    public static final String EXTRA_HAVE_INFECTION = "com.example.jiahang.pvrm.have_infection";
    public static final String EXTRA_MISSED_MEDICATION = "com.example.jiahang.pvrm.missed_medication";
    public static final String EXTRA_TAKING_NEW_MEDICATION = "com.example.jiahang.pvrm.taking_new_medication";
    public static final String EXTRA_INJURED_ARM = "com.example.jiahang.pvrm.injured_arm";
    public static final String EXTRA_FEELING_PAIN = "com.example.jiahang.pvrm.feeling_pain";

    ArrayList<View> unfilledViews = new ArrayList<>();
    ArrayList<View> filledViews = new ArrayList<>();

    Button button_nextbtn;

    /**Top Left elements **/
    //TextView textView_gender_title;
    //Spinner spinner_gender;
    TextView textView_weight_title;
    EditText editText_weight;
    TextView textView_weight_label;
    TextView textView_tested_arm_title;
    RadioGroup radioGroup_arm;
    TextView textView_gender;
    RadioGroup radioGroup_gender;
    TextView textView_wrist_angle_title;
    EditText editText_wrist_angle;
    TextView textView_wrist_angle_label;
    String whichArm;
    String gender;

    /**Top Right elements **/
    TextView textView_date_of_birth_title;
    DatePicker datepicker_date_of_birth;
    TextView textView_height_title;
    EditText editText_height_ft;
    TextView textView_height_ft_label;
    EditText editText_height_in;
    TextView textView_height_in_label;
    TextView textView_arm_angle_title;
    EditText editText_arm_angle;
    TextView textView_arm_angle_label;

    /**Bottom elements**/
    TextView textView_fast_extension_flexion_title;
    RadioGroup radioGroup_fast_extension_flexion;
    EditText editText_fast_extension_flexion;
    TextView textView_presence_of_clonus_title;
    RadioGroup radioGroup_presence_of_clonus;
    EditText editText_presence_of_clonus;
    TextView textView_presense_of_tremor_title;
    RadioGroup radioGroup_presence_of_tremor;
    EditText editText_presence_of_tremor;
    TextView textView_high_stress_title;
    RadioGroup radioGroup_high_stress;
    EditText editText_high_stress;
    TextView textView_fatigued_title;
    RadioGroup radioGroup_fatigued;
    EditText editText_fatigued;
    TextView textView_have_infection_title;
    EditText editText_have_infection;
    RadioGroup radioGroup_have_infection;
    TextView textView_missed_medication_title;
    EditText editText_missed_medication;
    RadioGroup radioGroup_missed_medication;
    TextView textView_taking_new_medication_title;
    EditText editText_taking_new_medication;
    RadioGroup radioGroup_taking_new_medication;
    TextView textView_injured_arm_title;
    RadioGroup radioGroup_injured_arm;
    EditText editText_injured_arm;
    TextView textView_feeling_pain_title;
    RadioGroup radioGroup_feeling_pain;
    EditText editText_feeling_pain;
    TextView textView_taking_steroids;
    RadioGroup radioGroup_taking_steroids;
    EditText editText_taking_steroids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_questionnaire);

        button_nextbtn = (Button)findViewById(R.id.btn_forward);
        button_nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent();
                if(checkRequiredFields())
                    startActivity(intent);
                highlightRequired();
            }
        });

        initUITop();
        initUIBottom();

    }

    public void initUITop() {
        /*
        textView_gender_title = (TextView)findViewById(R.id.ID_gender_title);
        spinner_gender = (Spinner)findViewById(R.id.ID_spinner_gender);
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: store selected gender to local variable
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_gender, R.layout.support_simple_spinner_dropdown_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        */
        textView_weight_title = (TextView)findViewById(R.id.ID_weight_title);
        editText_weight = (EditText)findViewById(R.id.ID_edittext_weight);
        textView_weight_label = (TextView)findViewById(R.id.ID_label_weight);

        textView_tested_arm_title = (TextView)findViewById(R.id.ID_tested_arm_title);
        radioGroup_arm = (RadioGroup)findViewById(R.id.ID_radiogroup_arm);
        radioGroup_arm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_leftarm) {
                    //TODO: save the selected arm to local variable
                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_leftarm);
                    whichArm = temp.getText().toString();
                } else if (checkedId == R.id.ID_radiobutton_rightarm) {
                    //TODO: save the selected arm to local variable
                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_leftarm);
                    whichArm = temp.getText().toString();
                }
            }
        });

        textView_gender = (TextView)findViewById(R.id.ID_textview_gender_title);
        radioGroup_gender = (RadioGroup)findViewById(R.id.ID_radiogroup_gender);
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_male) {
                    //TODO: save the selected arm to local variable
                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_male);
                    gender = temp.getText().toString();
                    Toast.makeText(getApplicationContext(),gender,Toast.LENGTH_LONG).show();
                } else if (checkedId == R.id.ID_radiobutton_female) {
                    //TODO: save the selected arm to local variable
                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_female);
                    gender = temp.getText().toString();
                    Toast.makeText(getApplicationContext(),gender,Toast.LENGTH_LONG).show();
                }
            }
        });

        textView_wrist_angle_title = (TextView)findViewById(R.id.ID_wrist_angle_title);
        editText_wrist_angle = (EditText)findViewById(R.id.ID_edittext_wristangle);
        textView_wrist_angle_label = (TextView)findViewById(R.id.ID_label_wristangle);

        textView_date_of_birth_title = (TextView)findViewById(R.id.ID_date_of_birth_title);
        Calendar calendar = Calendar.getInstance();
        datepicker_date_of_birth = (DatePicker)findViewById(R.id.ID_datepicker_date_of_birth);
        datepicker_date_of_birth.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH), null);

        textView_height_title = (TextView)findViewById(R.id.ID_height_title);
        editText_height_ft = (EditText)findViewById(R.id.ID_editText_heightft);
        textView_height_ft_label = (TextView)findViewById(R.id.ID_label_heightft);
        editText_height_in = (EditText)findViewById(R.id.ID_edittext_heightin);
        textView_height_in_label = (TextView)findViewById(R.id.ID_label_heightin);

        textView_arm_angle_title = (TextView)findViewById(R.id.ID_arm_angle_title);
        editText_arm_angle = (EditText)findViewById(R.id.ID_edittext_armangle);
        textView_arm_angle_label = (TextView)findViewById(R.id.ID_label_armangle);
    }

    public void initUIBottom() {
        editText_fast_extension_flexion = (EditText)findViewById(R.id.ID_edittext_fast_extension_flexion);
        textView_fast_extension_flexion_title = (TextView)findViewById(R.id.ID_fast_extension_flexion_title);
        radioGroup_fast_extension_flexion = (RadioGroup)findViewById(R.id.ID_radiogroup_fast_extension_flexion);
        radioGroup_fast_extension_flexion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.ID_radiobutton_fast_extension_flexion_yes) {
                    editText_fast_extension_flexion.setText("");
                    editText_fast_extension_flexion.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if(checkedId == R.id.ID_radiobutton_fast_extension_flexion_no) {
                    editText_fast_extension_flexion.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                }
            }
        });

        editText_presence_of_clonus = (EditText)findViewById(R.id.ID_edittext_presence_of_clonus);
        textView_presence_of_clonus_title = (TextView)findViewById(R.id.ID_presence_of_clonus_title);
        radioGroup_presence_of_clonus = (RadioGroup)findViewById(R.id.ID_radiogroup_presence_of_clonus);
        radioGroup_presence_of_clonus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_presence_of_clonus_yes) {
                    editText_presence_of_clonus.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_presence_of_clonus_no) {
                    editText_presence_of_clonus.setText("");
                    editText_presence_of_clonus.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                }
            }
        });

        editText_presence_of_tremor = (EditText)findViewById(R.id.ID_edittext_presence_of_tremor);
        textView_presense_of_tremor_title = (TextView)findViewById(R.id.ID_presence_of_tremor_title);
        radioGroup_presence_of_tremor = (RadioGroup)findViewById(R.id.ID_radiogroup_presence_of_tremor);
        radioGroup_presence_of_tremor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_presence_of_tremor_yes) {
                    editText_presence_of_tremor.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_presence_of_tremor_no) {
                    editText_presence_of_tremor.setText("");
                    editText_presence_of_tremor.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                }
            }
        });

        editText_high_stress = (EditText)findViewById(R.id.ID_edittext_high_stress);
        textView_high_stress_title = (TextView)findViewById(R.id.ID_high_stress_title);
        radioGroup_high_stress = (RadioGroup)findViewById(R.id.ID_radiogroup_high_stress);
        radioGroup_high_stress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_high_stress_yes) {
                    editText_high_stress.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_high_stress_no) {
                    editText_high_stress.setText("");
                    editText_high_stress.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_high_stress_idk) {
                    editText_high_stress.setText("");
                    editText_high_stress.setVisibility(View.GONE);
                }
            }
        });

        editText_fatigued= (EditText)findViewById(R.id.ID_edittext_fatigued);
        textView_fatigued_title = (TextView)findViewById(R.id.ID_fatigued_title);
        radioGroup_fatigued = (RadioGroup)findViewById(R.id.ID_radiogroup_fatigued);
        radioGroup_fatigued.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_fatigued_yes) {
                    editText_fatigued.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_fatigued_no) {
                    editText_fatigued.setText("");
                    editText_fatigued.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_fatigued_idk) {
                    editText_fatigued.setText("");
                    editText_fatigued.setVisibility(View.GONE);
                }
            }
        });

        editText_have_infection= (EditText)findViewById(R.id.ID_edittext_have_infections);
        textView_have_infection_title = (TextView)findViewById(R.id.ID_have_infections_title);
        radioGroup_have_infection = (RadioGroup)findViewById(R.id.ID_radiogroup_have_infections);
        radioGroup_have_infection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_have_infections_yes) {
                    editText_have_infection.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_have_infections_no) {
                    editText_have_infection.setText("");
                    editText_have_infection.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_have_infections_idk) {
                    editText_have_infection.setText("");
                    editText_have_infection.setVisibility(View.GONE);
                }
            }
        });

        editText_missed_medication= (EditText)findViewById(R.id.ID_edittext_missed_medications);
        textView_missed_medication_title = (TextView)findViewById(R.id.ID_missed_medications_title);
        radioGroup_missed_medication= (RadioGroup)findViewById(R.id.ID_radiogroup_missed_medications);
        radioGroup_missed_medication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_missed_medications_yes) {
                    editText_missed_medication.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_missed_medications_no) {
                    editText_missed_medication.setText("");
                    editText_missed_medication.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_missed_medications_idk) {
                    editText_missed_medication.setText("");
                    editText_missed_medication.setVisibility(View.GONE);
                }
            }
        });

        editText_taking_new_medication = (EditText)findViewById(R.id.ID_edittext_taking_new_medications);
        textView_taking_new_medication_title = (TextView)findViewById(R.id.ID_taking_new_medications_title);
        radioGroup_taking_new_medication = (RadioGroup)findViewById(R.id.ID_radiogroup_taking_new_medications);
        radioGroup_taking_new_medication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_taking_new_medications_yes) {
                    editText_taking_new_medication.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_taking_new_medications_no) {
                    editText_taking_new_medication.setText("");
                    editText_taking_new_medication.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_taking_new_medications_idk) {
                    editText_taking_new_medication.setText("");
                    editText_taking_new_medication.setVisibility(View.GONE);
                }
            }
        });

        editText_injured_arm= (EditText)findViewById(R.id.ID_edittext_injured_arm);
        textView_injured_arm_title = (TextView)findViewById(R.id.ID_injured_arm_title);
        radioGroup_injured_arm = (RadioGroup)findViewById(R.id.ID_radiogroup_injured_arm);
        radioGroup_injured_arm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_injured_arm_yes) {
                    editText_injured_arm.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_injured_arm_no) {
                    editText_injured_arm.setText("");
                    editText_injured_arm.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_injured_arm_idk) {
                    editText_injured_arm.setText("");
                    editText_injured_arm.setVisibility(View.GONE);
                }
            }
        });

        editText_feeling_pain= (EditText)findViewById(R.id.ID_edittext_feeling_pain);
        textView_feeling_pain_title = (TextView)findViewById(R.id.ID_feeling_pain_title);
        radioGroup_feeling_pain = (RadioGroup)findViewById(R.id.ID_radiogroup_feeling_pain);
        radioGroup_feeling_pain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_feeling_pain_yes) {
                    editText_feeling_pain.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_feeling_pain_no) {
                    editText_feeling_pain.setText("");
                    editText_feeling_pain.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_feeling_pain_idk) {
                    editText_feeling_pain.setText("");
                    editText_feeling_pain.setVisibility(View.GONE);
                }
            }
        });

        editText_taking_steroids = (EditText)findViewById(R.id.ID_edittext_taking_steroids);
        textView_taking_steroids = (TextView)findViewById(R.id.ID_taking_steroids_title);
        radioGroup_taking_steroids = (RadioGroup)findViewById(R.id.ID_radiogroup_taking_steroids);
        radioGroup_taking_steroids.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_taking_steroids_yes) {
                    editText_taking_steroids.setVisibility(View.VISIBLE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_taking_steroids_no) {
                    editText_taking_steroids.setText("");
                    editText_taking_steroids.setVisibility(View.GONE);
                    //TODO: save the selected arm to local variable
                } else if (checkedId == R.id.ID_radiobutton_taking_steroids_idk) {
                    editText_taking_steroids.setText("");
                    editText_taking_steroids.setVisibility(View.GONE);
                }
            }
        });

    }

    public Intent createIntent() {
        Intent lastIntent = getIntent();

        Intent intent = new Intent(PatientQuestionnaireActivity.this, InstructionActivity.class);
        intent.putExtras(lastIntent);

        intent.putExtra(EXTRA_TEST_DATE, Calendar.getInstance().getTime().toString());

        String clinicianName = lastIntent.getStringExtra(EXTRA_CLINICIAN_NAME);
        intent.putExtra(EXTRA_WEIGHT, editText_weight.getText().toString());
        intent.putExtra(EXTRA_PATIENT_GENDER, gender);
        intent.putExtra(EXTRA_TESTED_ARM, whichArm);
        intent.putExtra(EXTRA_WRIST_ANGLE, editText_wrist_angle.getText().toString());

        String dob = Integer.toString(datepicker_date_of_birth.getMonth()) + "-" +
                Integer.toString(datepicker_date_of_birth.getDayOfMonth()) + "-" +
                Integer.toString(datepicker_date_of_birth.getYear());
        intent.putExtra(EXTRA_DATE_OF_BIRTH, dob);
        intent.putExtra(EXTRA_SUBJECT_ID, clinicianName+dob);
        intent.putExtra(EXTRA_HEIGHT_FT, editText_height_ft.getText().toString());
        intent.putExtra(EXTRA_HEIGHT_IN, editText_height_in.getText().toString());
        intent.putExtra(EXTRA_ARM_ANGLE, editText_arm_angle.getText().toString());

        intent.putExtra(EXTRA_PRESENCE_OF_TREMOR, editText_presence_of_tremor.getText().toString());
        intent.putExtra(EXTRA_HIGH_STRESS, editText_high_stress.getText().toString());
        intent.putExtra(EXTRA_FATIGUED, editText_fatigued.getText().toString());
        intent.putExtra(EXTRA_HAVE_INFECTION, editText_have_infection.getText().toString());
        intent.putExtra(EXTRA_MISSED_MEDICATION, editText_missed_medication.getText().toString());
        intent.putExtra(EXTRA_TAKING_NEW_MEDICATION, editText_taking_new_medication.getText().toString());
        intent.putExtra(EXTRA_INJURED_ARM, editText_injured_arm.getText().toString());
        intent.putExtra(EXTRA_FEELING_PAIN, editText_feeling_pain.getText().toString());
        return intent;
    }

    public boolean checkRequiredFields() {
        boolean allfieldsfilled = true;
        //top ui
        if(editText_weight.getText().toString().equals("")) {
            unfilledViews.add(editText_weight);
            allfieldsfilled = false;
        } else {
            filledViews.add(editText_weight);
        }
        if(radioGroup_arm.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_arm);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_arm);
        }
        if(editText_wrist_angle.getText().toString().equals("")) {
            unfilledViews.add(editText_wrist_angle);
            allfieldsfilled = false;
        } else {
            filledViews.add(editText_wrist_angle);
        }
        if(editText_height_ft.getText().toString().equals("")) {
            unfilledViews.add(editText_height_ft);
            allfieldsfilled = false;
        } else {
            filledViews.add(editText_height_ft);
        }
        if(editText_height_in.getText().toString().equals("")) {
            unfilledViews.add(editText_height_in);
            allfieldsfilled = false;
        } else {
            filledViews.add(editText_height_in);
        }
        if(radioGroup_gender.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_gender);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_gender);
        }
        if(editText_arm_angle.getText().toString().equals("")) {
            unfilledViews.add(editText_arm_angle);
            allfieldsfilled = false;
        } else {
            filledViews.add(editText_arm_angle);
        }
        //bot ui
        if(radioGroup_fast_extension_flexion.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_fast_extension_flexion);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_fast_extension_flexion);
        }
        if(radioGroup_presence_of_clonus.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_presence_of_clonus);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_presence_of_clonus);
        }
        if(radioGroup_presence_of_tremor.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_presence_of_tremor);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_presence_of_tremor);
        }
        if(radioGroup_high_stress.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_high_stress);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_high_stress);
        }
        if(radioGroup_fatigued.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_fatigued);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_fatigued);
        }
        if(radioGroup_have_infection.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_have_infection);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_have_infection);
        }
        if(radioGroup_missed_medication.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_missed_medication);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_missed_medication);
        }
        if(radioGroup_taking_new_medication.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_taking_new_medication);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_taking_new_medication);
        }
        if(radioGroup_injured_arm.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_injured_arm);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_injured_arm);
        }
        if(radioGroup_feeling_pain.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_feeling_pain);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_feeling_pain);
        }
        if(radioGroup_taking_steroids.getCheckedRadioButtonId() == -1) {
            unfilledViews.add(radioGroup_taking_steroids);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_taking_steroids);
        }
        return allfieldsfilled;
    }

    public void highlightRequired() {
        for(View v : unfilledViews){
            v.setBackgroundColor(getResources().getColor(R.color.requiredField));
        }
        for(View v : filledViews){
            if(findViewById(v.getId()) instanceof EditText)
                v.setBackgroundResource(R.drawable.abc_edit_text_material);
            else if (findViewById(v.getId()) instanceof RadioGroup)
                v.setBackgroundColor(Color.TRANSPARENT);
        }
        unfilledViews.clear();
        filledViews.clear();
    }
}
