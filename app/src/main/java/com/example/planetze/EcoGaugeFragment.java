package com.example.planetze;

import static utilities.Colors.PALETTE_TURQUOISE;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_100;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_200;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_400;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_500;
import static utilities.Colors.PALETTE_TURQUOISE_TINT_600;
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

import androidx.cardview.widget.CardView;
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

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.CountryEmissionsData;
import customDataStructures.EmissionNode;
import customDataStructures.EmissionNodeCollection;
import utilities.UserEmissionsData;

public class EcoGaugeFragment extends Fragment {

    private LineChart emissionsTrendGraph;
    private PieChart categoryBreakdownChart;
    private BarChart comparisonChart;

    private CardView trendGraphCard;
    private CardView emissionBreakdownCard;
    private CardView comparisonChartCard;

    private TextView emissionsOverviewTextView;
    private TextView comparisonPercentageText;

    private MaterialButtonToggleGroup timePeriodToggle;
    private Spinner comparisonSpinner;

    private CountryEmissionsData countryEmissions;
    private UserEmissionsData userEmissionsData;
    private int timePeriod;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eco_gauge, container, false);

        this.emissionsTrendGraph = view.findViewById(R.id.line_chart);
        this.categoryBreakdownChart = view.findViewById(R.id.pie_chart);
        this.comparisonChart = view.findViewById(R.id.bar_chart);
        this.countryEmissions = new CountryEmissionsData(requireContext());
        this.emissionsOverviewTextView = view.findViewById(R.id.emissions_overview_textview);
        this.comparisonSpinner = view.findViewById(R.id.spinner);
        this.comparisonPercentageText = view.findViewById(R.id.comparison_percentage_text);

        this.timePeriod = OVERALL;

        this.trendGraphCard = view.findViewById(R.id.trend_graph_card);
        this.emissionBreakdownCard = view.findViewById(R.id.emissions_breakdown_card);
        this.comparisonChartCard = view.findViewById(R.id.comparison_chart_card);

        // TODO: THE SOURCE OF THE ID BELOW SHOULD BE CHANGED TO ACCURATELY REFLECT THE CURRENT USER
        this.userEmissionsData = new UserEmissionsData("QMCLRlEKD9h2Np1II1vrNU0vpxt2", new UserEmissionsData.DataReadyListener() {
            @Override
            public void start() {
                // TODO: Show progress dialog
            }

            @Override
            public void onDataReady() {
                if (userEmissionsData.userHasData()) {
                    // Upon determining the user has data un hide the charts and show the data.
                    unHideUI();
                    updateUI();
                } else {
                    String text = "Add activities to the EcoTracker to begin.";
                    emissionsOverviewTextView.setText(text);
                }
            }

            @Override
            public void onError(String errorMessage) {
                // If an error occurs
                unHideUI();
                showError(errorMessage);
            }
        });

        this.timePeriodToggle = view.findViewById(R.id.time_period_toggle);

        // Using the time period toggle update the local time period variable
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
                // Update the UI everytime to a new time period is selected.
                updateUI();
            }
        });

        // Update the comparison chart everytime the selected country is changed.
        comparisonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                renderComparisonChart(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                renderComparisonChart(default_country);
            }
        });

        // Hide UI to the check for user data can first be complete.
        hideUI();

        return view;
    }

    /**
     * Displays an error message on the emissions overview TextView and with a toast.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        emissionsOverviewTextView.setText(message);
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Un-hides all dashboard UI components.
     */
    private void unHideUI() {
        this.trendGraphCard.setVisibility(View.VISIBLE);
        this.emissionBreakdownCard.setVisibility(View.VISIBLE);
        this.comparisonChartCard.setVisibility(View.VISIBLE);

        this.timePeriodToggle.setVisibility(View.VISIBLE);
    }

    /**
     * Hides all dashboard UI components.
     */
    private void hideUI() {
        this.trendGraphCard.setVisibility(View.GONE);
        this.emissionBreakdownCard.setVisibility(View.GONE);
        this.comparisonChartCard.setVisibility(View.GONE);

        this.timePeriodToggle.setVisibility(View.GONE);
    }

    /**
     * Updates all the charts and emission overview text.
     */
    private void updateUI() {
        renderEmissionsViewText();
        renderEmissionsTrendGraph();
        renderCategoryBreakdownChart();
        renderComparisonUI();
    }

    /**
     * Initializes comparison chart to default country and loads spinner with a list of all
     * countries that can be compared with.
     */
    private void renderComparisonUI() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comparisonSpinner.setAdapter(adapter);

        // Set the default country on the spinner
        comparisonSpinner.setSelection(adapter.getPosition(default_country));

        renderComparisonChart(default_country);
    }

    /**
     * Updates the emissions overview TextView with user emissions data for the selected time period.
     */
    private void renderEmissionsViewText() {
        emissionsOverviewTextView.setText(getUserEmissionsText());
    }

    /**
     * Generates a text summary of user emissions for the selected time period.
     *
     * @return a string representing an overview of the user's emissions in kg CO2e
     * based on the selected time period.
     */
    private String getUserEmissionsText() {
        switch(timePeriod) {
            case DAILY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissions(DAILY) * 100) / 100.0
                        + " kg CO2e today.";
            case WEEKLY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissions(WEEKLY) * 100) / 100.0
                        + " kg CO2e this week.";
            case MONTHLY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissions(MONTHLY) * 100) / 100.0
                        + " kg CO2e this month.";
            case YEARLY:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissions(YEARLY) * 100) / 100.0
                        + " kg CO2e this year.";
            default:
                return "You emitted "
                        + Math.round(userEmissionsData.getUserEmissions(OVERALL) * 100) / 100.0
                        + " kg CO2e.";
        }
    }

    /**
     * Renders the trend graph which displays the user's emissions data
     * over the selected time period.
    */
    private void renderEmissionsTrendGraph() {
        ArrayList<Entry> entries = new ArrayList<>();

        // Retrieve data
        List<EmissionNodeCollection> emissionNodeCollections
                = userEmissionsData.getUserEmissionsData(timePeriod);

        List<String> dates = new ArrayList<>();

        // Extract emissions data and dates
        for (int i = 0; i < emissionNodeCollections.size(); i++) {
            EmissionNodeCollection collection = emissionNodeCollections.get(i);

            float totalEmissions = 0;
            for (EmissionNode node : collection.getData()) {
                totalEmissions += node.getEmissionsAmount();
            }

            entries.add(new Entry(i, totalEmissions));

            // Store dates for the data, so that it can to format the x-axis
            dates.add(collection.getDate());
        }

        // Format the x-axis to display dates in the format dd/mm/yyyy
        emissionsTrendGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                // Make sure the index is inbound.
                if (index >= 0 && index < dates.size()) {
                    // Convert the date format from yyyy-mm-dd to the preferred format dd/mm/yyyy.
                    String[] dateSegments = dates.get(index).split("-");
                    return dateSegments[2] + "/" + dateSegments[1] + "/" + dateSegments[0];
                }
                return "";
            }
        });

        // Create a custom gradient to fill in the chart
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{PALETTE_TURQUOISE_TINT_800, PALETTE_TURQUOISE_TINT_400}
        );
        gradientDrawable.setCornerRadius(0f);

        // Chart data configuration. See https://weeklycoding.com/mpandroidchart-documentation/
        LineDataSet lineDataSet = new LineDataSet(entries, "Emission");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setFillDrawable(gradientDrawable);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setColor(PALETTE_TURQUOISE);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(2f);

        LineData lineData = new LineData(lineDataSet);

        // Chart configuration. See https://weeklycoding.com/mpandroidchart-documentation/
        emissionsTrendGraph.animateY(2000, Easing.EaseInOutExpo);
        emissionsTrendGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        emissionsTrendGraph.getDescription().setEnabled(false);
        emissionsTrendGraph.getAxisRight().setEnabled(false);
        emissionsTrendGraph.setDrawGridBackground(false);
        emissionsTrendGraph.setTouchEnabled(true);
        emissionsTrendGraph.setDragEnabled(true);
        emissionsTrendGraph.setScaleEnabled(true);
        emissionsTrendGraph.setData(lineData);

        // To solve issue where x-axis labels are repeated.
        emissionsTrendGraph.getXAxis().setGranularityEnabled(true);

    }

    /**
     * Renders the category breakdown chart which displays the user's
     * emissions data broken down into different emission categories with data over the selected
     * time period.
     */
    private void renderCategoryBreakdownChart() {
        // Retrieve the dataset containing emissions data
        PieDataSet pieDataSet = getPieDataSet();

        // Define a custom colour palette for the pie chart slices
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(PALETTE_TURQUOISE);
        colors.add(PALETTE_TURQUOISE_TINT_200);
        colors.add(PALETTE_TURQUOISE_TINT_400);
        colors.add(PALETTE_TURQUOISE_TINT_500);
        colors.add(PALETTE_TURQUOISE_TINT_600);
        colors.add(PALETTE_TURQUOISE_TINT_800);

        // Chart data configuration. See https://weeklycoding.com/mpandroidchart-documentation/
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(12f);

        // Chart configuration. See https://weeklycoding.com/mpandroidchart-documentation/
        categoryBreakdownChart.animateXY(2000, 2000, Easing.EaseInOutExpo);
        categoryBreakdownChart.getDescription().setEnabled(false);
        categoryBreakdownChart.setData(new PieData(pieDataSet));
        categoryBreakdownChart.setEntryLabelTextSize(12f);
    }

    /**
     * Creates and returns a {@link PieDataSet} representing different emissions categories along
     * with the emission amount.
     *
     * @return a {@link PieDataSet} containing pie chart entries with category names along with
     * their corresponding emissions values.
     */
    private PieDataSet getPieDataSet() {
        // Retrieve data
        List<EmissionNodeCollection> emissionNodeCollections =
                userEmissionsData.getUserEmissionsData(timePeriod);

        List<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> categoryToEmissionsMap = new HashMap<>();


        // Sum the emission amounts for each category for all data regardless of date.
        for (EmissionNodeCollection collection : emissionNodeCollections) {
            for (EmissionNode node : collection.getData()) {
                String category = node.getEmissionType();
                float amount = node.getEmissionsAmount();

                categoryToEmissionsMap.put(category,
                        categoryToEmissionsMap.getOrDefault(category, 0f) + amount
                );
            }
        }

        // Convert the data into pie chart entries
        for (String category : categoryToEmissionsMap.keySet()) {
            float value = categoryToEmissionsMap.get(category);
            PieEntry pieEntry = new PieEntry(value, category);
            pieEntries.add(pieEntry);
        }

        return new PieDataSet(pieEntries, "");
    }

    /**
     * Renders the comparison breakdown chart which compares user emissions with the emissions of
     * the given country over the selected time period
     *
     * @param country The name of the country whose data will be used for the comparison.
     * @see CountryEmissionsData
     */
    private void renderComparisonChart(String country) {
        Double countryPerCapitaEmissions;
        float userEmissions = userEmissionsData.getUserEmissions(timePeriod);;

        /*
            Since OVERALL = Integer.MAX_VALUE and getComparableEmissionsDataKG retrieves comparable
            data for a given country, by taking the per capita emissions of country dividing it
            by 365 and multiplying by time period. To avoid getting an very large inaccurate number
            we must find the first time the user logged activity data and use the days since then
            instead to get the accurate comparison data.
        */
        if (timePeriod == OVERALL) {
            System.out.println();
            countryPerCapitaEmissions = countryEmissions.getComparableEmissionsDataKG(country,
                    userEmissionsData.totalDaysAsUser());
        } else {
            countryPerCapitaEmissions = countryEmissions.getComparableEmissionsDataKG(country, timePeriod);
        }

        // Stop rendering if the country emissions data is not found.
        if (countryPerCapitaEmissions == null) {
            return;
        }

        ArrayList<Integer> colors = new ArrayList<>();

        String[] xLabels = {"You", country};

        // Define a custom colour palette to be for each bar
        colors.add(PALETTE_TURQUOISE_TINT_400);
        colors.add(PALETTE_TURQUOISE_TINT_100);

        // Create two bar entries, one representing the user, and one representing the country.
        BarEntry barEntry1 = new BarEntry(0, userEmissions);
        BarEntry barEntry2 = new BarEntry(1, countryPerCapitaEmissions.floatValue());


        List<BarEntry> barEntries = new ArrayList<>(Arrays.asList(barEntry1, barEntry2));
        BarDataSet barDataSet = new BarDataSet(barEntries, "");

        // Chart data configuration. See https://weeklycoding.com/mpandroidchart-documentation/
        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);

        // Chart configuration. See https://weeklycoding.com/mpandroidchart-documentation/
        comparisonChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        comparisonChart.getAxisLeft().setAxisMinimum(Math.min(0f, barData.getYMin()));
        comparisonChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        comparisonChart.animateY(2000, Easing.EaseInOutExpo);
        comparisonChart.getAxisRight().setEnabled(false);
        comparisonChart.getDescription().setText("");
        comparisonChart.setData(barData);

        // Display the comparison percentage.
        showComparisonPercentage(userEmissions, countryPerCapitaEmissions.floatValue());

        // To solve issue where x-axis labels are repeated.
        comparisonChart.getXAxis().setGranularityEnabled(true);
    }

    /**
     * Calculates and displays the percentage difference between the user's emissions and the
     * per capita emissions of a given country.
     * <p>
     *  - Display the percentage in green if the user's emissions is less than the countries.
     *  - Display the percentage in red if the user's emissions if greater than the countries.
     *  - Display the percentage in gray if the user's emissions are equal to the countries.
     *
     * @param userEmissions the user's emissions for the selected time period.
     * @param countryEmissions the per capita emissions of the specified country over
     *                                  the selected time period.
     */
    private void showComparisonPercentage(float userEmissions, float countryEmissions) {
        if (userEmissions > countryEmissions) {
            // Calculate percent difference and round to one decimal point.
            double percentage = Math.round(((userEmissions - countryEmissions)
                    / countryEmissions * 100.0) * 10) / 10.0;

            String text = "+" + percentage + "%";
            comparisonPercentageText.setText(text);
            comparisonPercentageText.setTextColor(Color.RED);
        } else if (userEmissions < countryEmissions) {
            // Calculate percent difference and round to one decimal point.
            double percentage = Math.round(((countryEmissions - userEmissions)
                    / countryEmissions * 100.0) * 10) / 10.0;

            String text = "-" + percentage + "%";
            comparisonPercentageText.setText(text);
            comparisonPercentageText.setTextColor(Color.GREEN);
        } else {
            comparisonPercentageText.setText("0.0%");
            comparisonPercentageText.setTextColor(Color.GRAY);
        }
    }
}