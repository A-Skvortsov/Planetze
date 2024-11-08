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

import utilities.Constants;

public class Init_Survey extends AppCompatActivity {
    int current_cat = 0;  //0-transportation,1-food, 2-housing,3-consumption
    int current_q = 0;  //index of current question
    double[] co2PerCategory = {0.0, 0.0, 0.0, 0.0};
    final String[] categories = Constants.categories;
    final String[][] questions = Constants.questions;
    final int transport_qs = Constants.transport_qs;
    final int food_qs = Constants.food_qs;
    final int housing_qs = Constants.housing_qs;
    final int consumption_qs = Constants.consumption_qs;
    final double[][][][][] housing_answers = Constants.housing_answers;
    //elements of arrays below specify which answer option is selected for a particular question
    int[] transport_ans = new int[transport_qs];  //7 qs in transportation category
    int[] food_ans = new int [food_qs];
    int[] housing_ans = new int[housing_qs];
    int[] consumption_ans = new int[consumption_qs];

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
            //nested code initializes survey at first question
            category.setText(categories[current_cat]);
            question.setText(questions[current_q][0]);
            for (int i = 1; i < questions[current_q].length; i++) {
                RadioButton btn = new RadioButton(Init_Survey.this);
                btn.setId(i);
                btn.setText(questions[current_q][i]);
                options.addView(btn);
            }

        //method to iterate through questions on button click
        iterator_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if no answer selected, should return immediately and prompt user to answer
                if (!saveAnswer(options, current_cat, current_q)) {  //saves user's answer to prev question
                    please_answer1.setVisibility(View.VISIBLE); please_answer2.setVisibility(View.VISIBLE);
                    return;
                }
                please_answer1.setVisibility(View.INVISIBLE); please_answer2.setVisibility(View.INVISIBLE);
                //pass saved user answer to computation

                current_q++;  //iterates to next q
                if (questions[current_q][0].equals("-")) {  //iter'n to next category if necessary
                    computeCatEmissions(current_cat);  //computes emissions for the finished category
                    current_q++; current_cat++;
                    category.setText(categories[current_cat]);
                }
                question.setText(questions[current_q][0]);  //displays q

                options.removeAllViews(); options.clearCheck();  //remove previous answer options
                for (int i = 1; i < questions[current_q].length; i++) { //loading answer options for the current q
                    RadioButton btn = new RadioButton(Init_Survey.this);
                    //configure button settings here
                    btn.setId(i);
                    btn.setText(questions[current_q][i]);
                    options.addView(btn);
                }
            }
        });
    }


    /**
     * Updates the global one-hot vectors containing user answers for each category
     * @param options
     * @param cat
     * @return true if answer selected, false if no answer selected
     */
    protected boolean saveAnswer(RadioGroup options, int cat, int q) {
        int btnId = options.getCheckedRadioButtonId();
        if (btnId == -1) return false;

        switch(cat) {
            case 0:
                transport_ans[q] = btnId;
                break;
            case 1:
                //index subtractions rely on construction of questions array
                food_ans[q - transport_qs - 1] = btnId;
                break;
            case 2:
                housing_ans[q - transport_qs - food_qs - 2] = btnId;
                break;
            default:
                consumption_ans[q - transport_qs - food_qs - housing_qs - 3] = btnId;
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


    protected double transportEmissions() {
        return 0.0;
    }
    //sample method that computes total co2 emissions from food.
    //should be called by button iterator once user answers all food emissions questions.
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

    protected double housingEmissions() {
        return 0.0;
    }

    protected double consumptionEmissions() {
        return 0.0;
    }

}