package com.example.planetze;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Retrieve NavController from the NavHostFragment
        NavController navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.fragment))).getNavController();;

        // Setup the NavigationUI using the bottom navigation view and the navigation controller
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
