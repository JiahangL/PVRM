package com.example.jiahang.pvrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class ClinicianDataActivity extends AppCompatActivity {

    String clinicianName;
    public static final String EXTRA_CLINICIAN_NAME = "com.example.jiahang.pvrm.clinician_name";
    public static final String EXTRA_PATIENT_TYPE = "com.example.jiahang.pvrm.updrs";
    public static final String EXTRA_PATIENT_GENDER = "com.example.jiahang.pvrm.patient_gender";

    Spinner spinner_clinician;
    Spinner spinner_mas;
    Spinner spinner_mts;
    Spinner spinner_updrs;
    Spinner spinner_controls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinician_data);

        initSpinners();
    }

    public void initSpinners() {
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
        spinner_mas = (Spinner)findViewById(R.id.ID_spinner_mas);
        spinner_mas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
                if(position == 0)
                    return;
                Intent intent = createIntent("MAS", position);
                startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_mas = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_mas_levels, R.layout.support_simple_spinner_dropdown_item);
        adapter_mas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mas.setAdapter(adapter_mas);
        /********************/

        /**MTS Spinner**/
        spinner_mts = (Spinner)findViewById(R.id.ID_spinner_mts);
        spinner_mts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
                if(position == 0)
                    return;
                Intent intent = createIntent("MTS", position);
                startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_mts = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_mts_levels, R.layout.support_simple_spinner_dropdown_item);
        adapter_mts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mts.setAdapter(adapter_mts);
        /********************/

        /**UPPRS Spinner**/
        spinner_updrs = (Spinner)findViewById(R.id.ID_spinner_updrs);
        spinner_updrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: update MAS counter in app, launch instructions
                if(position == 0)
                    return;
                Intent intent = createIntent("UDPRS", position);
                startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> adapter_udprs = ArrayAdapter.createFromResource
                (this, R.array.STRING_ARR_udprs_levels, R.layout.support_simple_spinner_dropdown_item);
        adapter_udprs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_updrs.setAdapter(adapter_udprs);
        /********************/

        /**Controls Spinner**/
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
        /********************/
    }

    public Intent createIntent(String patientCategory, int level) {
        Intent intent = new Intent(ClinicianDataActivity.this, PatientQuestionnaireActivity.class);
        String patientCategoryString = patientCategory + " " + Integer.toString(level);
        intent.putExtra(EXTRA_CLINICIAN_NAME, spinner_clinician.getSelectedItem().toString());
        intent.putExtra(EXTRA_PATIENT_TYPE, patientCategoryString);
        intent.putExtra(EXTRA_PATIENT_GENDER, spinner_controls.getSelectedItem().toString());
        return intent;
    }

}
