package com.example.fomulade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceTrackingActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private int lapsInRace = 1;

    private final static String maxTires = "maxTires";
    private List<CheckBox> tireCheckboxes;
    private int tiresRemaining = 0;

    private final static String maxBrakes = "maxBrakes";
    private List<CheckBox> brakeCheckboxes;
    private int brakesRemaining = 0;

    private final static String maxFuel = "maxFuel";
    private List<CheckBox> fuelCheckboxes;
    private int fuelRemaining = 0;

    private final static String maxBody = "maxBody";
    private List<CheckBox> bodyCheckboxes;
    private int bodyRemaining = 0;

    private final static String maxEngine = "maxEngine";
    private List<CheckBox> engineCheckboxes;
    private int engineRemaining = 0;

    private Map<String, Integer> oneLapResourceMax;
    private Map<String, Integer> twoLapResourceMax;
    private Map<String, Integer> threeLapResourceMax;
    private Map<String, Integer> maximumResourcesMap;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_config:
                    nagivateToActivity(ConfigActivity.class);
                    return true;
                case R.id.navigation_actions:
                    nagivateToActivity(ActionsActivity.class);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_tracking_activitiy);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        createResourceMapsPerLapCount();
        buildResourceTypeCollections();
    }

    private void createResourceMapsPerLapCount() {
        oneLapResourceMax = new HashMap<String, Integer>();
        oneLapResourceMax.put(maxTires, 3);
        oneLapResourceMax.put(maxBrakes, 3);
        oneLapResourceMax.put(maxFuel, 2);
        oneLapResourceMax.put(maxBody, 1);
        oneLapResourceMax.put(maxEngine, 1);

        twoLapResourceMax = new HashMap<String, Integer>();
        twoLapResourceMax.put(maxTires, 5);
        twoLapResourceMax.put(maxBrakes, 4);
        twoLapResourceMax.put(maxFuel, 3);
        twoLapResourceMax.put(maxBody, 2);
        twoLapResourceMax.put(maxEngine, 2);

        threeLapResourceMax = new HashMap<String, Integer>();
        threeLapResourceMax.put(maxTires, 5);
        threeLapResourceMax.put(maxBrakes, 4);
        threeLapResourceMax.put(maxFuel, 3);
        threeLapResourceMax.put(maxBody, 2);
        threeLapResourceMax.put(maxEngine, 2);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadResourcePreferences();
    }


    private void buildResourceTypeCollections()
    {
        Map<String, Integer> resourceMap;

        switch (lapsInRace) {
            case 1:
                resourceMap = oneLapResourceMax;
                break;
            case 2:
                resourceMap = twoLapResourceMax;
                break;
            default:
                resourceMap = threeLapResourceMax;
                break;
        }

        tireCheckboxes = buildResourceCheckboxArray("tires", resourceMap.get(maxTires));
        brakeCheckboxes = buildResourceCheckboxArray("brakes", resourceMap.get(maxBrakes));
        bodyCheckboxes = buildResourceCheckboxArray("body", resourceMap.get(maxBody));
        fuelCheckboxes = buildResourceCheckboxArray("fuel", resourceMap.get(maxFuel));
        engineCheckboxes = buildResourceCheckboxArray("engine", resourceMap.get(maxTires));
    }

    View.OnClickListener onCheckBoxChecked(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
            }
        };
    }

    List<CheckBox> buildResourceCheckboxArray(String resourceIdPrefix, int numberOfResources)
    {
        List<CheckBox> resourceCheckBoxArray = new ArrayList<CheckBox>();
        CheckBox resourceCheckBox;
        for (int index = 0; index < numberOfResources; index++)
        {
            resourceCheckBox = new CheckBox(getApplicationContext());
            resourceCheckBox.setId(index);
            resourceCheckBox.setOnClickListener(onCheckBoxChecked(resourceCheckBox));
            resourceCheckBoxArray.add(resourceCheckBox);
        }

        return resourceCheckBoxArray;
    }

    protected void saveResourcePreferences()
    {
        // Write to a shared preference file
        SharedPreferences sharedPreferences = this.getSharedPreferences("formulaDeSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText editTextTeamName = (EditText) findViewById(R.id.textEditTeamName);
        EditText editTextPlayerName = (EditText) findViewById(R.id.textEditPlayerName);

        editor.putString(getString(R.string.settingsTeamName), editTextTeamName.getText().toString());
        editor.putString(getString(R.string.settingsPlayerName), editTextPlayerName.getText().toString());

        int tiresRemaining = 0;
        for (CheckBox checkbox: tireCheckboxes) {
            if (checkbox.isChecked()) tiresRemaining++;
        }
        editor.putInt(getString(R.string.settingsTiresRemaining), tiresRemaining);
        Log.d("PREFERENCES", "Saving " + getString(R.string.settingsTiresRemaining) + " Value: "+ Integer.toString(tiresRemaining));

        int brakesRemaining = 0;
        for (CheckBox checkbox: brakeCheckboxes) {
            if (checkbox.isChecked()) brakesRemaining++;
        }
        editor.putInt("settingBrakesRemaining", brakesRemaining);
        Log.d("PREFERENCES", "Saving " + "settingBrakesRemaining" + " Value: "+ Integer.toString(brakesRemaining));

        int fuelRemaining = 0;
        for (CheckBox checkbox: fuelCheckboxes) {
            if (checkbox.isChecked()) fuelRemaining++;
        }
        editor.putInt(getString(R.string.settingsFuelRemaining), fuelRemaining);
        Log.d("PREFERENCES", "Saving " + getString(R.string.settingsFuelRemaining) + " Value: "+ Integer.toString(fuelRemaining));

        int bodyRemaining = 0;
        for (CheckBox checkbox: bodyCheckboxes) {
            if (checkbox.isChecked()) bodyRemaining++;
        }
        editor.putInt(getString(R.string.settingsBodyRemaining), bodyRemaining);
        Log.d("PREFERENCES", "Saving " + getString(R.string.settingsBodyRemaining) + " Value: "+ Integer.toString(bodyRemaining));

        int engineRemaining = 0;
        for (CheckBox checkbox: engineCheckboxes) {
            if (checkbox.isChecked()) engineRemaining++;
        }
        editor.putInt(getString(R.string.settingsEngineRemaining), engineRemaining);
        Log.d("PREFERENCES", "Saving " + getString(R.string.settingsEngineRemaining) + " Value: "+ Integer.toString(engineRemaining));


        editor.commit();
    }

    protected void loadResourcePreferences()
    {
        // Read from a shared preference file
        SharedPreferences sharedPref = this.getSharedPreferences("formulaDeSharedPref", Context.MODE_PRIVATE);

        String defaultPlayerName = getString(R.string.settingsPlayerName);
        String playerName = sharedPref.getString(getString(R.string.settingsPlayerName), defaultPlayerName);
        EditText settingPlayerName = (EditText) findViewById(R.id.textEditPlayerName);
        settingPlayerName.setText(playerName);

        String defaultTeamName = getString(R.string.settingsTeamName);
        String teamName = sharedPref.getString(getString(R.string.settingsTeamName), defaultTeamName);
        EditText settingTeamName = (EditText) findViewById(R.id.textEditTeamName);
        settingTeamName.setText(teamName);

        lapsInRace = sharedPref.getInt(getString(R.string.settingsLapsInRace), -1);
        Log.d("PREFERENCES", "Loading: " + getString(R.string.settingsLapsInRace) + " Value:"+ lapsInRace);

        switch (lapsInRace)
        {
            case 1:
                maximumResourcesMap = oneLapResourceMax;
                break;
            case 2:
                maximumResourcesMap = twoLapResourceMax;
                break;
            case 3:
                maximumResourcesMap = threeLapResourceMax;
                break;
            default:
                maximumResourcesMap = oneLapResourceMax;
                break;
        }

//        tiresRemaining = sharedPref.getInt(getString(R.string.settingsTiresRemaining), 0);
//        brakesRemaining = sharedPref.getInt(getString(R.string.settingBrakesRemaining), 0);
//        bodyRemaining = sharedPref.getInt(getString(R.string.settingsBodyRemaining), 0);
//        engineRemaining = sharedPref.getInt(getString(R.string.settingsEngineRemaining), 0);
//        fuelRemaining = sharedPref.getInt(getString(R.string.settingsFuelRemaining), 0);

        for (CheckBox checkbox: tireCheckboxes)
        {
            if (tireCheckboxes.indexOf(checkbox) < tiresRemaining)
                checkbox.setChecked(true);
            if (tireCheckboxes.indexOf(checkbox) >= maximumResourcesMap.get(maxTires))
                checkbox.setVisibility(View.INVISIBLE);
            else
                checkbox.setVisibility(View.VISIBLE);
        }
        for (CheckBox checkbox: brakeCheckboxes)
        {
            if (brakeCheckboxes.indexOf(checkbox) < brakesRemaining)
                checkbox.setChecked(true);
            if (brakeCheckboxes.indexOf(checkbox) >= maximumResourcesMap.get(maxBrakes))
                checkbox.setVisibility(View.INVISIBLE);
            else
                checkbox.setVisibility(View.VISIBLE);
        }
        for (CheckBox checkbox: fuelCheckboxes) {
            if (fuelCheckboxes.indexOf(checkbox) < fuelRemaining)
                checkbox.setChecked(true);
            if (fuelCheckboxes.indexOf(checkbox) >= maximumResourcesMap.get(maxFuel))
                checkbox.setVisibility(View.INVISIBLE);
            else
                checkbox.setVisibility(View.VISIBLE);
        }
        for (CheckBox checkbox: bodyCheckboxes) {
            if (bodyCheckboxes.indexOf(checkbox) < bodyRemaining)
                checkbox.setChecked(true);
            if (bodyCheckboxes.indexOf(checkbox) >= maximumResourcesMap.get(maxBody))
                checkbox.setVisibility(View.INVISIBLE);
            else
                checkbox.setVisibility(View.VISIBLE);
        }
        for (CheckBox checkbox: engineCheckboxes) {
            if (engineCheckboxes.indexOf(checkbox) < engineRemaining)
                checkbox.setChecked(true);
            if (engineCheckboxes.indexOf(checkbox) >= maximumResourcesMap.get(maxEngine))
                checkbox.setVisibility(View.INVISIBLE);
            else
                checkbox.setVisibility(View.VISIBLE);

        }
    }

    public void handleTiresClicked(View view)
    {
        int resID=getResources().getIdentifier("tire_sound", "raw", getPackageName());
        MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
        mediaPlayer.start();
    }
}
