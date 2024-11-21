package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final String msg1 = "Select a Category";
    private final String msg2 = "Select an Activity";
    private final String catactPrompt = "Please select a category and activity";
    private final String inputPrompt = "Please select an option for input";
    public final String[] activityCats = Constants.activityCats;
    public final String[][] activities = Constants.activities;
    public String date = "test string";

    public AddActivity() {
        // Required empty public constructor
    }
    public AddActivity(String d) {
        date = d;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 eco tracker's currently selected calendar date in the form yyy
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static AddActivity newInstance(String param1, String param2) {
        AddActivity fragment = new AddActivity();
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
        View view = inflater.inflate(R.layout.fragment_add_activity, container, false);

        final Spinner selectCat = view.findViewById(R.id.selectCat);
        final Spinner selectActivity = view.findViewById(R.id.selectActivity);
        final Button backBtn = view.findViewById(R.id.backBtn);
        final Button saveBtn = view.findViewById(R.id.saveBtn);
        final RadioGroup box1 = view.findViewById(R.id.inputBox1);
        final RadioGroup box2 = view.findViewById(R.id.inputBox2);
        final TextView txt1 = view.findViewById(R.id.input1Text);
        final TextView txt2 = view.findViewById(R.id.input2Text);
        final TextView issuePrompt1 = view.findViewById(R.id.issuePrompt1);
        final TextView issuePrompt2 = view.findViewById(R.id.issuePrompt2);

        //"back" & "save" button implementations
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = (String) selectCat.getSelectedItem();
                String act = (String) selectActivity.getSelectedItem();
                int input1 = box1.getCheckedRadioButtonId();
                int input2 = box2.getCheckedRadioButtonId();
                if (cat.equals(msg1) || act.equals(msg2)) {
                    issuePrompt2.setText(catactPrompt);
                    issuePrompt1.setVisibility(View.VISIBLE);
                    issuePrompt2.setVisibility(View.VISIBLE);
                    return;
                } else if (input1 == -1 || (input2 == -1 && box2.getVisibility() == View.VISIBLE)) {
                    issuePrompt2.setText(inputPrompt);
                    issuePrompt1.setVisibility(View.VISIBLE);
                    issuePrompt2.setVisibility(View.VISIBLE);
                    return;
                }

                //compute values and save in a list
                List<String> activity = saveActivity(cat, act, input1, input2);
                writeToFirebase(date, activity);  //write list to firebase
                //update on eco tracker should happen automatically in eco tracker
                getParentFragmentManager().popBackStack();  //returns to eco tracker
            }
        });


        //spinner initializations
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, activityCats);  //used to load spinner
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCat.setAdapter(adapter1);  //loads spinner
        int init = adapter1.getPosition(msg1);  //initializes spinner to default country
        selectCat.setSelection(init);

        //initialize second spinner based on first spinner selection
        String[] x = {msg2};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, x);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectActivity.setAdapter(adapter2);
        init = adapter1.getPosition(msg2);  //initializes spinner to default country
        selectActivity.setSelection(init);


        //listener for category selection (sets activity spinner accordingly)
        selectCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                for (int i = 1; i < activityCats.length; i++) {
                    if (selectedItem.equals(activityCats[i])) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, activities[i-1]);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectActivity.setAdapter(adapter);
                        int x = adapter1.getPosition(msg2);  //initializes spinner to default country
                        selectActivity.setSelection(x);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //listener for activity selection (sets input boxes accordingly)
        selectActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedActivity = (String) parent.getItemAtPosition(position);
                displayInputs(box1, box2, txt1, txt2, selectedActivity);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Displays input options for user once activity has been selected
     * @param box1 the radiogroup for first input
     * @param box2 the radiogroup for second input (if necessary)
     * @param act the selected activity
     */
    public void displayInputs(RadioGroup box1, RadioGroup box2,
                              TextView txt1, TextView txt2, String act) {
        box1.removeAllViews(); box2.removeAllViews();
        box1.setVisibility(View.INVISIBLE); box2.setVisibility(View.INVISIBLE);
        txt1.setVisibility(View.INVISIBLE); txt2.setVisibility(View.INVISIBLE);
        if (act.equals(msg2)) return;  //if user selected the default "Select an Activity"

        String[] inpt1 = {}; String[] inpt2 = {};
        String text1 = ""; String text2 = "";
        if (act.equals("Drive personal vehicle")) {
            inpt1 = new String[]{"< 15km", "15-40km", "40-80km", "80-200km", "> 200km"};  //distance driven
            inpt2 = new String[]{"Gasoline", "Diesel", "Hybrid", "Electric"};  //optionally: change vehicle type
            text1 = "Distance driven"; text2 = "Vehicle type (default: "+ "[default]" + ")";
        } else if (act.equals("Take public transportation")) {
            inpt1 = new String[]{"Bus", "Train", "Subway"};  //type of public transport
            inpt2 = new String[]{"< 0.5 hours", "0.5-1 hours", "1-2 hours", "> 2 hours"};  //time spent on public transport
            text1 = "Type of public transport"; text2 = "Time spent on public transport";
        } else if (act.equals("Cycling/Walking")) {
            inpt1 = new String[]{"< 2km", "2-4km", "4-8km", "> 8km"};  //distance cycled/walked
            text1 = "Distance cycled/walked";
        } else if (act.equals("Flight (< 1,500km)") || act.equals("Flight (> 1,500km)")) {
            inpt1 = new String[]{"1", "2", "3-4", "> 5"};  //number of flights taken
            text1 = "Number of flights taken";
        }else if (act.equals("Meal")) {
            inpt1 = new String[]{"Beef", "Pork", "Chicken", "Fish", "Plant-based"};  //type of meal (beef pork chicken fish plant based)
            inpt2 = new String[]{"1", "2", "3-4", "> 5"};  //number of servings
            text1 = "Type of meal"; text2 = "Number of servings";
        }else if (act.equals("Buy new clothes")) {
            inpt1 = new String[]{"1", "2", "3-4", "> 5"};  //number of clothing items purchased
            text1 = "Number of clothing items purchased";
        }else if (act.equals("Buy electronics")) {
            inpt1 = new String[]{"Smartphone", "Laptop/Computer", "T/V"};  //type of electronic device
            inpt2 = new String[]{"1", "2", "3-4", "> 5"};  //number of devices purchased
            text1 = "Type of electronic device"; text2 = "Number of devices purchased";
        }else if (act.equals("Other purchases")) {
            inpt1 = new String[]{"Furniture", "Appliances"};  //type of purchase
            inpt2 = new String[]{"1", "2", "3-4", "> 5"};  //# of purchases
            text1 = "Type of purchase"; text2 = "Number of purchases";
        }else if (act.equals("Electricity") || act.equals("Gas") || act.equals("Water")) {
            inpt1 = new String[]{"Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"};  //bill amount $
            text1 = "Bill amount";
        }

        box1.setVisibility(View.VISIBLE);  //set input option boxes visible
        txt1.setVisibility(View.VISIBLE);
        txt1.setText(text1);
        if (inpt2.length != 0) {
            box2.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            txt2.setText(text2);
        }

        //load input option boxes with appropriate answer options
        RadioButton btn;
        for (int i = 0; i < inpt1.length; i++) {
            btn = new RadioButton(getContext());
            btn.setId(i);
            btn.setText(inpt1[i]);
            box1.addView(btn);
        }
        for (int i = 0 ; i < inpt2.length; i++) {
            btn = new RadioButton(getContext());
            btn.setId(i + 100);  //to avoid same id as btns of first radiogroup (which would cause
                                //issues in the xml file for this fragment)
            btn.setText(inpt2[i]);
            box2.addView(btn);
        }
    }


    public List<String> saveActivity(String cat, String act, int input1, int input2) {
        List<String> activity = new ArrayList<>();
        activity.add(cat); activity.add(act);
        int activityEmissions = 0;
        activity.add(String.valueOf(activityEmissions));
        /*switch (cat) {
            case "Transportation":
                switch (act) {
                    case "Drive personal vehicle":
                        break;
                    case "Take public transportation":
                        break;
                    case "Cycling/Walking":
                        break;
                    case "Flight (< 1,500km)":
                        break;
                    default: //"Flight (> 1,500km)"
                        break;
                }
                break;
            case "Food":
                //here, act must be "meal"
                break;
            case "Consumption":
                switch (act) {
                    case "Buy new clothes":
                        break;
                    case "Buy electronics":
                        break;
                    default:  //"Other purchases"
                        break;
                }
                break;
            default:
                switch (act) {
                    case "Electricity":
                        break;
                    case "Gas":
                        break;
                    default:  //"Water"
                        break;
                }
                break;
        }*/





        return activity;
    }


    public void writeToFirebase(String date, List<String> activity) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        String userId = "IHdNxXO2pGXsicTlymf5HQAaUnL2";  //this should be changed to the particular logged in user once everything works
        DatabaseReference calendarRef = db.getReference("user data")
                .child(userId).child("calendar");
        
        /*code in this nest only writes and overrides whatever you're writing to. Does not account
        for existing data (i.e. does not "update" the database, just overrides it with new data;
        not what we want)*//*
        Map<String, Object> map = new HashMap<>();
        map.put(date, activity);
        calendarRef.updateChildren(map)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Data written successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to write data: " + e.getMessage());
                });*/

        calendarRef.child(date).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                // Step 1: Look at the data in Firebase for this date
                List<Object> a = currentData.getValue(new GenericTypeIndicator<List<Object>>() {});
                if (a == null)  //true if the current date does not already exist in the database
                    a = new ArrayList<>();  //creates new arraylist for the date
                //otherwise, a is nonnull (date exists), so we just add the new activity
                a.add(activity);

                currentData.setValue(a);  //writes to firebase
                return Transaction.success(currentData);  //required for Transactions
            }

            @Override  //gives logcat message for success/failure
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (error != null) {
                    Log.e("Firebase", "Error updating data: " + error.getMessage());
                } else {
                    Log.d("Firebase", "Data updated successfully!");
                }
            }
        });

    }


}//end of class