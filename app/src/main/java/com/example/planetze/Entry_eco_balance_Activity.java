package com.example.planetze;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

                EcoBalance_fragment ecoBalanceFragment = new EcoBalance_fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.formlayout, ecoBalanceFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


}