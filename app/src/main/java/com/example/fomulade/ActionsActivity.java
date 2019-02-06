package com.example.fomulade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class ActionsActivity extends AppCompatActivity {

    private View previousSelectedGearButton;
    private int lastDieRollResult = 0;
    private int lastGearSelected = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_config:
                    nagivateToActivity(ConfigActivity.class);
                    return true;
                case R.id.navigation_tracking:
                    nagivateToActivity(ResourceTrackingActivity.class);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // TODO save the die roll to preferences so it persists across activity changes
    // TODO save the selected button to preferences so it persists across activity changes

    public void nagivateToActivity(Class targetActivity)
    {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadResourcePreferences();

        View selectedGearButton;
        switch (lastGearSelected)
        {
            case 1:
                selectedGearButton = findViewById(R.id.buttonFirstGear);
                break;
            case 2:
                selectedGearButton = findViewById(R.id.buttonSecondGear);
                break;
            case 3:
                selectedGearButton = findViewById(R.id.buttonThirdGear);
                break;
            case 4:
                selectedGearButton = findViewById(R.id.buttonFourthGear);
                break;
            case 5:
                selectedGearButton = findViewById(R.id.buttonFifthGear);
                break;
            case 6:
                selectedGearButton = findViewById(R.id.buttonSixthGear);
                break;
            default:
                selectedGearButton = findViewById(R.id.buttonFirstGear);
                break;
        }

        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(selectedGearButton);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        saveResourcePreferences();
    }

    public void rollDieFirstGear(View view) {
        lastDieRollResult = generateRandomDieRoll(2, 1);
        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(view);
        lastGearSelected = 1;
    }

    public void rollDieSecondGear(View view) {
        lastDieRollResult = generateRandomDieRoll(3, 2);
        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(view);
        lastGearSelected = 2;
    }

    public void rollDieThirdGear(View view) {
        lastDieRollResult = generateRandomDieRoll(5, 4);
        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(view);
        lastGearSelected = 3;
    }
    public void rollDieFourthGear(View view) {
        lastDieRollResult = generateRandomDieRoll(6, 7);
        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(view);
        lastGearSelected = 4;
    }

    public void rollDieFifthGear(View view) {
        lastDieRollResult = generateRandomDieRoll(10 , 11);
        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(view);
        lastGearSelected = 5;

    }

    public void rollDieSixthGear(View view) {
        lastDieRollResult = generateRandomDieRoll(10 , 21);
        setDisplayedDieRoll(lastDieRollResult);
        selectGearButton(view);
    }


    private int generateRandomDieRoll (int bounds, int minimumRoll)
    {
        Random randomGen = new Random();
        int dieRoll = randomGen.nextInt(bounds);
        dieRoll += minimumRoll;

        // TODO this should be somewhere else
        int resID=getResources().getIdentifier("racecar_sound", "raw", getPackageName());
        MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
        mediaPlayer.start();

        return dieRoll;
    }


    private void setDisplayedDieRoll(int valueRolled)
    {
        TextView rolledResultTextView = null;
        rolledResultTextView = (TextView) findViewById(R.id.textViewRollResult);

        rolledResultTextView.setText(new Integer(valueRolled).toString());

    }

    private void selectGearButton(View view)
    {
        if (previousSelectedGearButton != null)
        {
            previousSelectedGearButton.setBackgroundColor(Color.TRANSPARENT);
        }
        view.setBackgroundColor(Color.RED);
        previousSelectedGearButton = view;
    }

    protected void saveResourcePreferences()
    {
        // Write to a shared preference file
        SharedPreferences sharedPreferences = this.getSharedPreferences("formulaDeSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("lastDieRollResult", lastDieRollResult);
        Log.d("PREFERENCES", "Saving: " + "lastDieRollResult" + " Value:"+ lastDieRollResult);
        editor.putInt("lastGearSelected", lastGearSelected);
        Log.d("PREFERENCES", "Saving: " + "lastGearSelected" + " Value:"+ lastGearSelected);

        editor.commit();
    }

    protected void loadResourcePreferences() {
        // Read from a shared preference file
        SharedPreferences sharedPreferences = this.getSharedPreferences("formulaDeSharedPref", Context.MODE_PRIVATE);

        lastDieRollResult = sharedPreferences.getInt("lastDieRollResult", 0);
        Log.d("PREFERENCES", "Loading: " + "lastDieRollResult" + " Value:"+ lastDieRollResult);

        lastGearSelected = sharedPreferences.getInt("lastGearSelected", 1);
        Log.d("PREFERENCES", "Loading: " + "lastGearSelected" + " Value:"+ lastGearSelected);
    }

}
