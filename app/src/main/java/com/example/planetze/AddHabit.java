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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private View globalView;
    private final String[] categories = Constants.categories;
    private final String[] impacts = Constants.impacts;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
    private String userId = "QMCLRlEKD9h2Np1II1vrNU0vpxt2";
    List<List<String>> allHabits;
    List<List<String>> currentHabits;
    List<List<List<String>>> habitsByCategory;
    List<List<List<String>>> habitsByImpact;


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
        globalView = view;
        currentHabits = getCurrentHabits(userId);
        initHabitLists(view);

        attachSearchToList(view);
        initFilterSpinners(view);

        return view;
    }


    /**
     * Populates the searchable list of habits with available habits
     * Retrieves all standard habits from Firebase
     * @param view
     */
    private void initHabitLists(View view) {
        DatabaseReference habitsRef = db.getReference().child("habits");
        habitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allHabits = (List<List<String>>) snapshot.getValue();
                allHabits = removeDuplicates(allHabits, currentHabits);
                populateHabitList(view, allHabits);

                splitHabitsByCategory(view);  //initializes lists that sort habits by category/impact
                splitHabitsByImpact(view);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * related to UI; making the habit list searchable via the SearchView
     * @param view
     */
    private void attachSearchToList(View view) {
        SearchView searchView = view.findViewById(R.id.searchView);
        ListView listView = view.findViewById(R.id.listView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //filters listView results based on "query" (search string)
                ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
                if (adapter != null)
                    adapter.getFilter().filter(query);
                else Log.d("SearchView: ", "adapter is null");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //filters listView results as search string updates
                ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
                if (adapter != null)
                    adapter.getFilter().filter(newText);
                else Log.d("SearchView: ", "adapter is null");
                return false;
            }
        });
    }


    /**
     * Gets the user's currently adopted habits from Firebase
     * @param userId the user who's habits we get
     * @return
     */
    private List<List<String>> getCurrentHabits(String userId) {
        DatabaseReference currentHabitsRef = db.getReference().child("user data")
                .child(userId).child("current_habits");
        currentHabitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentHabits = (List<List<String>>) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return currentHabits;
    }


    /**
     * Removes duplicates between two lists of lists of strings.
     * Use: for removing the user's already adopted habits from the total habit list
     * (we only give them the option to add habits they haven't already added)
     * @param allHabits
     * @param currentHabits
     * @return
     */
    private List<List<String>> removeDuplicates(List<List<String>> allHabits,
                                                List<List<String>> currentHabits) {
        if (currentHabits == null || currentHabits.isEmpty()) return allHabits;
        for (int i = 0; i < currentHabits.size(); i++) {
            //if currentHabits.get(i) not in allHabits, does nothing
            allHabits.remove(currentHabits.get(i));
        }
        return allHabits;
    }


    /**
     * Splits entire collection of all possible habits down into the four categories
     * for filtering search
     * @param view
     */
    private void splitHabitsByCategory(View view) {
        //Each index of habitsByCategory corresponds to a list of habits with a particular category
        habitsByCategory = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {  //initializes the list
            habitsByCategory.add(new ArrayList<>());
        }

        for (int i = 0; i < categories.length; i++) {  //for e
            for (int j = 0; j < allHabits.size(); j++) {
                //.get(0) because habit category is stored at first index of the habit
                if (allHabits.get(j).get(0).equals(categories[i])) {
                    habitsByCategory.get(i).add(allHabits.get(j));
                }
            }
        }
        System.out.println(habitsByCategory);
    }


    /**
     * Splits the entire collection of habits into impact levels for filtering search
     * @param view
     */
    private void splitHabitsByImpact(View view) {

    }


    /**
     * Populates the list with the appropriate habits in param [habits]
     * @param view
     * @param habits the habits to populate with
     */
    private void populateHabitList(View view, List<List<String>> habits) {
        ListView list = view.findViewById(R.id.listView);  //habits populate this list container

        List<String> populatorList = new ArrayList<>();  //this will be used to populate the listview
        for (int i = 0; i < habits.size(); i++)
            populatorList.add(habits.get(i).get(1));  //1 corresponds to index of habit name
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, populatorList);
        list.setAdapter(adapter);
    }



    /**
     * initializes spinners for filtering habits
     * @param view
     */
    private void initFilterSpinners(View view) {
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        Spinner impactSpinner = view.findViewById(R.id.impactSpinner);

        //sets category filter spinner
        String[] arr = {"Select a category", "Transportation", "Food",
            "Housing", "Consumption"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        //default setting is "Select a category"
        int i = adapter.getPosition("Select a category");
        categorySpinner.setSelection(i);

        //sets impact filter spinner
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, impacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impactSpinner.setAdapter(adapter);
        i = adapter.getPosition("Select an impact level");
        impactSpinner.setSelection(i);

        setFilterSpinnerListeners(view);
    }


    /**
     * Sets the filter spinner logic (i.e. what to do when selection of spinner changed)
     * @param view
     */
    public void setFilterSpinnerListeners(View view) {
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select a category")) return;
                List<List<String>> habitList;
                switch ((String) parent.getItemAtPosition(position)) {
                    case "Select a category": return;
                    case "Transportation":
                        habitList = habitsByCategory.get(0); break;
                    case "Food":
                        habitList = habitsByCategory.get(1); break;
                    case "Housing":
                        habitList = habitsByCategory.get(2); break;
                    default:  //"Consumption"
                        habitList = habitsByCategory.get(3); break;
                }
                populateHabitList(globalView, habitList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


    }
}