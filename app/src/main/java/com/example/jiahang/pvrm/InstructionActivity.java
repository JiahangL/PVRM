package com.example.jiahang.pvrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class InstructionActivity extends AppCompatActivity {

    public static final String EXTRA_FOREARM_LENGTH = "com.example.jiahang.pvrm.forearm_length";

    ViewPager viewPager;

    int [] instruction_index = new int[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        viewPager = (ViewPager) findViewById(R.id.ID_viewpager_instruction);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                // position is not zero indexed, but arrays are
                return InstructionFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return instruction_index.length;
            }
        });
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ID_menuitem_clinician_next:
                Intent intent = new Intent(InstructionActivity.this, MainActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}
