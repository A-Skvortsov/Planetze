package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    private DatabaseReference calendarRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HashMap<String, Object> days = new HashMap<>();
    String date = "";

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

    /**
     * MAIN INITIALIZATION CODE HERE
     *
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eco_tracker, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        String userId = "IHdNxXO2pGXsicTlymf5HQAaUnL2";  //this should be changed to the particular logged in user once everything works
        calendarRef = db.getReference("user data")
                .child(userId).child("calendar");




        final Button calendarToggle = view.findViewById(R.id.calendarToggle);  //button to toggle calendar
        final ConstraintLayout calendarView = view.findViewById(R.id.calendarView);  //view containing calendar
        final MaterialCalendarView calendar = view.findViewById(R.id.calendar);  //calendar itself
        final TextView dateText = view.findViewById(R.id.dateText);
        final TextView yearText = view.findViewById(R.id.yearText);

        final TextView noActivities = view.findViewById(R.id.noActivities);
        final Button activitiesBtn = view.findViewById(R.id.activitiesBtn);
        final RadioGroup activities = view.findViewById(R.id.activitiesGroup);
        final Button habitsBtn = view.findViewById(R.id.habitsBtn);
        final Button addBtn = view.findViewById(R.id.addBtn);
        final Button editBtn = view.findViewById(R.id.editBtn);
        final Button delBtn = view.findViewById(R.id.delBtn);

        //initializing to current date
        Calendar today = Calendar.getInstance();
        String t = today.get(Calendar.DAY_OF_MONTH) + " " +
                months[today.get(Calendar.MONTH)];
        dateText.setText(t);
        yearText.setText(String.valueOf(today.get(Calendar.YEAR)));
        date = today.get(Calendar.YEAR) +"-"+(today.get(Calendar.MONTH)+1)+"-"
                +today.get(Calendar.DAY_OF_MONTH);


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








        //updates information when new date selected
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView view, CalendarDay day, boolean s) {
                int d = day.getDay(); int m = day.getMonth(); int y = day.getYear();
                String date1 = d + " " + months[m-1];
                dateText.setText(date1);
                yearText.setText(String.valueOf(y));
                date = y + "-" + m + "-" + d;

                /*calendarRef.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if the array exists
                        if (dataSnapshot.exists()) {
                            // Convert the snapshot into a List
                            days = (HashMap<String, Object>) dataSnapshot.getValue();
                            updateDisplay(activities, date2, noActivities);
                        } else {
                            System.out.println("FirebaseData: Array does not exist for this user.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("FirebaseData" + "Error: " + databaseError.getMessage());
                    }
                }); //calendarRef.addListenerForSingleValueEvent(listener);
                //fetchSnapshot(calendarRef, listener);*/
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


        //add activities
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passes date parameter so activity is added to current date
                loadFragment(new AddActivity(date));
            }
        });
        //edit activities
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddActivity());
            }
        });
        //delete activities
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }


    /**
     * Used to perform a one-time ping to the firebase to retrieve most up-to-date data
     * @param ref
     * @param listener
     */
    public void fetchSnapshot(DatabaseReference ref, ValueEventListener listener) {
        calendarRef.addListenerForSingleValueEvent(listener);
    }

    public void updateDisplay(RadioGroup activities, String date, TextView emptyMsg) {
        activities.clearCheck(); activities.removeAllViews();
        emptyMsg.setVisibility(View.INVISIBLE);
        RadioButton btn;
        System.out.println(days.keySet());
        if (days.containsKey(date)) {  //if true, will display logged activities for selected day
            //stores a list containing lists representing the logged activities of the day
            List<List<Object>> day = (List<List<Object>>) days.get(date);
            for (int i = 0; i < day.size(); i++) {
                btn = new RadioButton(getContext());
                String t = day.get(i).get(0) + ": " + day.get(i).get(1);
                btn.setText(t);
                activities.addView(btn);
            }
        } else {
            //set default ("no activities today")
            emptyMsg.setVisibility(View.VISIBLE);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}