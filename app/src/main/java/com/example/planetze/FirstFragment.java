package com.example.planetze;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private PieChart pieChart;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        this.pieChart = view.findViewById(R.id.pie_chart);

        MaterialButtonToggleGroup timePeriodToggle = view.findViewById(R.id.time_period_toggle);

        timePeriodToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.button_daily) {
                    updateWithDailyData();
                } else if (checkedId == R.id.button_weekly) {
                    updateWithWeeklyData();
                } else if (checkedId == R.id.button_monthly) {
                    updateWithMonthlyData();
                } else if (checkedId == R.id.button_overall) {
                    updateWithOverallData();
                }
            }
        });

        renderPieChart();

        return view;
    }

    private void updateWithDailyData() {
        Log.d("Data", "Daily!");
        pieChart.invalidate();
    }

    private void updateWithWeeklyData() {
        Log.d("Data", "Weekly!");
    }

    private void updateWithMonthlyData() {
        Log.d("Data", "Monthly!");
    }

    private void updateWithOverallData() {
        Log.d("Data", "Overall!");
    }

    private void renderPieChart() {
        PieDataSet pieDataSet = getPieDataSet();

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#009999"));
//        colors.add(Color.parseColor("#10A3A3"));
        colors.add(Color.parseColor("#23ADAD"));
//        colors.add(Color.parseColor("#37B8B8"));
        colors.add(Color.parseColor("#4EC2C2"));
        colors.add(Color.parseColor("#66CCCC"));
//        colors.add(Color.parseColor("#81D6D6"));
//        colors.add(Color.parseColor("#9DE0E0"));
//        colors.add(Color.parseColor("#BCEBEB"));
//        colors.add(Color.parseColor("#DCF5F5"));

        pieDataSet.setColors(colors);
        pieChart.setData(new PieData(pieDataSet));


        pieChart.animateXY(2000, 2000, Easing.EaseInOutExpo);

        pieChart.getDescription().setEnabled(false);
    }

    @NonNull
    private static PieDataSet getPieDataSet() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String[] sampleCategories = {"Transportation", "Energy use", "Food consumption", "Shopping/consumption"};

        for (int i = 1; i < 5; i++) {
            float value = (float) (i * 10.0);

            PieEntry pieEntry = new PieEntry(value, sampleCategories[i - 1]);
            pieEntries.add(pieEntry);
        }

        return new PieDataSet(pieEntries, "Student");
    }
}