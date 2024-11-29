package com.example.planetze;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.planetze.databinding.ActivityEntryEcoBalanceBinding;

public class Entry_eco_balance_Activity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEntryEcoBalanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEntryEcoBalanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btn = findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EcoBalanceFragment ecoBalanceFragment = new EcoBalanceFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.formlayout, ecoBalanceFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


}