package com.example.planetze;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityEcoHubEntryBinding;

public class EcoHub_Entry extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEcoHubEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEcoHubEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}