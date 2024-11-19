package com.example.planetze;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import utilities.Constants;

public class EcoTrackerActivity extends AppCompatActivity {

    final String[] months = Constants.months;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;


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

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        auth = FirebaseAuth.getInstance();
        userRef = db.getReference("user data")
                .child("IHdNxXO2pGXsicTlymf5HQAaUnL2")
                .child("test_data");

        final Button calendarToggle = findViewById(R.id.calendarToggle);  //button to toggle calendar
        final ConstraintLayout calendarView = findViewById(R.id.calendarView);  //view containing calendar
        final MaterialCalendarView calendar = findViewById(R.id.calendar);  //calendar itself
        final TextView dateText = findViewById(R.id.dateText);
        final TextView yearText = findViewById(R.id.yearText);

        final Button activitiesBtn = findViewById(R.id.activitiesBtn);
        final RadioGroup activities = findViewById(R.id.activitiesGroup);
        final Button habitsBtn = findViewById(R.id.habitsBtn);

        //initializing to current date
        Calendar today = Calendar.getInstance();
        String t = String.valueOf(today.get(Calendar.DAY_OF_MONTH)) + " " +
                String.valueOf(months[today.get(Calendar.MONTH)]);
        dateText.setText(t);
        yearText.setText(String.valueOf(today.get(Calendar.YEAR)));


        //Toggles calendar view
        calendarToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.INVISIBLE);
                    calendarToggle.setText("View Calendar");
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    calendarToggle.setText("Select");
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

                updateDisplay(activities);
            }
        });


        //show user activities
        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //should clear the linearlayout of any views and display activity data as stored
                //in firebase
            }
        });
        //show user habits
        habitsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    final private String[] testArr = {"activity 1", "activity 2", "activity 3"};
    public void updateDisplay(RadioGroup activities) {
        activities.clearCheck(); activities.removeAllViews();
        RadioButton btn;
        for (int i = 0; i < testArr.length; i++) {
            btn = new RadioButton(EcoTrackerActivity.this);
            btn.setId(i);
            btn.setText(testArr[i]);
            activities.addView(btn);
        }
    }

}