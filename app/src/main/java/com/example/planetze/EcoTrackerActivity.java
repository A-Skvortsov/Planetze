package com.example.planetze;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class EcoTrackerActivity extends AppCompatActivity {

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
        final FrameLayout calendarView = findViewById(R.id.calendarView);  //view containing calendar
        final MaterialCalendarView calendar = findViewById(R.id.calendar);  //calendar itself
        final TextView txt = findViewById(R.id.testtxt);

        //Toggles calendar view
        calendarToggle.setOnClickListener(new View.OnClickListener() {
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
                if (day.getMonth() == 11) txt.setText("november");
                else txt.setText("not november");
            }
        });

    }
}