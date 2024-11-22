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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private PieChart pieChart;
    private PieChart pieChart2;
    private BarChart barChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        this.pieChart = view.findViewById(R.id.pie_chart);
        this.pieChart2 = view.findViewById(R.id.pie_chart2);
        this.barChart = view.findViewById(R.id.bar_chart);

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

        renderPieChart(pieChart);
        renderPieChart(pieChart2);
        renderBarChart(barChart);

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

    private void renderPieChart(PieChart pieChart) {
        PieDataSet pieDataSet = getPieDataSet();

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#009999"));
        colors.add(Color.parseColor("#23ADAD"));
        colors.add(Color.parseColor("#4EC2C2"));
        colors.add(Color.parseColor("#66CCCC"));

        pieDataSet.setColors(colors);
        pieChart.setData(new PieData(pieDataSet));


        pieChart.animateXY(2000, 2000, Easing.EaseInOutExpo);

        pieChart.getDescription().setEnabled(false);
    }

    private void renderBarChart(BarChart barChart) {
        BarDataSet barDataSet = getBarDataSet();

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#23ADAD"));
        colors.add(Color.parseColor("#009999"));

        barDataSet.setDrawValues(false);
        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        String[] sampleCountries = {"Canada", "USA"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(sampleCountries));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.getAxisLeft().setAxisMinimum(Math.min(0f, barData.getYMin()));
        barChart.getAxisRight().setEnabled(false);

        barDataSet.setDrawValues(true);

        barChart.animateY(2000, Easing.EaseInOutExpo);

        barChart.getDescription().setText("");
    }

    @NonNull
    private static PieDataSet getPieDataSet() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String[] sampleCategories = {"Transportation", "Energy", "Food", "Shopping"};

        for (int i = 1; i < 5; i++) {
            float value = (float) (i * 10.0);
            PieEntry pieEntry = new PieEntry(value, sampleCategories[i - 1]);

            pieEntries.add(pieEntry);
        }

        return new PieDataSet(pieEntries, "");
    }

    @NonNull
    private static BarDataSet getBarDataSet() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            float value = (float) (i * 10.0) > 0? (float) (i * 10.0): 5;
            System.out.println(value);

            BarEntry barEntry = new BarEntry(i, value);

            barEntries.add(barEntry);
        }

        return new BarDataSet(barEntries, "");
    }
}