package com.example.planetze;

import static utilities.Colors.PALETTE_TURQUOISE;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_100;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_200;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_400;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_500;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_800;
import static utilities.Constants.DAILY;
import static utilities.Constants.MONTHLY;
import static utilities.Constants.OVERALL;
import static utilities.Constants.WEEKLY;
import static utilities.Constants.YEARLY;
import static utilities.Constants.country;
import static utilities.Constants.default_country;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.CountryEmissionsData;
import customDataStructures.EmissionNode;
import customDataStructures.EmissionsNodeCollection;
import utilities.UserEmissionsData;

public class FirstFragment extends Fragment {

    private View view;
    private LineChart lineChart;
    private PieChart pieChart;
    private BarChart comparisonChart;
    private CountryEmissionsData countryEmissions;
    private UserEmissionsData userEmissionsData;
    private TextView emissionsOverviewTextView;
    private Spinner comparisonSpinner;
    private TextView comparisonPercentageText;

    private char timePeriod;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        this.view = view;

        this.lineChart = view.findViewById(R.id.line_chart);
        this.pieChart = view.findViewById(R.id.pie_chart);
        this.comparisonChart = view.findViewById(R.id.bar_chart);
        this.countryEmissions = new CountryEmissionsData(requireContext());
        this.emissionsOverviewTextView = view.findViewById(R.id.emissions_overview_textview);
        this.comparisonSpinner = view.findViewById(R.id.spinner);
        this.comparisonPercentageText = view.findViewById(R.id.comparison_percentage_text);

        this.timePeriod = OVERALL;

        // TODO: THE SOURCE OF THE ID BELOW SHOULD BE CHANGED TO ACCURATELY REFLECT THE CURRENT USER
        this.userEmissionsData = new UserEmissionsData("QMCLRlEKD9h2Np1II1vrNU0vpxt2", new UserEmissionsData.DataReadyListener() {
            @Override
            public void onDataReady() {
                updateUI();
            }

            @Override
            public void onError(String errorMessage) {
                showError(errorMessage);
            }
        });

        MaterialButtonToggleGroup timePeriodToggle = view.findViewById(R.id.time_period_toggle);

        timePeriodToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.button_daily) {
                    timePeriod = DAILY;
                } else if (checkedId == R.id.button_weekly) {
                    timePeriod = WEEKLY;
                } else if (checkedId == R.id.button_monthly) {
                    timePeriod = MONTHLY;
                } else if (checkedId == R.id.button_yearly) {
                    timePeriod = YEARLY;
                } else if (checkedId == R.id.button_overall) {
                    timePeriod = OVERALL;
                }
                updateUI();
            }
        });

        comparisonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                renderComparisonChart(comparisonChart, selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                renderComparisonChart(comparisonChart, default_country);
            }
        });

        return view;
    }

    private void showError(String message) {
        emissionsOverviewTextView.setText(message);
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private void updateUI() {
        System.out.println(userEmissionsData.getUserEmissionsDataKG(timePeriod));

        renderEmissionsViewText();
        renderLineChart(lineChart);
        renderPieChart(pieChart);
        renderComparisonUI();
    }

    /**
     * Initializes second graph (comparison of user emissions with a selected country)
     * to default country and loads spinner with list of countries
     */
    private void renderComparisonUI() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comparisonSpinner.setAdapter(adapter);
        int init = adapter.getPosition(default_country);
        comparisonSpinner.setSelection(init);

        renderComparisonChart(comparisonChart, default_country);
    }

    private void renderEmissionsViewText() {
        emissionsOverviewTextView.setText(getUserEmissionsText());
    }

    private String getUserEmissionsText() {
        switch(timePeriod) {
            case DAILY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissionsKG(DAILY) * 100) / 100.0
                        + " kg CO2e today.";
            case WEEKLY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissionsKG(WEEKLY) * 100) / 100.0
                        + " kg CO2e this week.";
            case MONTHLY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissionsKG(MONTHLY) * 100) / 100.0
                        + " kg CO2e this month.";
            case YEARLY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissionsKG(YEARLY) * 100) / 100.0
                        + " kg CO2e this year.";
            default:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissionsKG(OVERALL) * 100) / 100.0
                        + " kg CO2e.";
        }
    }

    private void renderLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        List<EmissionsNodeCollection> emissionsNodeCollections
                = userEmissionsData.getUserEmissionsDataKG(timePeriod);

        System.out.println(emissionsNodeCollections.size());
        System.out.println(emissionsNodeCollections);

        for (int i = 0; i < emissionsNodeCollections.size(); i++) {
            EmissionsNodeCollection collection = emissionsNodeCollections.get(i);

            float totalEmissions = 0;
            for (EmissionNode node : collection.getData()) {
                totalEmissions += node.getEmissionsAmount();
            }

            entries.add(new Entry(i, totalEmissions));
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{PALETTE_TURQUOISE_TINT_800, PALETTE_TURQUOISE_TINT_400}
        );
        gradientDrawable.setCornerRadius(0f);


        LineDataSet lineDataSet = new LineDataSet(entries, "Sample");

        // Apply the gradient to the line chart
        lineDataSet.setFillDrawable(gradientDrawable);

        lineDataSet.setColor(PALETTE_TURQUOISE);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.animateY(2000, Easing.EaseInOutExpo);
    }

    private void renderPieChart(PieChart pieChart) {
        PieDataSet pieDataSet = getPieDataSet();

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(PALETTE_TURQUOISE);
        colors.add(PALETTE_TURQUOISE_TINT_200);
        colors.add(PALETTE_TURQUOISE_TINT_400);
        colors.add(PALETTE_TURQUOISE_TINT_500);

        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(12f);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.getDescription().setEnabled(false);

        pieChart.animateXY(2000, 2000, Easing.EaseInOutExpo);
    }

    private void renderComparisonChart(BarChart comparisonChart, String country) {
        Double countryPerCapitaEmissions = countryEmissions.getComparableEmissionsDataKG(country, timePeriod);
        float userEmissions = userEmissionsData.getUserEmissionsKG(timePeriod);

        if (countryPerCapitaEmissions == null) {
            comparisonChart.invalidate();
            return;
        }

        BarEntry barEntry1 = new BarEntry(0, userEmissions);
        BarEntry barEntry2 = new BarEntry(1, countryPerCapitaEmissions.floatValue());

        List<BarEntry> barEntries = new ArrayList<>(Arrays.asList(barEntry1, barEntry2));

        BarDataSet barDataSet = new BarDataSet(barEntries, "");

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(PALETTE_TURQUOISE_TINT_400);
        colors.add(PALETTE_TURQUOISE_TINT_100);

        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        comparisonChart.setData(barData);

        String[] sampleCountries = {"You", country};

        XAxis xAxis = comparisonChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(sampleCountries));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        comparisonChart.getAxisLeft().setAxisMinimum(Math.min(0f, barData.getYMin()));
        comparisonChart.getAxisRight().setEnabled(false);
        comparisonChart.getDescription().setText("");

        comparisonChart.animateY(2000, Easing.EaseInOutExpo);

        showComparisonPercentage(userEmissions, countryPerCapitaEmissions.floatValue());
    }

    private PieDataSet getPieDataSet() {
        List<EmissionsNodeCollection> emissionsNodeCollections =
                userEmissionsData.getUserEmissionsDataKG(timePeriod);

        List<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> categoryToEmissionsMap = new HashMap<>();

        for (EmissionsNodeCollection collection : emissionsNodeCollections) {
            for (EmissionNode node : collection.getData()) {
                String category = node.getEmissionType();
                float amount = node.getEmissionsAmount();

                categoryToEmissionsMap.put(category,
                        categoryToEmissionsMap.getOrDefault(category, 0f) + amount
                );
            }
        }

        for (String category : categoryToEmissionsMap.keySet()) {
            float value = categoryToEmissionsMap.get(category);
            PieEntry pieEntry = new PieEntry(value, category);
            pieEntries.add(pieEntry);
        }

        return new PieDataSet(pieEntries, "");
    }

    private void showComparisonPercentage(float userEmissions, float countryPerCapitaEmissions) {
        if (userEmissions > countryPerCapitaEmissions) {
            double percentage = Math.round(((userEmissions - countryPerCapitaEmissions)
                    / countryPerCapitaEmissions * 100.0) * 10) / 10.0;
            String text = "+" + percentage + "%";
            comparisonPercentageText.setText(text);
            comparisonPercentageText.setTextColor(Color.RED);
        } else if (userEmissions < countryPerCapitaEmissions) {
            double percentage = Math.round(((countryPerCapitaEmissions - userEmissions)
                    / countryPerCapitaEmissions * 100.0) * 10) / 10.0;
            String text = "-" + percentage + "%";
            comparisonPercentageText.setText(text);
            comparisonPercentageText.setTextColor(Color.GREEN);
        } else {
            comparisonPercentageText.setText("0.0%");
            comparisonPercentageText.setTextColor(Color.GRAY);
        }
    }
}