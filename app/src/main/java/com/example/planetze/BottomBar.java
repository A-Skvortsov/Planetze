package com.example.planetze;

import android.os.Bundle;


import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityBottomBarBinding;

public class BottomBar extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_bar);

        Button btn1 = findViewById(R.id.home2);
        Button btn2 = findViewById(R.id.guage2);
        Button btn3 = findViewById(R.id.balnce2);
        Button btn4 = findViewById(R.id.hub2);

        Button[] btn = {btn1, btn2, btn3, btn4};
        for (int i = 0; i < 4; i++){
            btn[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    Intent j = new Intent(getApplicationContext(), EcoGauge.class);
                    startActivity(j);
                }
            });
        }
        }


        }


