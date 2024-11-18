package com.example.planetze;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import utilities.Constants;

public class EcoTrackerActivity extends AppCompatActivity {

    final String[] months = Constants.months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eco_tracker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Button calendarToggle = findViewById(R.id.calendarToggle);  //button to toggle calendar
        final ConstraintLayout calendarView = findViewById(R.id.calendarView);  //view containing calendar
        final MaterialCalendarView calendar = findViewById(R.id.calendar);  //calendar itself
        final TextView dateText = findViewById(R.id.dateText);
        final TextView yearText = findViewById(R.id.yearText);

        final Button activities = findViewById(R.id.activities);
        final Button habits = findViewById(R.id.habits);

        //Toggles calendar view
        calendarToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.INVISIBLE);
                    calendarToggle.setText("View Calendar");
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    calendarToggle.setText("Select Day");
                }

            }
        });

        //Changes presented information when new date selected
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView view, CalendarDay day, boolean s) {
                int d = day.getDay();
                int m = day.getMonth();
                String date = String.valueOf(d) + " " + months[m-1];
                dateText.setText(date);
                yearText.setText(String.valueOf(day.getYear()));
            }
        });


        //show user activities
        activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //should clear the linearlayout of any views and display activity data as stored
                //in firebase
            }
        });
        //show user habits
        habits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}