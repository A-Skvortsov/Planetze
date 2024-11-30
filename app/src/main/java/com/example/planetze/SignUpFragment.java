package com.example.planetze;


import static android.app.Activity.RESULT_OK;
import static java.lang.Character.isLetter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;



public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword,signupConfirmPassword, fullName;
    private Button signupButton;

    private TextView loginRedirectText, inputError, signinLink;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatabaseReference userRef;
    private FirebaseDatabase db;
    ActivityResultLauncher<Intent> launcher;
    private Button googleSignUp;

    public SignUpFragment() {
        // Required empty public constructor
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
        googleSignUp = view.findViewById(R.id.signUpWithGoogleButton);


        Activity activity = getActivity();
        setSignUpLauncher();

        signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new LoginView());
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

        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient client = GoogleSignIn.getClient(activity, options);

                Intent intent = client.getSignInIntent();
                launcher.launch(intent);
            }
        });

        return view;

    }

    private void setMessage(String msg) {
        inputError.setText(msg);
        if (!msg.trim().isEmpty()) {
            inputError.setTextSize(18);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,20,20,20);
            inputError.setLayoutParams(params);
        }
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
            setMessage("Password has to be at least 7 character long");
            return false;
        }
        else if (!cond2) {
            setMessage("Password has to contains numbers");
            return false;
        }
        else if (cond3) {
            setMessage("Password cannot contain a space");
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



    private boolean notEmpty(String email, String name) {
        boolean cond1 = name == null || name.trim().isEmpty();
        boolean cond2 = email == null ||email.trim().isEmpty();
        if (cond1) {
            setMessage("Name cannot be empty");
            return false;
        }
        else if (cond2) {
            setMessage("Email cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validPass(String pass, String conf_pass, String name) {
        boolean cond4 = conf_pass.equals(pass);
        if (!validPassword(pass)) {
            return false;
        }
        else if (!cond4) {
            setMessage("Confirm Password has to match with password");
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

                if(notEmpty(email, name)) {
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
                if (equalsEmail && notEmpty(email, name)) {
                    setMessage("Email already accociated with an account");
                }
                else if (notEmpty(email, name) && validPass(pass,confirm_pass, name)){
                    createAccountOnFirebase(email, pass, name);
                }
                //inputError.setText(errorMsg);


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
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String id = auth.getCurrentUser().getUid();
                            userRef.child(id+"/name").setValue(name);
                            userRef.child(id+"/email").setValue(email);
                            setDefaultSettings(id);

                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(activity, "there was an error in sending verification email", Toast.LENGTH_LONG).show();
                                    }
                                    loadFragment(new ResendConfirmFragment());
                                }
                            });

                        } else {
                            Toast.makeText(activity, "Signup Failed please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDefaultSettings(String userID) {
        userRef.child(userID+"/is_new_user").setValue(true);
        userRef.child(userID+"/settings/stay_logged_on").setValue(false);
        userRef.child(userID+"/settings/interpolate_emissions_data").setValue(false);
        userRef.child(userID+"/calendar/0000-00-00/0").setValue(0);

    }

    public void setSignUpLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //loadFragment(new HomeFragment());
                                            Intent intent = new Intent(getActivity(), com.example.planetze.HomeActivity.class);
                                            startActivity(intent);
                                        }
                                        else {

                                        }
                                    }
                                });

                            } catch (ApiException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

}