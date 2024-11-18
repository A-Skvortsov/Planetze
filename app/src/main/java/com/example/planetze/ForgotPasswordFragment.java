package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private ArrayList<String> emailArray;
    private FirebaseDatabase db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText emailInput;
    private String email;

    private String messagetext;
    private TextView message;

    private Button login, sendlink;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        auth = FirebaseAuth.getInstance();

        login = view.findViewById(R.id.returnButton);
        sendlink = view.findViewById(R.id.resetPasswordButton);
        emailInput = view.findViewById(R.id.emailInput);
        message = view.findViewById(R.id.msg);

        messagetext = " ";

        emailArray = new ArrayList<String>();

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadFragment(new LogInFragment());
            }
        });

        sendlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String link = FirebaseAuth.getInstance().generatePasswordResetLink(email);
                email = emailInput.getText().toString().trim();
                if (!accountNotExists(email)) {
                    auth.sendPasswordResetEmail(email);
                    messagetext = "Password reset link sent! Please check your email";
                }
                else {
                    messagetext = "There isn't an account accociated with that email";
                    message.setText(messagetext);
                }


                //sendCustomEmail(email, displayName, link);

            }
        });

        return view;


    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean accountNotExists(String email) {
        getEmails();
        if (emailArray == null || emailArray.size() == 0) {
            return true;
        }
        int index = emailArray.indexOf(email);
        if (index == -1) {
            return true;
        }
        return false;
    }


    private void getEmails() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                emailArray = new ArrayList<String>();
                for(DataSnapshot user:users.getChildren()) {
                    if (user.hasChild("email")) {
                        String email = user.child("email").getValue(String.class).toString().trim();
                        emailArray.add(email);
                    }

                }

            }
        });
    }
}
