package com.example.planetze;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import utilities.Constants;

 
public class SurveyResults extends Fragment {
		/*private FirebaseAuth auth;
    private DatabaseReference userRef;
    private FirebaseDatabase db;
		*/
    final String[] country = Constants.country;
    final double[] country_emissions = Constants.country_emissions;
    String default_country = Constants.default_country;
    double user_e = 0.0;  //total user emissions
    List<String> results_string;
    List<Double> results = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_results, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

				//this big comment block may be important DO NOT REMOVE FOR NOW
        /*auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
        String userId = "IHdNxXO2pGXsicTlymf5HQAaUnL2";  //this should be changed to the particular logged in user
        DatabaseReference userArrayRef = userRef.child(userId).child("survey_results");
        // Read the data
        userArrayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the array exists
                if (dataSnapshot.exists()) {
                    // Convert the snapshot into a List
                    results_string = (List<String>) dataSnapshot.getValue();
                    for (int i = 0; i < results_string.size(); i++)   //convert results to double
                        results.add(Double.valueOf(results_string.get(i)));
                    results = kgToTons(results);
                    user_e = sum(results);  //saves user result immediately
                    System.out.println(user_e);
                    setCategoryGraph(results);  //sets category graph
                } else {
                    System.out.println("FirebaseData: Array does not exist for this user.");
                }


                //initializes some basics
                final TextView your_emissions = findViewById(R.id.your_emissions);
                    String sum = round(user_e) + "   ";
                    your_emissions.setText(sum);
                final TextView total_bar = findViewById(R.id.total_bar);
                    String msg = sum + " tons of CO2 emitted annually";
                    total_bar.setText(msg);
                //setCategoryGraph(results);  //sets category graph

                initComparisonGraph(default_country);  //initializes comparison graph
                setUserDataComparisonGraph(user_e);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("FirebaseData" + "Error: " + databaseError.getMessage());
            }
        });*/
        Bundle args = getArguments();

        assert args != null;
        double[] results = args.getDoubleArray("co2PerCategory");

        assert results != null;
        results = kgToTons(results);
        user_e = sum(results);  //saves user result immediately

        //initializes some basics
        final TextView your_emissions = view.findViewById(R.id.your_emissions);
            String sum = round(user_e) + "   ";
            your_emissions.setText(sum);
        final TextView total_bar = view.findViewById(R.id.total_bar);
            String msg = sum + " tons of CO2 emitted annually";
            total_bar.setText(msg);
        setCategoryGraph(view, results);  //sets category graph

        initComparisonGraph(view, default_country);  //initializes comparison graph
        setUserDataComparisonGraph(view, user_e);

        //spinner change listener
        Spinner s = view.findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Use a current view
                setComparisonGraph(view, selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setComparisonGraph(view, default_country);
            }
        });

        return view;
    }


    /**
     * Sets the first graph depicting breakdown of user emissions by category
		 * "public void setCategoryGraph(List<Double> results) {" was the previous method declaration,
		 * may be useful for future.
     * @param results
     */
    public void setCategoryGraph(View view, double[] results) {
        TextView[] bars = {
                    view.findViewById(R.id.transport),
                    view.findViewById(R.id.food),
                    view.findViewById(R.id.housing),
                    view.findViewById(R.id.consumption)};
        TextView[] extra = {
                    view.findViewById(R.id.t_negligible),
                    view.findViewById(R.id.f_negligible),
                    view.findViewById(R.id.h_negligible),
                    view.findViewById(R.id.c_negligible)};
        double max_result = max(results);

        //preprocessing
        //230 is based on container width/bar width calculations in XML
        //230 = 358 (container width) - 104 (x value of start of bar) - 24 (extra spacing)
        // Converts container width to pixels
        int container_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                230, getResources().getDisplayMetrics());
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        // Convert dp to pixels. 35dp chosen based on UI
        int min_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                25, display_metrics);

        //implementing bar length (updating ui)
        for (int i = 0; i < bars.length; i++) {
            //computes pixel width for current bar
            int p = Math.max(min_p, (int) (container_width * (results.get(i) / max_result)));
            if (p == min_p) extra[i].setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutParams = bars[i].getLayoutParams();
            layoutParams.width = p;  //sets width of bar
            bars[i].setLayoutParams(layoutParams);
            bars[i].requestLayout();  //updates UI

            String txt = String.valueOf(round(results.get(i)));
            bars[i].setText(txt);
        }
    }


    /**
     * Initializes second graph (comparison of user emissions with a selected country)
     * to default country and loads spinner with list of countries
     * @param c the default country
     */
    private void initComparisonGraph(View view, String c) {
        //initialize spinner to country c
        Spinner s = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                 android.R.layout.simple_spinner_item, country);  //used to load spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);  //loads spinner
        int init = adapter.getPosition(c);  //initializes spinner to default country
        s.setSelection(init);  //^^

        setComparisonGraph(view, c);  //set UI
    }


    /**
     * Updates the comparison graph based on current spinner setting
     * @param c the country selected by spinner for which we display average emissions
     */
    private void setComparisonGraph(View view, String c) {
        int i = indexOf(country, c);
        double country_e = country_emissions[indexOf(country, c)];

        assert view.findViewById(R.id.user_bar) != null;

        //computes desired bar height of compared country
        double height_user_in_p = view.findViewById(R.id.user_bar).getHeight();
        // Convert dp to pixels. 13dp and 125dp chosen based on UI
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        int min_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                13, display_metrics);
        int max_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                125, display_metrics);
        //next line is main formula. Math.abs() used in the case that user emissions are < 0
        int height_in_p =
                (int) Math.min(max_p, Math.max(min_p, Math.abs((country_e/user_e) * height_user_in_p)));

        //sets bar height of compared country
        TextView bar = view.findViewById(R.id.country_bar);
        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = height_in_p;
        bar.setLayoutParams(params);
        //bar.requestLayout();  //possibly necessary. For now no issues without this

        //sets text of bar height textbox
        TextView bar_height = view.findViewById(R.id.country_emissions);
        String x = String.valueOf(round(country_e)) + "   ";
        bar_height.setText(x);
    }


    /**
     * Sets the bar representing user emissions in the comparison graph.
     * Reason we need this is to adjust user emissions bar to be very low if needed.
     * Otherwise default setting of user bar is about half the size of the container
     * @param e user emissions
     */
    private void setUserDataComparisonGraph(View view, double e) {
        TextView user_bar = view.findViewById(R.id.user_bar);
        ViewGroup.LayoutParams params = user_bar.getLayoutParams();
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        int height_in_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                65, display_metrics);  //default user bar height
        if (e < 0) {
            height_in_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    13, display_metrics);
        } else if (e < 1) {
            height_in_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    16, display_metrics);
        }
        params.height = height_in_p;
        user_bar.setLayoutParams(params);
    }


    /**
     * Converts array of carbon emissions in kg to tons
     * @param list array of carbon emissions in kg
     * @return array of carbon emissions in tons
     */
    private List<Double> kgToTons(List<Double> list) {
        list.replaceAll(value -> value / 1000.0);
        return list;
    }

    private double sum(List<Double> list) {
        double total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i);
        }
        return total;
    }

    private double max(List<Double> list) {
        double m = 0.0;
        for (int i = 0; i < list.size(); i++) {
            if (m < list.get(i)) m = list.get(i);
        }
        return m;
    }

    private double round(double x) {
        x = x * 10;
        int y = (int) x;
        return y / 10.0;
    }

    /**
     * Self-explanatory. Java has no built-in array.indexOf function,
     * need one to convert spinner selection to country emissions
     * @param arr
     * @param item
     * @return
     */
    private int indexOf(String[] arr, String item) {
        int x = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(item)) {
                x = i; break;
            }
        }
        return x;
    }

}