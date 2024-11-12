package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SurveyResults extends AppCompatActivity {

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
    }

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
        int container_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, getResources().getDisplayMetrics());
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        // Convert dp to pixels. 35dp chosen based on UI
        int min_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, display_metrics);

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

}