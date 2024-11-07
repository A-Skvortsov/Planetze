package com.example.planetze;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Init_Survey extends AppCompatActivity {
    String[] categories = {"Transportation",
    "Food", "Housing", "Consumption"};
    String[] questions = {"Do you own or regularly use a car?",
                    "What type of car do you drive?",
                    "How many kilometers/miles do you drive per year?",
                    "How often do you use public transportation?",
                    "How much time do you spend on public transport per week?",
                    "How many short-haul flights have you taken in the past year?",
                    "How many long-haul flights have you taken in the past year?",
                    "-",
                    "What best describes your diet?",
                    "How often do you eat beef?",
                    "How often do you eat pork?",
                    "How often do you eat chicken?",
                    "How often do you eat fish?",
                    "How often do you waste food or throw away uneaten leftovers?",
                    "-",
                    "What type of home do you live in?",
                    "How many people live in your household?",
                    "What is the size of your home?",
                    "What type of energy do you use to heat your home?",
                    "What is your average monthly electricity bill?",
                    "What type of energy do you use to heat water?",
                    "Do you use any renewable energy sources for electricity or heating?",
                    "-",
                    "How often do you buy new clothes?",
                    "Do you buy second-hand or eco-friendly products?",
                    "How many electronic devices have you purchased in the past year?",
                    "How often do you recycle?"};
    String[][] answers = {{"Yes", "No"}, {}};  //for each q, array of choices (answers)
    int current_q = 0;  //index of current question
    int current_cat = 0;  //index of current category

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

        //not sure why, Android docs put "final" on the button declaration. Will keep for now
        final Button iteratorBtn = findViewById(R.id.iteratorButton);
        final TextView category = findViewById(R.id.category);
        final TextView question = findViewById(R.id.question);
            category.setText(categories[current_cat]);
            question.setText(questions[current_q]);
        iteratorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (current_q > questions.length || current_cat > categories.length)
                    return;

                current_q++;
                if (questions[current_q].equals("-")) {
                    current_q++;
                    current_cat++;
                    category.setText(categories[current_cat]);
                }
                question.setText(questions[current_q]);
            }
        });
    }


}