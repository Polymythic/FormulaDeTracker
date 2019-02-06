package com.example.fomulade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class ConfigActivity extends AppCompatActivity {

    private int lapsInRace = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tracking:
                    nagivateToActivity(ResourceTrackingActivity.class);
                    return true;
                case R.id.navigation_actions:
                    nagivateToActivity(ActionsActivity.class);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadResourcePreferences();
        makePresentationConsistentWithPreferences();
    }


    protected void makePresentationConsistentWithPreferences()
    {
        RadioGroup lapsSetting = (RadioGroup) findViewById(R.id.settingLapsInRace);
        RadioButton selectedLapsSettingButton = (RadioButton) findViewById(lapsSetting.getCheckedRadioButtonId());

        switch (lapsInRace) {
            case 1:
                lapsSetting.check(R.id.radio1LapRace);
                break;
            case 2:
                lapsSetting.check(R.id.radio2LapRace);
                break;
            case 3:
                lapsSetting.check(R.id.radio3LapRace);
                break;
        }
    }

    public void nagivateToActivity(Class targetActivity)
    {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }



    @Override
    protected void onPause() {
        super.onPause();

        saveResourcePreferences();
    }


    protected void saveResourcePreferences()
    {
        // Write to a shared preference file
        SharedPreferences sharedPreferences = this.getSharedPreferences("formulaDeSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        RadioGroup lapsSetting = (RadioGroup) findViewById(R.id.settingLapsInRace);
        RadioButton selectedLapsSettingButton = (RadioButton) findViewById(lapsSetting.getCheckedRadioButtonId());

        switch (selectedLapsSettingButton.getId())
        {
            case (R.id.radio1LapRace):
                lapsInRace = 1;
                break;
            case (R.id.radio2LapRace):
                lapsInRace = 2;
                break;
            case (R.id.radio3LapRace):
                lapsInRace = 3;
                break;
        }

        editor.putInt(getString(R.string.settingsLapsInRace), lapsInRace);
        Log.d("PREFERENCES", "Saving: " + getString(R.string.settingsLapsInRace) + " Value: "+ lapsInRace);
        editor.commit();
    }

    protected void loadResourcePreferences() {
        // Read from a shared preference file
        SharedPreferences sharedPreferences = this.getSharedPreferences("formulaDeSharedPref", Context.MODE_PRIVATE);

        lapsInRace = sharedPreferences.getInt(getString(R.string.settingsLapsInRace), 1);
        Log.d("PREFERENCES", "Loading: " + getString(R.string.settingsLapsInRace) + " Value:"+ lapsInRace);

    }
}
