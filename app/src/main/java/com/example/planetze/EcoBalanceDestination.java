package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityEcoBalanceDestinationBinding;

import java.util.ArrayList;

public class EcoBalanceDestination extends AppCompatActivity {
    private ActivityEcoBalanceDestinationBinding binding;
    ListData listdat;
    String[] descriptions;
    String[] locations;
    ArrayList<ListData> arrayList = new ArrayList<>();
    ListAdapter listAdapter;
    int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEcoBalanceDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        descriptions = new String[]{"Helping Plant Trees", "Building Wells", "Bike providers"};
        locations = new String[]{"Ethiopia", "Russia", "India"};
        images = new int[]{R.drawable.ic_google, R.drawable.ic_mail, R.drawable.ic_lock};


        for (int i = 0; i < images.length; i++){

            listdat = new ListData(images[i], descriptions[i], locations[i]);
            arrayList.add(listdat);
        }

        listAdapter = new ListAdapter(getApplicationContext(), arrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("locations", locations[position]);
                intent.putExtra("descriptions", descriptions[position]);
                intent.putExtra("images", images[position]);
                startActivity(intent);
            }
        }


        );





    }

}