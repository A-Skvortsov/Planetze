package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityEcoHubEntryBinding;

public class EcoHub_Entry extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEcoHubEntryBinding binding;
    CardView crd1;
    CardView crd2;
    CardView[] crd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEcoHubEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        crd1 =  findViewById(R.id.learning_resourc);
        crd2 =  findViewById(R.id.market_trends);

        crd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), LearningResource.class );
                startActivity(i1);
            }
        });

        crd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), MarketTrends.class );
                startActivity(i1);
            }
        });





    }
}