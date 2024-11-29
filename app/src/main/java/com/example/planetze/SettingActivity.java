package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;

import com.example.planetze.Login.LoginView;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivitySettingBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import utilities.UserData;

public class SettingActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySettingBinding binding;

    private SwitchMaterial stayLoggedOn;
    private SwitchMaterial interpolateEmissionsData;
    private Button returnButton;
    private Button logoutButton;

    private DatabaseReference userRef;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        stayLoggedOn = findViewById(R.id.stay_logged_on_switch);
        returnButton = findViewById(R.id.returnButton);
        logoutButton = findViewById(R.id.logoutButton);
        interpolateEmissionsData = findViewById(R.id.ied_switch);

        initialize();

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(j);

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.logout(getApplicationContext());
                loadFragment(new LoginView());
            }
        });

        interpolateEmissionsData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = UserData.getUserID(getApplicationContext());

                db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
                userRef = db.getReference("user data");
                if (stayLoggedOn.isChecked()) {
                    userRef.child(userID+"/settings/interpolate_emissions_data").setValue(true);
                }
                else {
                    userRef.child(userID+"/settings/interpolate_emissions_data").setValue(false);
                }

            }
        });

        stayLoggedOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = UserData.getUserID(getApplicationContext());

                db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
                userRef = db.getReference("user data");
                if (stayLoggedOn.isChecked()) {
                    userRef.child(userID+"/settings/stay_logged_on").setValue(true);
                }
                else {
                    userRef.child(userID+"/settings/stay_logged_on").setValue(false);
                }

            }
        });
    }
    private void initialize() {
        stayLoggedOn.setChecked(UserData.stayLoggedOn(getApplicationContext()));
        interpolateEmissionsData.setChecked(UserData.interpolateEmissionsData(getApplicationContext()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}