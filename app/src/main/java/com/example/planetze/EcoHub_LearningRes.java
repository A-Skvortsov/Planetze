package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;

import com.example.planetze.databinding.ActivityEcoBalanceDestinationBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityEcoHhubBinding;

import java.util.ArrayList;

public class EcoHub_LearningRes extends AppCompatActivity {

    private ActivityEcoBalanceDestinationBinding binding;
    ListDataEcohub listdat;
    String[] descriptions;
    ArrayList<ListDataEcohub> arrayList = new ArrayList<>();
    ListAdapter_eco listAdapter;
    int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEcoBalanceDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        descriptions = new String[]{"Learning Resources", "Green Trends"};
        images = new int[]{R.drawable.ic_google, R.drawable.ic_mail};


        for (int i = 0; i < images.length; i++){

            listdat = new ListDataEcohub(images[i], descriptions[i]);
            arrayList.add(listdat);
        }

        listAdapter = new ListAdapter_eco(getApplicationContext(), arrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("descriptions", descriptions[position]);
                intent.putExtra("images", images[position]);
                startActivity(intent);
            }
        }


        );





    }

}