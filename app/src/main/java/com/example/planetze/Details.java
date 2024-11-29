package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityDetailsBinding;

public class Details extends AppCompatActivity {
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        if (intent !=null){

            String des =intent.getStringExtra("descriptions");
            String par =intent.getStringExtra("paragraphs");
            int img =intent.getIntExtra("images", -2);

            binding.detailImage.setImageResource(img);
            binding.detailDescription.setText(des);
            binding.detailsParapgraph.setText(par);



        }


    }

}