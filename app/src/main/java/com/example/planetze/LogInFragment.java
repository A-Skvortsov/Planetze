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


        /*

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] currentUser = new String[2];

                ArrayList<String[]> userArray = new ArrayList<String[]>();

                for(DataSnapshot user:dataSnapshot.getChildren()) {
                    if (user.hasChild("email") && user.hasChild("password")) {
                        String email = user.child("email").getValue().toString().trim();
                        String pass =  user.child("password").getValue().toString().trim();
                        currentUser[0] = email; currentUser[1] = pass;
                        userArray.add(currentUser);
                    }
                }
                boolean valid = false;
                errorMsg = " ";

                System.out.println("Size: " + userArray.size());

                String[] user = {email, pass};
                if (userArray != null && userArray.contains(user)) {
                    valid = true;
                }
                else {
                    errorMsg = "invalid email or password";
                }

                inputError.setText(errorMsg);
                if (valid) {
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_LONG).show();
                    loadFragment(new HomeFragment());
                }



                boolean valid = isValidLogin(email, pass);
                inputError.setText(errorMsg);
                if (valid) {
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_LONG).show();
                    loadFragment(new HomeFragment());
                }





                //System.out.println("Size: " + userArray.size());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "Unable to fetch user info", databaseError.toException());
            }
        };

        userRef.addValueEventListener(listener);

        */



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //userRef.child("test/testvalue").setValue(0);
                //userRef.child("test/testvalue").setValue(1);

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

    private boolean isValidLogin(String email, String pass) {
        /*
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //getUserList(task);
                DataSnapshot users = task.getResult();
                ArrayList<String[]> userArray = new ArrayList<String[]>();
                UserData.reset();
                String[] currentUser = new String[2];
                for(DataSnapshot user:users.getChildren()) {

                    if (user.hasChild("email") && user.hasChild("password")) {

                        String email = user.child("email").getValue().toString().trim();
                        String pass =  user.child("password").getValue().toString().trim();
                        currentUser[0] = email; currentUser[1] = pass;
                        userArray.add(currentUser);
                    }
                }
            }
        });

         */

        /*
        System.out.println("Size: " + userArray.size());
        if (userArray == null) {
            errorMsg = "invalid email or password";
            return false;
        }

        String[] user = {email, pass};
        if (userArray.contains(user)) {
            return true;
        }
        errorMsg = "invalid email or password";
        return false;

         */
        return false;
    }

    /*
    private void getUsers() {

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                getUserList(task);
            }
        });
    }

     */


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    private void getUserList(Task<DataSnapshot> task) {
        DataSnapshot users = task.getResult();
        ArrayList<String[]> userArray = new ArrayList<String[]>();
        UserData.reset();
        String[] currentUser = new String[2];
        for(DataSnapshot user:users.getChildren()) {

            if (user.hasChild("email") && user.hasChild("password")) {

                String email = user.child("email").getValue().toString().trim();
                String pass =  user.child("password").getValue().toString().trim();
                currentUser[0] = email; currentUser[1] = pass;
                userArray.add(currentUser);
            }
        }
        //System.out.println("size of users: " + UserData.size());
    }

     */


}
