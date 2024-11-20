package com.example.planetze;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityEcoGaugeBinding;

public class EcoGauge extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEcoGaugeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        binding = ActivityEcoGaugeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ... (rest of your onCreate code)
    }

    // ... (other methods)
}