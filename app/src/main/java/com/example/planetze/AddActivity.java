package com.example.planetze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
    public final String[] activityCats = Constants.activityCats;
    public final String[][] activities = Constants.activities;

    public AddActivity() {
        // Required empty public constructor
    }

    public AddActivity(String cat) {
        //initializeSpinners() first then set spinner to given arguments
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
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
                if (cat.equals(msg1) || act.equals(msg2)) {
                    //true if user has not selected category/activity
                    //return & display msg prompting user to select
                }
                //if (appropriate input not given)
                //return & display msg prompting user to give required input

                //save activity to firebase
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
                displayInputs(box1, box2, selectedActivity);
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
    public void displayInputs(RadioGroup box1, RadioGroup box2, String act) {
        box1.removeAllViews(); box2.removeAllViews();
        box1.setVisibility(View.INVISIBLE); box2.setVisibility(View.INVISIBLE);
        if (act.equals(msg2)) return;  //if user selected the default "Select an Activity"

        String[] inpt1 = {}; String[] inpt2 = {};
        if (act.equals("Drive personal vehicle")) {
            inpt1 = new String[]{"< 15km", "15-40km", "40-80km", "80-200km", "> 200km"};  //distance driven
            inpt2 = new String[]{"Gasoline", "Diesel", "Hybrid", "Electric"};  //optionally: change vehicle type

        } else if (act.equals("Take public transportation")) {
            inpt1 = new String[]{"Bus", "Train", "Subway"};  //type of public transport
            inpt2 = new String[]{"< 0.5 hours", "0.5-1 hours", "1-2 hours", "> 2 hours"};  //time spent on public transport

        } else if (act.equals("Cycling/Walking")) {
            inpt1 = new String[]{"< 2km", "2-4km", "4-8km", "> 8km"};  //distance cycled/walked

        } else if (act.equals("Flight (< 1,500km)") || act.equals("Flight (> 1,500km)")) {
            inpt1 = new String[]{"1", "2", "3-4", "> 5"};  //number of flights taken
        }else if (act.equals("Meal")) {
            inpt1 = new String[]{"Beef", "Pork", "Chicken", "Fish", "Plant-based"};  //type of meal (beef pork chicken fish plant based)
            inpt2 = new String[]{"1", "2", "3-4", "> 5"};  //number of servings

        }else if (act.equals("Buy new clothes")) {
            inpt1 = new String[]{"1", "2", "3-4", "> 5"};  //number of clothing items purchased

        }else if (act.equals("Buy electronics")) {
            inpt1 = new String[]{"Smartphone", "Laptop/Computer", "T/V"};  //type of electronic device
            inpt2 = new String[]{"1", "2", "3-4", "> 5"};  //number of devices purchased

        }else if (act.equals("Other purchases")) {
            inpt1 = new String[]{"Furniture", "Appliances"};  //type of purchase
            inpt2 = new String[]{"1", "2", "3-4", "> 5"};  //# of purchases

        }else if (act.equals("Electricity") || act.equals("Gas") || act.equals("Water")) {
            inpt1 = new String[]{"Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"};  //bill amount $
        }
        box1.setVisibility(View.VISIBLE);
        if (inpt2.length != 0) box2.setVisibility(View.VISIBLE);


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



}//end of class