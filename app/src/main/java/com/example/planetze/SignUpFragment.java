package com.example.planetze;


import static java.lang.Character.isLetter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword,signupConfirmPassword, fullName;
    private Button signupButton;

    private TextView loginRedirectText, inputError, signinLink;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String errorMsg;

    private ArrayList<String> emailArray;
    private DatabaseReference userRef;
    private FirebaseDatabase db;

    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        auth = FirebaseAuth.getInstance();
        signupEmail = view.findViewById(R.id.emailInput);
        signupPassword = view.findViewById(R.id.passwordInput);
        signupConfirmPassword = view.findViewById(R.id.confirmPasswordInput);
        signupButton = view.findViewById(R.id.registerButton);
        fullName = view.findViewById(R.id.nameInput);

        inputError = view.findViewById(R.id.error);

        signinLink = view.findViewById(R.id.signInLink);


        Activity activity = getActivity();

        emailArray = new ArrayList<String>();



        signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new LogInFragment());
            }
        });

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();

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

    private boolean validPassword(String password) {
        boolean cond1 = password.length() >= 7;
        boolean cond2 = hasNumber(password);
        boolean cond3 = hasSpace(password);

        if (!cond1) {
            errorMsg = "Password has to be at least 7 character long";
            return false;
        }
        else if (!cond2) {
            errorMsg = "Password has to contains numbers";
            return false;
        }
        else if (cond3) {
            errorMsg = "Password cannot contain a space";
            return false;
        }

        return true;
    }

    private boolean hasNumber(String string) {
        ArrayList<Character> digits = new ArrayList<Character>();
        digits.add('0');digits.add('1');digits.add('2');digits.add('3');digits.add('4');
        digits.add('5');digits.add('6');digits.add('7');digits.add('8');digits.add('9');
        for (int i = 0; i < string.length(); i++) {
            if (digits.contains(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSpace(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                return true;
            }
        }
        return false;
    }

    /*
    private boolean hasSpecialCharcter(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (isLetter(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

     */



    private boolean valid1(String email, String name) {
        boolean cond1 = name.length() != 0;
        boolean cond2 = email.length() != 0;
        if (!cond1) {
            errorMsg = "Name cannot be empty";
            return false;
        }
        else if (!cond2) {
            errorMsg = "Email cannot be empty";
            return false;
        }
        return true;
    }

    private boolean valid2(String pass, String conf_pass, String name) {
        boolean cond4 = conf_pass.equals(pass);
        if (!validPassword(pass)) {
            return false;
        }
        else if (!cond4) {
            errorMsg = "Confirm Password has to match with password";
            return false;
        }
        return true;
    }

    private void createAccount() {

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String confirm_pass = signupConfirmPassword.getText().toString().trim();
                String name = fullName.getText().toString().trim();

                boolean equalsEmail = false;

                if(valid1(email, name)) {
                    DataSnapshot users = task.getResult();
                    for(DataSnapshot user:users.getChildren()) {
                        String currentemail = " ";
                        if (user.hasChild("email")) {
                            currentemail = user.child("email").getValue(String.class).toString().trim();
                        }
                        if (currentemail.equals(email)) {
                            equalsEmail = true;
                        }

                    }
                }
                if (equalsEmail && valid1(email, name)) {
                    errorMsg = "Email already accociated with an account";
                }
                else if (valid2(pass,confirm_pass, name)){
                    createAccountOnFirebase(email, pass, name);
                }
                inputError.setText(errorMsg);


            }
        });

        /*
        else if (!cond6) {
            //signupEmail.setError("Not a valid Email");
            errorMsg = "Not a valid Email";
            return false;
        }

         */

    }

    private void createAccountOnFirebase(String email, String pass, String name) {
        Activity activity = getActivity();
        errorMsg = " ";
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String id = auth.getCurrentUser().getUid();
                            userRef.child(id+"/name").setValue(name);
                            userRef.child(id+"/email").setValue(email);
                            //userRef.child(id+"/password").setValue(pass);
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(activity, "Signup Successful, check email for verification.", Toast.LENGTH_LONG).show();
                                        loadFragment(new LogInFragment());
                                    }else{
                                        Toast.makeText(activity, "there was an error in sending verification email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(activity, "Signup Failed please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}