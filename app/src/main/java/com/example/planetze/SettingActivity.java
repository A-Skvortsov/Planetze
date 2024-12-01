package com.example.planetze;

import static utilities.Constants.FIREBASE_LINK;
import static utilities.Constants.HIDE_GRID_LINES;
import static utilities.Constants.INTERPOLATE_EMISSIONS_DATA;
import static utilities.Constants.HIDE_TREND_LINE_POINTS;
import static utilities.Constants.STAY_LOGGED_ON;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.planetze.Login.LoginView;

import androidx.appcompat.app.AlertDialog;

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
    private SwitchMaterial hideTrendLinePointsSwitch;

    private Button returnButton;
    private Button logoutButton;

    private Button deleteAccountButton;

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
        deleteAccountButton = findViewById(R.id.delete_account_button);
        stayLoggedOnSwitch = findViewById(R.id.stay_logged_in_switch);
        interpolateEmissionsDataSwitch = findViewById(R.id.ied_switch);
        hideTrendLinePointsSwitch = findViewById(R.id.hide_trend_line_points_switch);
        hideGridLinesSwitch = findViewById(R.id.hide_grid_lines_switch);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        name.setText(UserData.getUsername(getApplicationContext()));
        email.setText(UserData.getEmail(getApplicationContext()));

        db = FirebaseDatabase.getInstance(FIREBASE_LINK);
        userRef = db.getReference("user data");
        String userID = UserData.getUserID(getApplicationContext());

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

        deleteAccountButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);

            builder.setTitle("Are you sure you want to delete this account?");
            builder.setMessage("This action cannot be undone");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserData.deleteAccount(getApplicationContext());
                    loadFragment(new LoginView());
                    dialog.cancel();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        });

        stayLoggedOnSwitch.setOnClickListener(view -> {

            if (stayLoggedOnSwitch.isChecked()) {
                userRef.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(false);
            }
            UserData.initialize(getApplicationContext());

        });


        interpolateEmissionsDataSwitch.setOnClickListener(view -> {

            if (interpolateEmissionsDataSwitch.isChecked()) {
                userRef.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(false);
            }
            UserData.initialize(getApplicationContext());

        });

        hideGridLinesSwitch.setOnClickListener(view -> {

            if (hideGridLinesSwitch.isChecked()) {
                userRef.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(false);
            }
            UserData.initialize(getApplicationContext());
        });


        hideTrendLinePointsSwitch.setOnClickListener(view -> {

            if (hideTrendLinePointsSwitch.isChecked()) {
                userRef.child(userID+"/settings/" + HIDE_TREND_LINE_POINTS).setValue(true);
            }
            else {
                userRef.child(userID+"/settings/"+ HIDE_TREND_LINE_POINTS).setValue(false);
            }
            UserData.initialize(getApplicationContext());
        });
    }

    private void initialize() {
        boolean stayLoggedOn = UserData.getSetting(getApplicationContext(),STAY_LOGGED_ON);
        boolean interpolateEmissionsData = UserData.getSetting(getApplicationContext(),INTERPOLATE_EMISSIONS_DATA);
        boolean hideGridLines = UserData.getSetting(getApplicationContext(),HIDE_GRID_LINES);
        boolean hideTrendLinePoints = UserData.getSetting(getApplicationContext(), HIDE_TREND_LINE_POINTS);

        stayLoggedOnSwitch.setChecked(stayLoggedOn);
        interpolateEmissionsDataSwitch.setChecked(interpolateEmissionsData);
        hideGridLinesSwitch.setChecked(hideGridLines);
        hideTrendLinePointsSwitch.setChecked(hideTrendLinePoints);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}