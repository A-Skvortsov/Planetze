package com.example.planetze;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogInFragment extends Fragment  {

    private EditText loginEmail, loginPass;
    private FirebaseAuth auth;

    private Button login;

    private FirebaseDatabase db;
    private DatabaseReference userRef;

    private TextView inputError, forgotpass;
    private String errorMsg;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");

        TextView signUpLink = view.findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(v -> loadFragment(new SignUpFragment()));

        auth = FirebaseAuth.getInstance();
        loginEmail = view.findViewById(R.id.emailInput);
        loginPass = view.findViewById(R.id.passwordInput);
        login = view.findViewById(R.id.logInButton);
        inputError = view.findViewById(R.id.error);
        forgotpass = view.findViewById(R.id.forgotPasswordLink);



        userRef = db.getReference("user data");

        Activity activity = getActivity();

        errorMsg = " ";

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmail.getText().toString().trim();
                String pass = loginPass.getText().toString().trim();

                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        loadFragment(new HomeFragment());
                                        //eco guage
                                        // survey if just registered
                                    }
                                    else {
                                        errorMsg = "Account needs to be verified";
                                        inputError.setText(errorMsg);
                                    }

                                } else {
                                    //Toast.makeText(activity, "signup failed", Toast.LENGTH_SHORT).show();
                                    errorMsg = "invalid email or password";
                                    inputError.setText(errorMsg);

                                }
                            }
                        });


            }
        });

        //dosent work with school email

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //auth.sendPasswordResetEmail(email);
                loadFragment(new ForgotPasswordFragment());
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



}
