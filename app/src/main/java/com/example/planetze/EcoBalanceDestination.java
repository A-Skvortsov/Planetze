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

    String[] par;
    String p1 = "Tree Canada’s Grow Clean Air program supports improved forest management practices led by coastal First Nations communities living in the Great Bear Rainforest. You can offset your carbon emissions by helping to protect old-growth trees in British Columbia. Users can help prevent deforestation and land clearing, which then prevents the release of carbon, through these trees.";
    String p2 = "Tree Canada’s Grow Clean Air program supports reforestation, through growing trees where forests have become damaged, and where there is empty land. This is a great way to help offset, users' carbon emissions as trees absorb carbon dioxide as they grow, which will help to remove and store carbon from the air. ";
    String p3 = "GivePower Foundation is a foundation that supplies eco-friendly equipment like solar panels, to developing regions who need it the most. Using Solar technology helps stop using other technologies that rely on fossil fuels, which are responsible for 40% of the carbon dioxide emitted in the world.";
    String p4 = "TEA's goal is to create a waste-free future for Toronto. Through reducing, reusing, and recycling, individuals can use fewer natural resources, generate less greenhouse gas emissions, and create good green jobs. These are all methods that can significantly help reduce one's carbon footprint. Through donating to TEA users can help TEA accomplish its goals, of a more eco-friendly environment, while at the same time helping to reduce their carbon footprint.";
    String p5 = "Trees for the Future is a registered 501(c)(3) nonprofit organization, that provides hands-on agroforestry training and resources to farming communities. Thorugh through training Trees for the Future helps fight climate change, reduce poverty,  and develop economic growth within communities.";
    String p6 = "Earthjustice is a registered  501(c)3 nonprofit, whose primary goal is to “End the Extraction and Burning of Fossil Fuels”. The foundation helps to keep coal, oil, and gas in the ground while supporting communities most affected by coal, oil, and gas. A significant amount of America’s and Canada's climate-polluting carbon dioxide emissions come from the burning and extraction of oil, gas, and coal. By supporting this foundation, users can help directly to offset their carbon emissions and also help to reduce millions of tons of carbon dioxide emitted annually.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEcoBalanceDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        descriptions = new String[]{"Grow Clean Air", "Plant A Tree", "Sustainable Technology", "Waste Management", "Supporting Farmers", "End Fossil Fuels"};
        images = new int[]{R.mipmap.clean_air,R.mipmap.plant_tree, R.mipmap.solar, R.mipmap.waste, R.mipmap.farmers, R.mipmap.fossil};
        par = new String[]{p1,p2,p3,p4,p5,p6};

        for (int i = 0; i < images.length; i++){

            listdat = new ListData(images[i], descriptions[i]);
            arrayList.add(listdat);
        }

        listAdapter = new ListAdapter(getApplicationContext(), arrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("descriptions", descriptions[position]);
                intent.putExtra("images", images[position]);
                intent.putExtra("paragraphs", par[position]);
                startActivity(intent);
            }
        }


        );





    }

}