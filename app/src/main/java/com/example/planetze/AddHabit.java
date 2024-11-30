package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilities.Constants;
import utilities.UserData;

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
    private String date;
    private View globalView;
    private final String[] categories = Constants.categories;
    private final String[] impacts = Constants.impacts;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
    private String userId;
    private HashMap<String, Object> calendar;
    List<List<String>> allHabits;
    List<List<String>> currentHabits;
    List<List<String>> recommendedHabits;
    List<List<List<String>>> habitsByCategory;
    List<List<List<String>>> habitsByImpact;
    String selectedHabit = "";


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

        userId = UserData.getUserID(getContext());
        //sets user's calendar right away
        DatabaseReference calendarRef = db.getReference().child("user data")
                .child(userId).child("calendar");
        calendarRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calendar = (HashMap<String, Object>) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Bundle args = getArguments();  //needed solely for when returning to EcoTracker
        //date = args.getString("date");

        userId = UserData.getUserID(getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_habit, container, false);
        globalView = view;
        currentHabits = getCurrentHabits(userId);
        initHabitLists(view);

        attachSearchToList(view);
        initFilterSpinners(view);
        setHabitListListeners(view);

        return view;
    }





    //HABIT RETRIEVAL LOGIC

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
                splitHabitsByCategory(view);  //initializes lists that sort habits by category/impact
                splitHabitsByImpact(view);
                allHabits = removeDuplicates(allHabits, currentHabits);  //for display purposes
                populateHabitList(view, allHabits);

                Button allHabitsBtn = globalView.findViewById(R.id.allHabitsBtn);
                initButtons(view);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    }


    /**
     * Splits the entire collection of habits into impact levels for filtering search
     * @param view
     */
    private void splitHabitsByImpact(View view) {
        habitsByImpact = new ArrayList<>();
        for (int i = 0; i < impacts.length - 1; i++) {  //initializes the list
            habitsByImpact.add(new ArrayList<>());
        }

        int impact = 0;
        for (int i = 0; i < allHabits.size(); i++) {
            impact = getImpactLevel(allHabits.get(i));
            if (impact <= 25) {  //for hard coded constants 25, 50, see "impact" array in Constants.java
                habitsByImpact.get(0).add(allHabits.get(i));
            } else if (impact <= 50) {
                habitsByImpact.get(1).add(allHabits.get(i));
            } else {
                habitsByImpact.get(2).add(allHabits.get(i));
            }
        }
    }


    /**
     * Populates the list with the appropriate habits in param [habits]
     * @param view
     * @param habits the habits to populate with
     */
    private void populateHabitList(View view, List<List<String>> habits) {
        ListView list = view.findViewById(R.id.listView);  //habits populate this list container

        List<String> populatorList = new ArrayList<>();  //this will be used to populate the listview
        if (habits != null) {
            for (int i = 0; i < habits.size(); i++)
                populatorList.add(habits.get(i).get(1));  //1 corresponds to index of habit name
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, populatorList);
            list.setAdapter(adapter);
        } else {
            list.setAdapter(null);
        }
        //set this to default to prevent adopting/quitting habits that may no longer appear in the list
        selectedHabit = "";
    }


    private List<List<String>> setDisplayHabits(Spinner categorySpinner, Spinner impactSpinner) {
        List<List<String>> habitList;
        //either filters user's habits or all habits
        if (viewingUserHabits()) habitList = clone(currentHabits);
        else habitList = clone(allHabits);

        String category = (String) categorySpinner.getSelectedItem();
        String impactLevel = (String) impactSpinner.getSelectedItem();

        switch (category) {  //reduces habitList by category selection
            case "Select a category": break;
            case "Transportation":
                habitList.removeIf(n -> !habitsByCategory.get(0).contains(n)); break;
            case "Food":
                habitList.removeIf(n -> !habitsByCategory.get(1).contains(n)); break;
            case "Housing":
                habitList.removeIf(n -> !habitsByCategory.get(2).contains(n)); break;
            default:  //"Consumption"
                habitList.removeIf(n -> !habitsByCategory.get(3).contains(n)); break;
        }

        if (!impactLevel.equals("Select an impact level")) {
            for (int i = 1; i < impacts.length; i++) {
                if (impactLevel.equals(impacts[i])) {
                    final int x = i;
                    //predicate argument translates to; if habitList(index) not in
                    //habitsByImpact(particular impact level), then remove index from habitList
                    habitList.removeIf(n -> !habitsByImpact.get(x-1).contains(n));
                    break;
                }
            }
        }

        return habitList;
    }


    /**
     * Initializes buttons and button listener for "all habits" and "your habits" buttons
     * and the "adopt"/"quit" button
     * @param view
     */
    private void initButtons(View view) {
        setActionBtnListener(view);
        Button allHabitsBtn = view.findViewById(R.id.allHabitsBtn);
        Button yourHabitsBtn = view.findViewById(R.id.yourHabitsBtn);
        Button recommendedBtn = view.findViewById(R.id.recommendedBtn);
        Button backBtn = view.findViewById(R.id.backBtn);

        allHabitsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button allHabitsBtn = globalView.findViewById(R.id.allHabitsBtn);
                Button yourHabitsBtn = globalView.findViewById(R.id.yourHabitsBtn);
                Button recommendedBtn = globalView.findViewById(R.id.recommendedBtn);
                Button actionBtn = globalView.findViewById(R.id.actionBtn);
                allHabitsBtn.setSelected(true);
                yourHabitsBtn.setSelected(false);
                recommendedBtn.setSelected(false);
                String s = "Adopt";
                actionBtn.setText(s);
                displayRecommendedHabitsTexts(false);

                //resets filter
                Spinner categorySpinner = globalView.findViewById(R.id.categorySpinner);
                categorySpinner.setSelection(0);
                Spinner impactSpinner = globalView.findViewById(R.id.impactSpinner);
                impactSpinner.setSelection(0);

                //resets displayed habits list to all habits
                populateHabitList(globalView, allHabits);
            }
        });

        yourHabitsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button allHabitsBtn = globalView.findViewById(R.id.allHabitsBtn);
                Button yourHabitsBtn = globalView.findViewById(R.id.yourHabitsBtn);
                Button recommendedBtn = globalView.findViewById(R.id.recommendedBtn);
                Button actionBtn = globalView.findViewById(R.id.actionBtn);
                yourHabitsBtn.setSelected(true);
                allHabitsBtn.setSelected(false);
                recommendedBtn.setSelected(false);
                String s = "Quit";
                actionBtn.setText(s);
                displayRecommendedHabitsTexts(false);

                Spinner categorySpinner = globalView.findViewById(R.id.categorySpinner);
                categorySpinner.setSelection(0);
                Spinner impactSpinner = globalView.findViewById(R.id.impactSpinner);
                impactSpinner.setSelection(0);

                //resets displayed habits list to user's current habits
                populateHabitList(globalView, currentHabits);
            }
        });

        recommendedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button allHabitsBtn = globalView.findViewById(R.id.allHabitsBtn);
                Button yourHabitsBtn = globalView.findViewById(R.id.yourHabitsBtn);
                Button recommendedBtn = globalView.findViewById(R.id.recommendedBtn);
                Button actionBtn = globalView.findViewById(R.id.actionBtn);
                recommendedBtn.setSelected(true);
                yourHabitsBtn.setSelected(false);
                allHabitsBtn.setSelected(false);
                String s = "Adopt";
                actionBtn.setText(s);
                displayRecommendedHabitsTexts(true);

                Spinner categorySpinner = globalView.findViewById(R.id.categorySpinner);
                categorySpinner.setSelection(0);
                Spinner impactSpinner = globalView.findViewById(R.id.impactSpinner);
                impactSpinner.setSelection(0);

                //resets displayed habits list to recommended habits
                computeRecommendedHabits();
                populateHabitList(globalView, recommendedHabits);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToEcoTracker();
            }
        });

    }

    private void computeRecommendedHabits() {
        Spinner categorySpinner = globalView.findViewById(R.id.categorySpinner);
        Spinner impactSpinner = globalView.findViewById(R.id.impactSpinner);
        String[] orderOfHighestEmissions = {""};
                //should be getOrderOfHighestEmissions(); or getOrderOfActivityFrequency();

        recommendedHabits = null;
        for (int i = 0; i < orderOfHighestEmissions.length; i++) {
            if (hasCategory(allHabits, orderOfHighestEmissions[i])) {
                categorySpinner.setSelection(i);
                recommendedHabits = setDisplayHabits(categorySpinner, impactSpinner);
                return;
            }
        }
    }




    //SEARCH/FILTER SEARCH LOGIC

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

        setApplyFilterBtnListener(view);
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
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                } else Log.d("SearchView: ", "adapter is null");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //filters listView results as search string updates
                ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                } else Log.d("SearchView: ", "adapter is null");
                return false;
            }
        });
    }


    private void setApplyFilterBtnListener(View view) {
        Button btn = view.findViewById(R.id.applyFilterBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner categorySpinner = globalView.findViewById(R.id.categorySpinner);
                Spinner impactSpinner = globalView.findViewById(R.id.impactSpinner);

                List<List<String>> habits = setDisplayHabits(categorySpinner, impactSpinner);
                populateHabitList(globalView, habits);
            }
        });
    }





    //SELECT HABIT TO ADOPT/QUIT LOGIC

    private boolean viewingUserHabits() {
        Button btn = globalView.findViewById(R.id.yourHabitsBtn);
        return btn.isSelected();
    }

    private void setHabitListListeners(View view) {
        ListView list = view.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedHabit = (String) parent.getItemAtPosition(position);
            }
        });
    }

    private void setActionBtnListener(View view) {
        Button actionBtn = view.findViewById(R.id.actionBtn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button actionBtn = globalView.findViewById(R.id.actionBtn);
                if (actionBtn.getText().equals("Adopt")) {
                    adoptHabit(selectedHabit);
                } else {
                    quitHabit(selectedHabit);
                }
            }
        });
    }


    /**
     * Adopt a habit function
     * @param habitToAdopt
     */
    private void adoptHabit(String habitToAdopt) {
        ListView listView = globalView.findViewById(R.id.listView);
        //should be viewing all habits. User habits are already adopted ones
        if (viewingUserHabits()) return;

        for (int i = 0; i < allHabits.size(); i++) {
            //second index of a habit in allHabits is the habit name
            if (habitToAdopt.equals(allHabits.get(i).get(1))) {
                if (currentHabits == null) currentHabits = new ArrayList<>();
                currentHabits.add(new ArrayList<>(allHabits.get(i)));  //add to current habits
                allHabits.remove(i);  //remove from allHabits

                //update filter lists to be based on updated allHabits
                splitHabitsByCategory(globalView);
                splitHabitsByImpact(globalView);
                break;
            }
        }

        writeUsersHabitsToFirebase();
        Toast.makeText(getContext(), "New Habit Adopted", Toast.LENGTH_SHORT).show();
        returnToEcoTracker();
    }


    private void quitHabit(String habitToQuit) {
        ListView listView = globalView.findViewById(R.id.listView);
        //Should be viewing user habits. Those are the adopted ones that we can now quit
        if (!viewingUserHabits()) return;

        for (int i = 0; i < currentHabits.size(); i++) {
            if (habitToQuit.equals(currentHabits.get(i).get(1))) {
                allHabits.add(new ArrayList<>(currentHabits.get(i)));  //add to all habits
                currentHabits.remove(i);  //remove from user habits

                //update filter lists to be based on updated allHabits
                splitHabitsByCategory(globalView);
                splitHabitsByImpact(globalView);
                break;
            }
        }

        writeUsersHabitsToFirebase();
        Toast.makeText(getContext(), "Habit Quit", Toast.LENGTH_SHORT).show();
        returnToEcoTracker();
    }


    private void writeUsersHabitsToFirebase() {
        DatabaseReference currentHabitsRef = db.getReference().child("user data")
                .child(userId).child("current_habits");

        currentHabitsRef.setValue(currentHabits)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Data written successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to write data: " + e.getMessage());
                });
    }

    private void returnToEcoTracker() {
        EcoTrackerFragment.fetchSnapshot();
        /*Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putBoolean("habitsToggled", true);

        NavController navController = NavHostFragment.findNavController(requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.fragment));
        navController.navigate(R.id.EcoTrackerFragment, bundle);*/
        getParentFragmentManager().popBackStack();
    }






    //HELPER/MISCELLANEOUS FUNCTIONS BELOW


    /*private String[] getOrderOfActivityFrequency() {
        int transportation = 0, food = 0, housing = 0, consumption = 0;
        List<String> past29days = AddActivity.getPast29Days(date);
        for (int i = 0; i < past29days.size(); i++) {
            if (calendar.containsKey(past29days.get(i))) {
                List<List<String>> activities = (List<List<String>>) calendar.get(past29days.get(i));
                for (int j = 0; j < activities.size(); j++) {
                    switch (activities.get(j).get(0)) {
                        case "Transportation": transportation++; break;
                        case "Food": food++; break;
                        case "Housing": housing++; break;
                        default: consumption++; break;
                    }
                }
            }
        }

        //List<String>

    }*/


    private boolean hasCategory(List<List<String>> habits, String category) {
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).get(0).equals(category)) return true;
        }
        return false;
    }

    private void displayRecommendedHabitsTexts(boolean b) {
        TextView recommended = globalView.findViewById(R.id.recommendedDescription);
        TextView noRecs = globalView.findViewById(R.id.noRecs);

        if (!b) {
            recommended.setVisibility(View.INVISIBLE);
            noRecs.setVisibility(View.INVISIBLE);
            return;
        }
        recommended.setVisibility(View.VISIBLE);
        noRecs.setVisibility(View.INVISIBLE);
        if (recommendedHabits == null) {
            noRecs.setVisibility(View.VISIBLE);
        }
    }


    /**
     * checks if a particular item is in a ListView's current adapter
     * @param listView
     * @param selectedItem
     * @return
     */
    private boolean contains(ListView listView, String selectedItem) {
        android.widget.ListAdapter adapter = listView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (selectedItem.equals(adapter.getItem(i))) return true;
        }
        return false;
    }


    /**
     * Computes impact level (in kg of CO2) of a particular habit
     * @param habit the habit to compute the impact level for
     * @return
     */
    private int getImpactLevel(List<String> habit) {
        String str = habit.get(2);  //3rd index always contains impact level
        int i = Math.abs(Integer.valueOf(str));  //we just use positive values for comparison **
        return i;

        //** in reality, all habits have a negative value for impact level, representing
        //lowered CO2 emissions. Habits are meant to be good deeds that lower your CO2
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
     * Clones a list of lists of string. Need this since lists of strings are mutable, so
     * doing [List<List<String>>] = [other List<List<String>>] would cause memory issues.
     * @param list
     * @return
     */
    private List<List<String>> clone(List<List<String>> list) {
        List<List<String>> clonedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //related to mutability and deep vs shallow cloning
            clonedList.add(new ArrayList<>(list.get(i)));
        }

        return clonedList;
    }
}