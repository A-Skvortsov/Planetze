package com.example.planetze;
import android.content.Intent;
import static androidx.navigation.fragment.FragmentKt.findNavController;
import static java.security.AccessController.getContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference userRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
        auth = FirebaseAuth.getInstance();

        /*
        DatabaseReference myRef = db.getReference("testDemo");

//        myRef.setValue("B07 Demo!");
        myRef.child("movies").setValue("B07 Demo!");

        //Intent intent = new Intent(MainActivity.this, SurveyResults.class);
        //startActivity(intent);

         */

        //initilizeData();

        if (savedInstanceState == null) {
            //onOpenApp();
            loadFragment(new EcoTrackerFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



}