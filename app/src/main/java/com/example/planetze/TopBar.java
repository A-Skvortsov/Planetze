package com.example.planetze;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopBar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopBar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button userSettings;

    public TopBar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopBar.
     */
    // TODO: Rename and change types and number of parameters
    public static TopBar newInstance(String param1, String param2) {
        TopBar fragment = new TopBar();
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
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);
        userSettings = view.findViewById(R.id.usersetting_button);
        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout();
                popup(view);

            }
        });

        return view;
    }

    private void popup(View view) {
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
        if (item.getItemId() == R.id.logout) {
            loadFragment(new LoginView());
        }
        else if (item.getItemId() == R.id.settings) {
            loadFragment(new SettingsFragment());
        }

        return false;
    }
}