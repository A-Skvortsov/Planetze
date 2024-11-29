package com.example.planetze;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.planetze.databinding.ActivityEcoHubBinding;

public class EcoHub_ACTIVITY extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEcoHubBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEcoHubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Button btn = findViewById(R.id.buttonEcoHUb);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nati1, new EcoHubEntryFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }



}