package com.example.planetze;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityMarketTrendsBinding;

public class MarketTrends extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMarketTrendsBinding binding;
    TextView textview1;
    TextView textview2;
    TextView textview3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMarketTrendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textview1 =findViewById(R.id.hyperlink1);
        textview1.setMovementMethod(LinkMovementMethod.getInstance());

        textview2 =findViewById(R.id.hyperlink2);
        textview2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}