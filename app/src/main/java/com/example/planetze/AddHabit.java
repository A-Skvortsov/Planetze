package com.example.planetze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHabit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHabit extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final String[] categories = Constants.categories;
    private final String[] impacts = Constants.impacts;
    private String userId = "QMCLRlEKD9h2Np1II1vrNU0vpxt2";


    public AddHabit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHabit.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHabit newInstance(String param1, String param2) {
        AddHabit fragment = new AddHabit();
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
        View view = inflater.inflate(R.layout.fragment_add_habit, container, false);

        populateHabitList(view);
        initFilterSpinners(view);


        return view;
    }


    /**
     * Populates the searchable list of habits with available habits
     * @param view
     */
    private void populateHabitList(View view) {
        List<List<String>> allHabits = getAllHabitsFromFirebase();
        List<List<String>> currentHabits = getCurrentHabits(userId);

        allHabits = removeDuplicates(allHabits, currentHabits);

    }

    private List<List<String>> getAllHabitsFromFirebase() {

        return new ArrayList<>();
    }

    private List<List<String>> getCurrentHabits(String userId) {
        return new ArrayList<>();
    }

    private List<List<String>> removeDuplicates(List<List<String>> allHabits,
                                                List<List<String>> currentHabits) {
        if (currentHabits == null || currentHabits.size() == 0) return allHabits;
        for (int i = 0; i < currentHabits.size(); i++) {
            if (allHabits.contains(currentHabits.get(i))) {
                allHabits.remove(allHabits.indexOf(currentHabits.get(i)));
            }
        }
        return allHabits;
    }


    /**
     * initializes spinners for filtering habits
     * @param view
     */
    private void initFilterSpinners(View view) {
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        Spinner impactSpinner = view.findViewById(R.id.impactSpinner);

        //sets category filter spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        adapter.add("Select a category");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        //default setting is "Select a category"
        int i = adapter.getPosition("Select a category");
        categorySpinner.setSelection(i);


        //sets impact filter spinner
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, impacts);
        adapter.add("Select an impact level");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impactSpinner.setAdapter(adapter);
        i = adapter.getPosition("Select an imapct level");
        impactSpinner.setSelection(i);
    }
}