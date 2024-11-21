package com.example.planetze.Signup;

import static android.app.Activity.RESULT_OK;
import static android.provider.Settings.System.getString;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.planetze.LogInFragment;
import com.example.planetze.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class signupModel {

    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private FirebaseDatabase db;

    private GoogleSignInClient client;

    private ActivityResultLauncher<Intent> launcher;

    private final String WEB_CLIENT_ID = "928488830433-camll1e451a5i4a0vrart4aia0rq707i.apps.googleusercontent.com";
    private GoogleSignInClient signInClient;
    private static final int RC_SIGN_IN = 9001;

    private String message;

    public signupModel() {
        setMessage(" ");
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
        auth = FirebaseAuth.getInstance();

    }

    private void setMessage(String message) {
        this.message = message;
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

    private boolean checkEmpty(String email, String name) {
        boolean cond1 = name.length() != 0;
        boolean cond2 = email.length() != 0;
        if (!cond1) {
            setMessage("Name cannot be empty");
            return false;
        }
        else if (!cond2) {
            setMessage("Email cannot be empty");
            return false;
        }
        return true;
    }

    private boolean checkPassword(String pass, String conf_pass, String name) {
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

    public void createAccount(String email, String password, String confirmPassword, String name, signupPresenter presenter) {
        if (checkEmpty(email, name)) {
            userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    boolean equalsEmail = false;

                    DataSnapshot users = task.getResult();
                    for (DataSnapshot user : users.getChildren()) {
                        String currentemail = " ";
                        if (user.hasChild("email")) {
                            currentemail = user.child("email").getValue(String.class).toString().trim();
                        }
                        if (currentemail.equals(email)) {
                            equalsEmail = true;
                        }

                    }
                    if (equalsEmail) {
                        setMessage("Email already accociated with an account");
                    } else if (checkPassword(password, confirmPassword, name)) {
                        createAccountOnFirebase(email, password, name, presenter);
                    }

                }
            });

        }

        presenter.setMessage(message);
    }

    private void createAccountOnFirebase(String email, String pass, String name, signupPresenter presenter) {
        Activity activity = presenter.getViewActivity();
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
                                        setMessage("Signup Successful");
                                        presenter.takeToLogin();
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

    /*
    public void signUpWithGoogle(signupPresenter presenter) {


    }

     */

    public void onSignUpResult(ActivityResult result, signupPresenter presenter) {
        if (result.getResultCode() == RESULT_OK) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            presenter.takeToLogin();
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







}
