package com.example.jiahang.pvrm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ClinicianDataActivity extends AppCompatActivity {


    public static final String EXTRA_CLINICIAN_NAME = "com.example.jiahang.pvrm.clinician_name";
    public static final String EXTRA_PATIENT_TYPE = "com.example.jiahang.pvrm.updrs";

    ArrayList<View> unfilledViews = new ArrayList<>();
    ArrayList<View> filledViews = new ArrayList<>();

    String patientCategory;

    RadioGroup radioGroup_patient_type;

    TextView textView_mas_title;
    TextView textView_mts_title;
    TextView textView_updrs_title;
    TextView textView_controls_title;

    Spinner spinner_clinician;
    Spinner spinner_mas;
    Spinner spinner_mts;
    Spinner spinner_updrs;
    Spinner spinner_controls;

    Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinician_data);

        initSpinnersUI();
        initRadioGroup();
        if(!Shared.getBoolean(this, Shared.HAS_BEEN_SAHRED, true)){
            ShareLastFileFragment dialog = new ShareLastFileFragment();
            dialog.show(getSupportFragmentManager(), "share_last_file");
        }
        if(Shared.getBoolean(this, Shared.TO_UNFINISH, false)){
            UnfinishedPorgressFragment dialog = new UnfinishedPorgressFragment();
            dialog.show(getSupportFragmentManager(), "back_to_unfinished_process");
        }

        button_next = (Button)findViewById(R.id.ID_button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup_patient_type.getCheckedRadioButtonId();
                if(checkRequiredFields()) {
                    if (selectedID == R.id.ID_radiobutton_spasticity) {
                        String masLevel = "MAS: " + Integer.toString(spinner_mas.getSelectedItemPosition());
                        String mtsLevel = "MTS: " + Integer.toString(spinner_mts.getSelectedItemPosition());
                        Intent intent = createIntent("Spasticity,", masLevel, mtsLevel);
                        if (checkRequiredFields())
                            startActivity(intent);
                        highlightRequired();
                    }
                    if (selectedID == R.id.ID_radiobutton_rigidity) {
                        String updrsLevel = "UDPRS: " + Integer.toString(spinner_updrs.getSelectedItemPosition());
                        Intent intent = createIntent("Rigidity,", updrsLevel, "");
                        if (checkRequiredFields())
                            startActivity(intent);
                        highlightRequired();
                    }
                    if (selectedID == R.id.ID_radiobutton_controls) {
                        String healthyLevel = "Controls: " + Integer.toString(spinner_updrs.getSelectedItemPosition());
                        Intent intent = createIntent("Healthy,", healthyLevel, "");
                        if (checkRequiredFields())
                            startActivity(intent);
                        highlightRequired();
                    }
                } else {
                    highlightRequired();
                }
            }
        });
    }

    public void initRadioGroup() {
        radioGroup_patient_type = (RadioGroup)findViewById(R.id.ID_radiogroup_patient_type);
        radioGroup_patient_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.ID_radiobutton_spasticity) {
                    clearCategory();
                    textView_mas_title.setVisibility(View.VISIBLE);
                    spinner_mas.setVisibility(View.VISIBLE);
                    textView_mts_title.setVisibility(View.VISIBLE);
                    spinner_mts.setVisibility(View.VISIBLE);

                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_rigidity);
                    patientCategory = temp.getText().toString();
                } else if (checkedId == R.id.ID_radiobutton_rigidity) {
                    clearCategory();
                    textView_updrs_title.setVisibility(View.VISIBLE);
                    spinner_updrs.setVisibility(View.VISIBLE);

                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_spasticity);
                    patientCategory = temp.getText().toString();
                } else if (checkedId == R.id.ID_radiobutton_controls) {
                    clearCategory();
                    textView_controls_title.setVisibility(View.VISIBLE);
                    spinner_controls.setVisibility(View.VISIBLE);

                    RadioButton temp = (RadioButton)findViewById(R.id.ID_radiobutton_controls);
                    patientCategory = temp.getText().toString();
                }
            }
        });
    }

    public void initSpinnersUI() {
        /**Clinician Spinner**/
        spinner_clinician = (Spinner)findViewById(R.id.ID_spinner_clinician);
        spinner_clinician.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_clinician = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_clinician_names, R.layout.support_simple_spinner_dropdown_item);
        adapter_clinician.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_clinician.setAdapter(adapter_clinician);
        /********************/

        /**MAS Spinner**/
        textView_mas_title = (TextView)findViewById(R.id.ID_mas_title);
        textView_mas_title.setVisibility(View.GONE);
        spinner_mas = (Spinner)findViewById(R.id.ID_spinner_mas);
        spinner_mas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
                if(position == 0)
                    return;
                //Intent intent = createIntent("MAS", position);
                //startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_mas = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_mas_levels, R.layout.support_simple_spinner_dropdown_item);
        adapter_mas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mas.setAdapter(adapter_mas);
        spinner_mas.setVisibility(View.GONE);
        /********************/

        /**MTS Spinner**/
        textView_mts_title = (TextView)findViewById(R.id.ID_mts_title);
        textView_mts_title.setVisibility(View.GONE);
        spinner_mts = (Spinner)findViewById(R.id.ID_spinner_mts);
        spinner_mts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
                if(position == 0)
                    return;
                //Intent intent = createIntent("MTS", position);
                //startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_mts = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_mts_levels, R.layout.support_simple_spinner_dropdown_item);
        adapter_mts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mts.setAdapter(adapter_mts);
        spinner_mts.setVisibility(View.GONE);
        /********************/

        /**UPPRS Spinner**/
        textView_updrs_title = (TextView)findViewById(R.id.ID_udprs_title);
        textView_updrs_title.setVisibility(View.GONE);
        spinner_updrs = (Spinner)findViewById(R.id.ID_spinner_updrs);
        spinner_updrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
                if(position == 0)
                    return;
                //Intent intent = createIntent("UDPRS", position);
                //startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_udprs = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_udprs_levels, R.layout.support_simple_spinner_dropdown_item);
        adapter_udprs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_updrs.setAdapter(adapter_udprs);
        spinner_updrs.setVisibility(View.GONE);
        /********************/

        /**Controls Spinner**/
        textView_controls_title = (TextView)findViewById(R.id.ID_controls_title);
        textView_controls_title.setVisibility(View.GONE);
        spinner_controls = (Spinner)findViewById(R.id.ID_spinner_controls);
        spinner_controls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_controls = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_gender, R.layout.support_simple_spinner_dropdown_item);
        adapter_udprs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_controls.setAdapter(adapter_controls);
        spinner_controls.setVisibility(View.GONE);
        /********************/
    }

    public void clearCategory() {
        spinner_mts.setSelection(0);
        spinner_mas.setSelection(0);
        spinner_updrs.setSelection(0);
        spinner_controls.setSelection(0);

        textView_mas_title.setVisibility(View.GONE);
        textView_mts_title.setVisibility(View.GONE);
        textView_updrs_title.setVisibility(View.GONE);
        textView_controls_title.setVisibility(View.GONE);

        spinner_mts.setVisibility(View.GONE);
        spinner_mas.setVisibility(View.GONE);
        spinner_updrs.setVisibility(View.GONE);
        spinner_controls.setVisibility(View.GONE);
    }

    public boolean checkRequiredFields() {
        boolean allfieldsfilled = true;
        if(spinner_clinician.getSelectedItemPosition() == 0) {
            unfilledViews.add(spinner_clinician);
            allfieldsfilled = false;
        } else {
            filledViews.add(spinner_clinician);
        }
        if(radioGroup_patient_type.getCheckedRadioButtonId() == -1){
            unfilledViews.add(radioGroup_patient_type);
            allfieldsfilled = false;
        } else {
            filledViews.add(radioGroup_patient_type);
            if (radioGroup_patient_type.getCheckedRadioButtonId() == R.id.ID_radiobutton_spasticity) {
                if(spinner_mts.getSelectedItemPosition() == 0) {
                    unfilledViews.add(spinner_mts);
                    allfieldsfilled = false;
                } else {
                    filledViews.add(spinner_mts);
                }
                if(spinner_mas.getSelectedItemPosition() == 0) {
                    unfilledViews.add(spinner_mas);
                    allfieldsfilled = false;
                } else {
                    filledViews.add(spinner_mas);
                }
            }
            else if (radioGroup_patient_type.getCheckedRadioButtonId() == R.id.ID_radiobutton_rigidity) {
                if(spinner_updrs.getSelectedItemPosition() == 0) {
                    unfilledViews.add(spinner_updrs);
                    allfieldsfilled = false;
                } else {
                    filledViews.add(spinner_updrs);
                }
            }
            else if (radioGroup_patient_type.getCheckedRadioButtonId() == R.id.ID_radiobutton_controls) {
                if(spinner_controls.getSelectedItemPosition() == 0) {
                    unfilledViews.add(spinner_controls);
                    allfieldsfilled = false;
                } else {
                    filledViews.add(spinner_controls);
                }
            }
        }

        return allfieldsfilled;
    }

    public void highlightRequired() {
        for(View v : unfilledViews){
            v.setBackgroundColor(getResources().getColor(R.color.requiredField));
        }
        for(View v : filledViews){
            if(findViewById(v.getId()) instanceof Spinner)
                v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            else if (findViewById(v.getId()) instanceof RadioGroup)
                v.setBackgroundColor(Color.TRANSPARENT);
        }
        unfilledViews.clear();
        filledViews.clear();
    }

    public Intent createIntent(String patientCategory, String level1, String level2) {
        Intent intent = new Intent(ClinicianDataActivity.this, PatientQuestionnaireActivity.class);
        String patientCategoryString = patientCategory + " " + level1 + level2;
        intent.putExtra(EXTRA_CLINICIAN_NAME, spinner_clinician.getSelectedItem().toString());
        intent.putExtra(EXTRA_PATIENT_TYPE, patientCategoryString);
        return intent;
    }

}
