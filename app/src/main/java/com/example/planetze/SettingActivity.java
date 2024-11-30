package com.example.planetze;

import static utilities.Constants.HIDE_GRID_LINES;
import static utilities.Constants.INTERPOLATE_EMISSIONS_DATA;
import static utilities.Constants.SHOW_TREND_LINE_POINTS;
import static utilities.Constants.STAY_LOGGED_ON;

import android.content.Intent;
import android.os.Bundle;

import com.example.planetze.Login.LoginView;


import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import utilities.UserData;

public class SettingActivity extends AppCompatActivity {

    private SwitchMaterial stayLoggedOnSwitch;
    private SwitchMaterial interpolateEmissionsDataSwitch;
    private SwitchMaterial hideGridLinesSwitch;
    private SwitchMaterial showTrendLinePointsSwitch;
    private Button returnButton;
    private Button logoutButton;

    private TextView name;
    private TextView email;

    private DatabaseReference userRef;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        returnButton = findViewById(R.id.returnButton);
        logoutButton = findViewById(R.id.logoutButton);
        stayLoggedOnSwitch = findViewById(R.id.stay_logged_in_switch);
        interpolateEmissionsDataSwitch = findViewById(R.id.ied_switch);
        showTrendLinePointsSwitch = findViewById(R.id.show_trend_line_points_switch);
        hideGridLinesSwitch = findViewById(R.id.hide_grid_lines_switch);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        name.setText(UserData.getUsername(getApplicationContext()));
        email.setText(UserData.getEmail(getApplicationContext()));

        initialize();

        returnButton.setOnClickListener(view -> {
            UserData.initialize(getApplicationContext());
            Intent j = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(j);

        });

        logoutButton.setOnClickListener(view -> {
            UserData.logout(getApplicationContext());
            loadFragment(new LoginView());
        });

        stayLoggedOnSwitch.setOnClickListener(view -> {
            String userID = UserData.getUserID(getApplicationContext());

            db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
            userRef = db.getReference("user data");
            if (stayLoggedOnSwitch.isChecked()) {
                userRef.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(false);
            }

        });

        interpolateEmissionsDataSwitch.setOnClickListener(view -> {
            String userID = UserData.getUserID(getApplicationContext());

            db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
            userRef = db.getReference("user data");
            if (interpolateEmissionsDataSwitch.isChecked()) {
                userRef.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(false);
            }

        });

        hideGridLinesSwitch.setOnClickListener(view -> {
            String userID = UserData.getUserID(getApplicationContext());

            db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
            userRef = db.getReference("user data");
            if (hideGridLinesSwitch.isChecked()) {
                userRef.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(false);
            }
        });


        showTrendLinePointsSwitch.setOnClickListener(view -> {
            String userID = UserData.getUserID(getApplicationContext());

            db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
            userRef = db.getReference("user data");
            if (showTrendLinePointsSwitch.isChecked()) {
                userRef.child(userID+"/settings/" + SHOW_TREND_LINE_POINTS).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+ SHOW_TREND_LINE_POINTS).setValue(false);
            }
        });
    }

    private void initialize() {
        boolean stayLoggedOn = UserData.getSetting(getApplicationContext(),STAY_LOGGED_ON);
        boolean interpolateEmissionsData = UserData.getSetting(getApplicationContext(),INTERPOLATE_EMISSIONS_DATA);
        boolean hideGridLines = UserData.getSetting(getApplicationContext(),HIDE_GRID_LINES);
        boolean showTrendLinePoints = UserData.getSetting(getApplicationContext(),SHOW_TREND_LINE_POINTS);

        stayLoggedOnSwitch.setChecked(stayLoggedOn);
        interpolateEmissionsDataSwitch.setChecked(interpolateEmissionsData);
        hideGridLinesSwitch.setChecked(hideGridLines);
        showTrendLinePointsSwitch.setChecked(showTrendLinePoints);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}