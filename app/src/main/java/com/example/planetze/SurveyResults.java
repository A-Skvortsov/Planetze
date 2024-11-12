package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import utilities.Constants;

public class SurveyResults extends AppCompatActivity {

    final String[] country = Constants.country;
    final double[] country_emissions = Constants.country_emissions;
    String default_country = Constants.default_country;
    double user_e = 0.0;  //total user emissions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survey_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        double[] results = intent.getDoubleArrayExtra("co2PerCategory");
        results = kgToTons(results);
        setCategoryGraph(results);

        user_e = sum(results);
        initComparisonGraph(default_country);

        //spinner change listener
        Spinner s = findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                setComparisonGraph(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setComparisonGraph(default_country);
            }
        });

    }


    /**
     * Sets the first graph depicting breakdown of user emissions by category
     * @param results
     */
    public void setCategoryGraph(double[] results) {
        TextView[] bars = {
                    findViewById(R.id.transport),
                    findViewById(R.id.food),
                    findViewById(R.id.housing),
                    findViewById(R.id.consumption)};
        TextView[] extra = {
                    findViewById(R.id.t_negligible),
                    findViewById(R.id.f_negligible),
                    findViewById(R.id.h_negligible),
                    findViewById(R.id.c_negligible)};
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
            int p = Math.max(min_p, (int) (container_width * (results[i] / max_result)));
            if (p == min_p) extra[i].setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutParams = bars[i].getLayoutParams();
            layoutParams.width = p;  //sets width of bar
            bars[i].setLayoutParams(layoutParams);
            bars[i].requestLayout();  //updates UI

            String txt = String.valueOf(round(results[i]));
            bars[i].setText(txt);
        }
    }


    /**
     * Initializes second graph (comparison of user emissions with a selected country)
     * to default country and loads spinner with list of countries
     * @param c the default country
     */
    private void initComparisonGraph(String c) {
        //initialize spinner to country c
        Spinner s = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                 android.R.layout.simple_spinner_item, country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        int init = adapter.getPosition(c);
        s.setSelection(init);

        setComparisonGraph(c);
    }


    /**
     * Updates the comparison graph based on current spinner setting
     * @param c the country selected by spinner for which we display average emissions
     */
    private void setComparisonGraph(String c) {
        int i = indexOf(country, c);
        double country_e = country_emissions[indexOf(country, c)];

        //computes desired bar height of compared country
        double height_user_in_p = findViewById(R.id.user_bar).getHeight();
        // Convert dp to pixels. 25dp and 125dp chosen based on UI
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        int min_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                25, display_metrics);
        int max_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                125, display_metrics);
        //next line is main formula
        int height_in_p =
                (int) Math.min(max_p, Math.max(min_p, (country_e/user_e) * height_user_in_p));

        //sets bar height of compared country
        TextView bar = findViewById(R.id.country_bar);
        bar.setHeight(height_in_p);
        TextView bar_height = findViewById(R.id.country_emissions);

        //sets text of bar height textbox
        String x = String.valueOf(round(country_e)) + "   ";
        bar_height.setText(x);
    }


    /**
     * Converts array of carbon emissions in kg to tons
     * @param arr array of carbon emissions in kg
     * @return array of carbon emissions in tons
     */
    private double[] kgToTons(double[] arr) {
        double[] a = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            a[i] = arr[i] / 1000.0;
        }
        return a;
    }

    private double sum(double[] arr) {
        double total = 0.0;
        for (int i = 0; i < arr.length; i++) {
            total += arr[i];
        }
        return total;
    }

    private double max(double[] arr) {
        double m = 0.0;
        for (int i = 0; i < arr.length; i++) {
            if (m < arr[i]) m = arr[i];
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