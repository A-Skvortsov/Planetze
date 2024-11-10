package com.example.planetze;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.stream.IntStream;

import utilities.Constants;

public class Init_Survey extends AppCompatActivity {
    int current_cat = 0;  //0-transportation,1-food, 2-housing,3-consumption
    int current_q = 0;  //index of current question
    double[] co2PerCategory = {0.0, 0.0, 0.0, 0.0};
    final String[] categories = Constants.categories;
    //final int total_cats = categories.length;
    final String[][] questions = Constants.questions;
    final int num_qs = size(questions);
    final int num_transport_qs = Constants.transport_qs;
    final int num_food_qs = Constants.food_qs;
    final int num_housing_qs = Constants.housing_qs;
    final int num_consumption_qs = Constants.consumption_qs;
    //final int total_qs = num_transport_qs + num_food_qs + num_housing_qs + num_consumption_qs;
    final double[][][][][] housing_emissions = Constants.housing_emissions;
    //elements of arrays below specify which answer option is selected for a particular question
    final double[][] public_transport_emissions = Constants.public_trans_emissions;
    final double[][][] recycling_reduction = Constants.recycling_reduction;
    int[] transport_ans = new int[num_transport_qs];  //7 qs in transportation category
    int[] food_ans = new int [num_food_qs];
    int[] housing_ans = new int[num_housing_qs];
    int[] consumption_ans = new int[num_consumption_qs];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init_survey);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });  //end of default onCreate() code

        //final vars correspond to UI components
        final TextView please_answer1 = findViewById(R.id.please_answer1);
        final TextView please_answer2 = findViewById(R.id.please_answer2);
        final Button iterator_btn = findViewById(R.id.iteratorButton);
        final TextView category = findViewById(R.id.category);
        final TextView question = findViewById(R.id.question);
        final RadioGroup options = findViewById(R.id.options);
        final TextView result = findViewById(R.id.result_test);
            //nested code initializes survey at first question
            category.setText(categories[current_cat]);
            question.setText(questions[current_q][0]);
            for (int i = 1; i < questions[current_q].length; i++) {
                RadioButton btn = new RadioButton(Init_Survey.this);
                btn.setId(i - 1);
                btn.setText(questions[current_q][i]);
                options.addView(btn);
            }

        //method to iterate through questions on button click
        iterator_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if no answer selected, should not proceed. Should prompt user to answer
                if (!saveAnswer(options, current_cat, current_q)) {  //saves user's answer to prev question
                    please_answer1.setVisibility(View.VISIBLE); please_answer2.setVisibility(View.VISIBLE);
                    return;
                }
                please_answer1.setVisibility(View.INVISIBLE); please_answer2.setVisibility(View.INVISIBLE);

                current_q++;  //iterates to next q
                if (current_q >= num_qs) {  //true if survey is finished
                    computeCatEmissions(current_cat);
                    category.setVisibility(View.INVISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    options.setVisibility(View.INVISIBLE);
                    result.setText(String.valueOf(sum(co2PerCategory)));
                    return;
                }
                if (questions[current_q][0].equals("-")) {  //iter'n to next category if necessary
                    computeCatEmissions(current_cat);  //computes emissions for the finished category
                    current_q++; current_cat++;
                    category.setText(categories[current_cat]);
                }
                question.setText(questions[current_q][0]);  //displays next q

                options.removeAllViews(); options.clearCheck();  //remove previous answer options
                for (int i = 1; i < questions[current_q].length; i++) { //loading answer options for the new q
                    RadioButton btn = new RadioButton(Init_Survey.this);
                    btn.setId(i - 1);  //standard btn configurations
                    btn.setText(questions[current_q][i]);
                    options.addView(btn);  //adds btn to the RadioGroup (btn container)
                }
            }
        });
    }


    /**
     * Updates the global arrays containing user answers for each category
     * @param options ; button container for answer option buttons
     * @param cat ; the current category of questions being asked in the survey
     * @return true if answer selected, false if no answer selected
     */
    protected boolean saveAnswer(RadioGroup options, int cat, int q) {
        int btnId = options.getCheckedRadioButtonId();
        if (btnId == -1) return false;

        switch(cat) {
            case 0:
                transport_ans[q] = btnId;
                if (q == 0 && transport_ans[0] == 1) current_q += 2;  //skips follow-ups if user says no to car
                if (q == 3 && transport_ans[3] == 0) current_q += 1;  //same but for public transport
                break;
            case 1:
                //index subtractions rely on construction of questions array
                food_ans[q - num_transport_qs - 1] = btnId;
                if (q == 8 && food_ans[0] != 3) current_q += 4;  //skips follow-ups if user says no to meat
                break;
            case 2:
                housing_ans[q - num_transport_qs - num_food_qs - 2] = btnId;
                break;
            default:
                consumption_ans[q - num_transport_qs - num_food_qs - num_housing_qs - 3] = btnId;
                break;
        }
        return true;
    }

    /**
     * Computes emissions per category, given the category of questions just finished
     * @param cat the category of qs just finished
     */
    protected void computeCatEmissions(int cat) {
        switch(cat) {
            case 0:
                co2PerCategory[0] = transportEmissions();
                break;
            case 1:
                co2PerCategory[1] = foodEmissions();
                break;
            case 2:
                co2PerCategory[2] = housingEmissions();
                break;
            default:
                co2PerCategory[3] = consumptionEmissions();
                break;
        }
    }

    /**
     * Computes user's total annual carbon emissions (in kg) based on initialization survey answers
     * @return double representing total annual transport emissions (in kg)
     */
    protected double transportEmissions() {
        double totalkg = 0.0;
        double r = 0.0;
        if (transport_ans[0] != 1) {  //true corresponds to user saying "yes" to "do u use car?"
            switch(transport_ans[1]) {  //which car they drive
                case 0: r = 0.24; break;  //gas emissions rate
                case 1: r = 0.27; break;  //diesel, etc.
                case 3: r = 0.05; break;  //electric
                default: r = 0.16; break;  //hybrid or "i don't know" (default to hybrid)
            }
            switch(transport_ans[2]) {  //how much they drive
                case 0: totalkg += r * 5000; break;  //constants are distances driven
                case 1: totalkg += r * 10000; break;
                case 2: totalkg += r * 15000; break;
                case 3: totalkg += r * 20000; break;
                case 4: totalkg += r * 25000; break;
                default: totalkg += r * 35000; break;
            }
        }

        totalkg += public_transport_emissions[3][4];  //see Constants.java

        switch(transport_ans[5]) {  //short haul flight emissions
            case 0: totalkg += 225; break;
            case 1: totalkg += 600; break;
            case 2: totalkg += 1200; break;
            default: totalkg += 1800; break;
        }
        switch(transport_ans[6]) {  //long haul flight emissions
            case 0: totalkg += 825; break;
            case 1: totalkg += 2200; break;
            case 2: totalkg += 4400; break;
            default: totalkg += 6600; break;
        }

        return totalkg;
    }

    /**
     * Computes total annual user emissions from food consumption based on survey input
     * @return double representing total annual user emissions from food consumption
     */
    protected double foodEmissions() {
        double totalkg = 0.0;
        boolean meat = false;
        switch (food_ans[0]) {
            case 0: totalkg += 1000;break;
            case 1: totalkg += 500;break;
            case 2: totalkg += 1500;break;
            default: meat = true;break;
        }
        if (meat) {
            switch (food_ans[1]) {
                case 0: totalkg += 2500;break;
                case 1: totalkg += 1900;break;
                case 2: totalkg += 1300;break;
                default: break;
            }
            switch (food_ans[2]) {
                case 0: totalkg += 1450;break;
                case 1: totalkg += 860;break;
                case 2: totalkg += 450;break;
                default: break;
            }
            switch (food_ans[3]) {
                case 0: totalkg += 950;break;
                case 1: totalkg += 600;break;
                case 2: totalkg += 200;break;
                default: break;
            }
            switch (food_ans[4]) {
                case 0: totalkg += 800;break;
                case 1: totalkg += 500;break;
                case 2: totalkg += 150;break;
                default: break;
            }
        }
        switch (food_ans[5]) {
            case 0: break;
            case 1: totalkg += 23.4;break;
            case 2: totalkg += 70.2;break;
            default: totalkg += 140.4;break;
        }

        return totalkg;
    }

    /**
     * Computes total annual emissions from housing related energy use based on survey input
     * @return double representing total emissions (in kg) from housing
     */
    protected double housingEmissions() {
        double totalkg = 0.0;
        int[] i = housing_ans;
        if (i[3] != i[5]) totalkg += 233;

        System.out.println(i[0] + " " + i[1] + " " + i[2] + " " + i[4] + " " + i[3]);
        if (i[0] == 4) i[0] = 2;  //sets "other" answer option to "townhouse" (as instructed in formula spreadsheet)
        totalkg += housing_emissions[i[0]][i[1]][i[2]][i[4]][i[3]];  //home heating (i[3]; fourth question of category)
        totalkg += housing_emissions[i[0]][i[1]][i[2]][i[4]][i[5]];  //water heating (i[5]; sixth question of category)
        switch(i[6]) {  //7th question of housing category; re: renewable energy use
            case 0:
                totalkg -= 6000; break;  //primarily use renewable energy
            case 1:
                totalkg -= 4000; break;  //partially use " ... "
            default: break;  //no use of renewable energy
        }
        return totalkg;
    }

    /**
     * Computes total annual user carbon emissions related to consumption based on init survey results
     * @return double representing total annual user consumption-related emissions
     */
    protected double consumptionEmissions() {
        double totalkg = 0.0;
        int[] i = consumption_ans;  //for convenience
        switch(i[0]) {  //how often they buy clothes
            case 0: totalkg += 360; break;
            case 1: totalkg += 120; break;
            case 2: totalkg += 100; break;
            default: totalkg += 5; break;
        }
        switch(i[1]) {  //how often they recycle
            case 0: totalkg *= 0.5; break;
            case 1: totalkg *= 0.7; break;
            default: break;
        }
        switch(i[2]) {  //how many electronic devices they buy
            case 0: break;
            case 1: totalkg += 300; break;
            case 2: totalkg += 600; break;
            case 3: totalkg += 900; break;
            default: totalkg += 1200; break;
        }
        totalkg -= recycling_reduction[i[2]][i[0]][i[3]];  //how often they recycle (see Constants.java)

        return totalkg;
    }

    private double sum(double[] arr) {
        double s = 0.0;
        for (int i = 0; i < arr.length; i++) {
            s += arr[i];
        }
        return s;
    }

    private int size(String[][] arr) {
        int s = 0;
        String x = "";
        for (int i = 0; true; i++) {
            try {
                x = questions[i][0];
                s++;
            } catch (Exception e) {
                break;
            }
        }
        return s;
    }

}