package com.example.planetze;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EcoTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EcoTrackerFragment extends Fragment {

    final String[] months = Constants.months;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EcoTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EcoTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EcoTrackerFragment newInstance(String param1, String param2) {
        EcoTrackerFragment fragment = new EcoTrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eco_tracker, container, false);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        auth = FirebaseAuth.getInstance();
        userRef = db.getReference("user data")
                .child("IHdNxXO2pGXsicTlymf5HQAaUnL2")
                .child("test_data");

        final Button calendarToggle = view.findViewById(R.id.calendarToggle);  //button to toggle calendar
        final ConstraintLayout calendarView = view.findViewById(R.id.calendarView);  //view containing calendar
        final MaterialCalendarView calendar = view.findViewById(R.id.calendar);  //calendar itself
        final TextView dateText = view.findViewById(R.id.dateText);
        final TextView yearText = view.findViewById(R.id.yearText);

        final Button activitiesBtn = view.findViewById(R.id.activitiesBtn);
        final RadioGroup activities = view.findViewById(R.id.activitiesGroup);
        final Button habitsBtn = view.findViewById(R.id.habitsBtn);
        final Button addBtn = view.findViewById(R.id.addBtn);

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddActivity());
            }
        });


        return view;
    }






    final private String[] testArr = {"activity 1", "activity 2", "activity 3"};
    public void updateDisplay(RadioGroup activities) {
        activities.clearCheck(); activities.removeAllViews();
        RadioButton btn;
        for (int i = 0; i < testArr.length; i++) {
            btn = new RadioButton(getContext());
            btn.setId(i);
            btn.setText(testArr[i]);
            activities.addView(btn);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}