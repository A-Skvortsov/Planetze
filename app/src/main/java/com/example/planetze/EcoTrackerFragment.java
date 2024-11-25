package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.renderscript.Sampler;
import android.util.Log;
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
import java.util.concurrent.Semaphore;

import utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EcoTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EcoTrackerFragment extends Fragment {

    final String[] months = Constants.months;
    private FirebaseDatabase db;
    private static DatabaseReference calendarRef;  //this is static so that we can call fetchSnapshot()
            //from addActivity fragment when returning to ecotrackerfragment in order to update
            //ecotracker activity info upon return
    private static ValueEventListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HashMap<String, Object> days = new HashMap<>();  //used to store the days of a user calendar
    List<List<String>> acts = new ArrayList<>();  //used to store the activities of a day
    List<String> activityToEdit = new ArrayList<>();  //used to store the activity selected for editing
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

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        String userId = "QMCLRlEKD9h2Np1II1vrNU0vpxt2";  //this should be changed to the particular logged in user once everything works
        calendarRef = db.getReference("user data")
                .child(userId).child("calendar");
        Log.d("Firebase", "Reference Path: " + calendarRef);  //for debugging

        final Button calendarToggle = view.findViewById(R.id.calendarToggle);  //button to toggle calendar
        final ConstraintLayout calendarView = view.findViewById(R.id.calendarView);  //view containing calendar
        final MaterialCalendarView calendar = view.findViewById(R.id.calendar);  //calendar itself
        final TextView dateText = view.findViewById(R.id.dateText);
        final TextView yearText = view.findViewById(R.id.yearText);
        final TextView dailyTotal = view.findViewById(R.id.dailyTotal);

        final TextView noActivities = view.findViewById(R.id.noActivities);
        final Button activitiesBtn = view.findViewById(R.id.activitiesBtn);
        final RadioGroup activities = view.findViewById(R.id.activitiesGroup);
        final Button habitsBtn = view.findViewById(R.id.habitsBtn);
        final Button addBtn = view.findViewById(R.id.addBtn);
        final Button editBtn = view.findViewById(R.id.editBtn);
        final Button delBtn = view.findViewById(R.id.delBtn);
        final TextView issuePrompt1 = view.findViewById(R.id.issuePrompt1);
        final TextView issuePrompt2 = view.findViewById(R.id.issuePrompt2);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the array exists
                if (dataSnapshot.exists()) {
                    // Convert the snapshot into a List
                    days = (HashMap<String, Object>) dataSnapshot.getValue();
                    Log.d("Firebase", "data loaded successfully" + days);
                    updateDisplay(activities, noActivities, dailyTotal);
                } else {
                    Log.d("Firebase", "Array does not exist for this user.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        };

        //initializes everything
        initUI(listener, dateText, yearText, activities, noActivities, dailyTotal);

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
                fetchSnapshot();
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
                //puts these away in case they were brought up, no need for them
                issuePrompt1.setVisibility(View.INVISIBLE);
                issuePrompt2.setVisibility(View.INVISIBLE);

                //passes date parameter so activity is added to current date
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                NavController navController = NavHostFragment.findNavController(requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fragment));
                navController.navigate(R.id.AddActivity, bundle);
                //loadFragment(new AddActivity());
            }
        });
        //edit activities
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //puts these away in case they were brought up, no need for them
                issuePrompt1.setVisibility(View.INVISIBLE);
                issuePrompt2.setVisibility(View.INVISIBLE);

                if (!activitySelected(activities, issuePrompt1, issuePrompt2)) return;
                int id = activities.getCheckedRadioButtonId();

                startEdit(id, userId);  //this retrieves the selected activity for editing and
                //boots up AddActivity fragment in edit mode via
                //"loadFragment(new AddActivity(date, activityToEdit));".
                //Note that upon clicking "save" btn in addactivity, the activity (as stored in firebase)
                //will be updated using updateChildren() method of firebase rtdb
            }
        });
        //delete activities
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activitySelected(activities, issuePrompt1, issuePrompt2)) return;
                int id = activities.getCheckedRadioButtonId();

                //code below removes associated activity in firebase rtdb
                //get the date of the activity we want to remove
                DatabaseReference dateRef = db.getReference("user data")
                        .child(userId)
                        .child("calendar")
                        .child(date);
                //get the activities of the date
                dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if the array exists
                        if (dataSnapshot.exists()) {
                            // Convert the snapshot into a List
                            acts = (List<List<String>>) dataSnapshot.getValue();
                            Log.d("Firebase", "data loaded successfully" + acts);
                            delFromFirebase(dateRef, acts, id, noActivities);  //delete the activity
                            //next line polls firebase for update and updates ui via call to updateDisplay
                            //in "listener"
                            fetchSnapshot();
                        } else {
                            Log.d("Firebase", "Array does not exist for this user.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                    }
                });
            }
        });

        return view;
    }


    /**
     * Deletes an activity from the firebase rtdb.
     * @param dateRef the date in which the activity is logged
     * @param acts the activities of the aforementioned date
     * @param id the id of the activity we want to delete
     */
    public void delFromFirebase(DatabaseReference dateRef, List<List<String>> acts, int id,
                                TextView noActivities) {
        acts.remove(id);  //removes the activity at position [id]
        if (acts.isEmpty())   //if no activities left on this date, remove the date from firebase
            dateRef.removeValue();
        else dateRef.setValue(acts);  //otherwise, set the activity list of date to be updated list
    }


    /**
     * Used to perform a one-time ping to the firebase to retrieve most up-to-date data
     */
    public static void fetchSnapshot() {
        calendarRef.addListenerForSingleValueEvent(listener);
    }

    /**
     * Initializes eco tracker UI
     * @param listener ValueEventListener to listen for firebase updates
     * @param d TextView for displaying current date in form "11 Nov" or "21 Sep"
     * @param y TextView for displaying current year in standard form
     * @param activities RadioGroup in which the radiobutton activities are displayed
     * @param noActivities  TextView with message "no activities logged yet for today"
     * @param dailyTotal TextView displaying the day's total emissions
     */
    public void initUI(ValueEventListener listener, TextView d, TextView y,
                       RadioGroup activities, TextView noActivities, TextView dailyTotal) {
        //pings Firebase to show activities of current date
        fetchSnapshot();
        updateDisplay(activities, noActivities, dailyTotal);

        //displays current date
        Calendar today = Calendar.getInstance();
        String t = today.get(Calendar.DAY_OF_MONTH) + " " +
                months[today.get(Calendar.MONTH)];
        d.setText(t);
        y.setText(String.valueOf(today.get(Calendar.YEAR)));
        date = today.get(Calendar.YEAR) +"-"+(today.get(Calendar.MONTH)+1)+"-"
                +today.get(Calendar.DAY_OF_MONTH);
    }

    public void updateDisplay(RadioGroup activities, TextView emptyMsg, TextView dailyTotal) {
        activities.clearCheck(); activities.removeAllViews();
        emptyMsg.setVisibility(View.INVISIBLE);

        double emissions = 0.0;
        RadioButton btn;
        if (days.containsKey(date)) {  //if true, will display logged activities for selected day
            //stores a list containing lists representing the logged activities of the day
            List<List<Object>> day = (List<List<Object>>) days.get(date);
            for (int i = 0; i < day.size(); i++) {
                btn = new RadioButton(getContext());
                btn.setId(i);  //this is used for delete and edit activity features
                String t = (Math.round(Double.parseDouble((String) day.get(i).get(2)) * 10) / 10.0)
                        + "kg CO2: " + day.get(i).get(1);  //the rounding prevents weird floating point stuff
                btn.setText(t);
                activities.addView(btn);
                emissions += Double.parseDouble(String.valueOf(day.get(i).get(2)));
            }
            emissions = Math.round(emissions * 10) / 10.0;  //round to 1 decimal
            String s = emissions + "kg of CO2 emitted";
            dailyTotal.setText(s);
        } else {
            //set default ("no activities today")
            emptyMsg.setVisibility(View.VISIBLE);
            dailyTotal.setText("0.0 kg of CO2 Emitted");
        }
    }


    private boolean activitySelected(RadioGroup activities, TextView issuePrompt1,
                                     TextView issuePrompt2) {
        if (activities.getChildCount() == 0) return false;  //if no activities, do nothing
        int id = activities.getCheckedRadioButtonId();
        if (id == -1) {
            issuePrompt1.setVisibility(View.VISIBLE);
            issuePrompt2.setVisibility(View.VISIBLE);
            return false;
        }
        issuePrompt1.setVisibility(View.INVISIBLE);
        issuePrompt2.setVisibility(View.INVISIBLE);
        return true;
    }

    /**
     * From the firebase rtdb, retrieves the activity we want to edit
     * @param id index of the activity as in the firebase
     * @param userId id of the user who's using the app currently (as in the firebase)
     * @return
     */
    public void startEdit(int id, String userId) {
        DatabaseReference dateRef = db.getReference("user data")
                .child(userId)
                .child("calendar")
                .child(date);  //index of the activity in the list of activities for the date
        //get the activities of the date
        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the array exists
                if (dataSnapshot.exists()) {
                    //gets date snapshot (list of activity (lists of strings))
                    //gets the list (activity) at position id (as wanted)
                    activityToEdit = ((List<List<String>>) dataSnapshot.getValue()).get(id);
                    //boot addActivity in edit mode, passing activityToEdit
                    Bundle bundle = new Bundle();
                    bundle.putString("date", date);
                    bundle.putStringArrayList("activityToEdit", (ArrayList<String>) activityToEdit);
                    bundle.putInt("id", id);
                    NavController navController = NavHostFragment.findNavController(requireActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.fragment));
                    navController.navigate(R.id.AddActivity, bundle);

                    //loadFragment(new AddActivity(date, activityToEdit, id));
                    Log.d("Firebase", "data loaded successfully1" + activityToEdit);

                } else {
                    Log.d("Firebase", "Array does not exist for this user.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}