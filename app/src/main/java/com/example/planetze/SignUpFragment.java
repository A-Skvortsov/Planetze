package com.example.planetze;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileInputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword,signupConfirmPassword, fullName;
    private Button signupButton;
    private TextView loginRedirectText, signinLink;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

        signinLink = view.findViewById(R.id.signInLink);

        Activity activity = getActivity();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("minki jiang hello");
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String confirm_pass = signupConfirmPassword.getText().toString().trim();
                String name = fullName.getText().toString().trim();
                // Set up Validation Logic

                /*

                if (email.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                } else if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                } else if (!pass.equals(confirm_pass)) {
                    signupConfirmPassword.setError("Confirm password does not match with password");
                }
                else {

                }

                 */


                if (validprofile(email, pass, confirm_pass)) {
                    auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = task.getResult().getUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(activity,"Name update ",Toast.LENGTH_LONG).show();
                                                }else
                                                    Toast.makeText(activity,"Name update Failed, try again",Toast.LENGTH_LONG).show();
                                            }
                                        });

                                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(activity, "Signup Successful, check email for verification.", Toast.LENGTH_LONG).show();
                                                    //startActivity(new Intent(activity, MainActivity.class));
                                                    loadFragment(new SignUpFragment());
                                                }else{
                                                    Toast.makeText(activity, task.getException().getMessage() , Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(activity, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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
        boolean cond1 = password.length() >= 8;
        String result = password.replaceAll("\\d", "");
        boolean cond2 = result.length() != password.length();

        return cond1 && cond2;
    }

    private boolean validprofile(String email, String pass, String conf_pass) {

        boolean cond1 = email.length() != 0;
        boolean cond2 = pass.length() >= 7;
        boolean cond3 = conf_pass.equals(pass);

        if (!cond1) {
            signupEmail.setError("Email cannot be empty");
            return false;
        }
        else if (!cond2) {
            signupPassword.setError("Password has to be at least 7 character long");
            return false;
        }
        else if (!cond3) {
            signupConfirmPassword.setError("Confirm Password has to match with password");
            return false;
        }
        return true;
    }

}