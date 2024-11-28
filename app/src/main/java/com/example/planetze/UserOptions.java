package com.example.planetze;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.planetze.Login.LoginView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOptions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatabaseReference userRef;
    private FirebaseDatabase db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View settingView;

    public UserOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserOptions.
     */
    // TODO: Rename and change types and number of parameters
    public static UserOptions newInstance(String param1, String param2) {
        UserOptions fragment = new UserOptions();
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
        View view = inflater.inflate(R.layout.fragment_user_options, container, false);
        settingView = inflater.inflate(R.layout.fragment_user_settings, container, false);


        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");

        String userID = UserData.getUserID(getContext());

        Button userSettingsButton = view.findViewById(R.id.button);

        UserSettingsFragment setup = new UserSettingsFragment();
        //setup.setButtons(settingView);

        Button logoutButton = setup.setButtons(settingView);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout();
                loadFragment(new LoginView());

            }
        });

        userSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout();
                popup2(view);

            }
        });


        return view;
    }

    private void logout() {
        UserData.logout(getContext());
        loadFragment(new LoginView());
    }

    private void popup() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_user_settings);
        dialog.show();
    }

    private void popup2(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onMenuClick(item);
            }
        });
        popupMenu.show();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean onMenuClick(MenuItem item) {
        //android.nonFinalResIds=false
        if (item.getItemId() == R.id.logout) {
            loadFragment(new LoginView());
        }

        return false;
    }

    /*

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

     */
}