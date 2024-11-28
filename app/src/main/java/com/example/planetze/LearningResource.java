package com.example.planetze;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityLearningResourceBinding;

public class LearningResource extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLearningResourceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLearningResourceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}