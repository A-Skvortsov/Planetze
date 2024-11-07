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

public class Init_Survey extends AppCompatActivity {
    int current_cat = 0;  //0-transportation,1-food, 2-housing,3-consumption
    int current_q = 0;  //index of current question
    Double[] co2PerCategory = {0.0, 0.0, 0.0, 0.0};
    final String[] categories = {"Transportation", "Food", "Housing", "Consumption"};
    final String[][] questions = {{"Do you own or regularly use a car?", "Yes", "No"},
                    {"What type of car do you drive?", "Gasoline", "Diesel", "Hybrid", "Electric", "I don't know"},
                    {"How many kilometers/miles do you drive per year?", "Up to 5,000 km", "5,000–10,000 km", "10,000–15,000 km", "15,000–20,000 km", "20,000–25,000 km", "More than 25,000 km"},
                    {"How often do you use public transportation?", "Never", "Occasionally (1-2 times/week)", "Frequently (3-4 times/week)", "Always (5+ times/week)"},
                    {"How much time do you spend on public transport per week?", "Under 1 hour", "1-3 hours", "3-5 hours", "5-10 hours", "More than 10 hours"},
                    {"How many short-haul flights have you taken in the past year?", "None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
                    {"How many long-haul flights have you taken in the past year?", "None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
                    {"-"},
                    {"What best describes your diet?", "Vegetarian", "Vegan", "Pescatarian", "Meat-based"},
                    {"How often do you eat beef?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
                    {"How often do you eat pork?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
                    {"How often do you eat chicken?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
                    {"How often do you eat fish?", "Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
                    {"How often do you waste food or throw away uneaten leftovers?", "Never", "Rarely", "Occasionally", "Frequently"},
                    {"-"},
                    {"What type of home do you live in?", "Detached house", "Semi-detached house", "Townhouse", "Condo/Apartment", "Other"},
                    {"How many people live in your household?", "1", "2", "3-4", "5 or more"},
                    {"What is the size of your home?", "Under 1000 sq. ft.", "1000-2000 sq. ft.", "Over 2000 sq. ft."},
                    {"What type of energy do you use to heat your home?", "Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
                    {"What is your average monthly electricity bill?", "Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"},
                    {"What type of energy do you use to heat water?", "Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
                    {"Do you use any renewable energy sources for electricity or heating?", "Yes, primarily", "Yes, partially", "No"},
                    {"-"},
                    {"How often do you buy new clothes?", "Monthly", "Quarterly", "Annually", "Rarely"},
                    {"Do you buy second-hand or eco-friendly products?", "Yes, regularly", "Yes, occasionally", "No"},
                    {"How many electronic devices have you purchased in the past year?", "None", "1", "2", "3 or more"},
                    {"How often do you recycle?", "Never", "Occasionally", "Frequently", "Always"}};
    String[][] answers = {{"Yes", "No"}, {}};  //for each q, array of choices (answers)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init_survey);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Button iteratorBtn = findViewById(R.id.iteratorButton);
        final TextView category = findViewById(R.id.category);
        final TextView question = findViewById(R.id.question);
        final RadioGroup options = findViewById(R.id.options);
            //nested code is initializing survey at first question
            category.setText(categories[current_cat]);
            question.setText(questions[current_q][0]);
            for (int i = 1; i < questions[current_q].length; i++) {
                RadioButton btn = new RadioButton(Init_Survey.this);
                btn.setId(i);
                btn.setText(questions[current_q][i]);
                options.addView(btn);
            }
        //method to iterate through questions
        iteratorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                current_q++;  //iterates to next q
                if (questions[current_q][0].equals("-")) {
                    current_q++;
                    current_cat++;
                    category.setText(categories[current_cat]);
                }
                question.setText(questions[current_q][0]);  //displays q

                options.removeAllViews();  //deleting all previous options
                RadioButton btn;  //loading all answer options for the current q
                for (int i = 1; i < questions[current_q].length; i++) {
                    btn = new RadioButton(Init_Survey.this);
                    //configure button settings here
                    btn.setId(i);
                    btn.setText(questions[current_q][i]);
                    options.addView(btn);
                }

                //seeing which option was selected (should be done first before
                //deleting previous question options)

            }
        });
    }


    protected double transportEmissions(int[] arr) {
        return 0.0;
    }
    //sample method that computes total co2 emissions from food.
    //should be called by button iterator once user answers all food emissions questions.
    protected double foodEmissions(int[] arr) {
        double totalkg = 0.0;
        boolean meat = false;
        switch (arr[0]) {
            case 0: totalkg += 1000;break;
            case 1: totalkg += 500;break;
            case 2: totalkg += 1500;break;
            default: meat = true;break;
        }
        if (meat) {
            switch (arr[1]) {
                case 0: totalkg += 2500;break;
                case 1: totalkg += 1900;break;
                case 2: totalkg += 1300;break;
                default: break;
            }
            switch (arr[2]) {
                case 0: totalkg += 1450;break;
                case 1: totalkg += 860;break;
                case 2: totalkg += 450;break;
                default: break;
            }
            switch (arr[3]) {
                case 0: totalkg += 950;break;
                case 1: totalkg += 600;break;
                case 2: totalkg += 200;break;
                default: break;
            }
            switch (arr[4]) {
                case 0: totalkg += 800;break;
                case 1: totalkg += 500;break;
                case 2: totalkg += 150;break;
                default: break;
            }
        }
        switch (arr[5]) {
            case 0: break;
            case 1: totalkg += 23.4;break;
            case 2: totalkg += 70.2;break;
            default: totalkg += 140.4;break;
        }

        return totalkg;
    }

}