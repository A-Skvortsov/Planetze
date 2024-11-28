package com.example.planetze.Login;

import static android.app.Activity.RESULT_OK;
import static android.provider.Settings.System.getString;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.planetze.EcoTrackerFragment;
import com.example.planetze.HomeFragment;
import com.example.planetze.R;
import com.example.planetze.SurveyFragment;
import com.example.planetze.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginModel {

    private FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference userRef;

    public LoginModel() {

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
    }

    public void loginUser(String email, String password, LoginPresenter presenter) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            String userID = user.getUid().toString().trim();
                            if (user.isEmailVerified()) {
                                UserData.login(presenter.getViewContext(),userID);
                                takeToHomePage(presenter);
                                //eco guage
                                // survey if just registered
                            }
                            else {
                                presenter.setMessage("Account needs to be verified");
                            }

                        } else {
                            //Toast.makeText(activity, "signup failed", Toast.LENGTH_SHORT).show();
                            presenter.setMessage("Invalid email or password");

                        }
                    }
                });
    }

    private void takeToHomePage(LoginPresenter presenter) {
        //change this later

        //presenter.takeToHub();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                String userID = UserData.getUserID(presenter.getViewContext());
                for(DataSnapshot user:users.getChildren()) {
                    boolean cond1 = user.getKey().toString().trim().equals(userID);
                    boolean cond2 = user.child("is_new_user").getValue().toString().equals("true");
                    //System.out.println(user.getKey().toString().trim() + "       " +userID);
                    if (cond1 && cond2) {
                        presenter.takeToSurvey();
                        break;
                    }
                    else if (cond1) {
                        presenter.takeToHub();
                        break;
                    }
                }
            }
        });


    }

    public void onSignInResult(ActivityResult result, LoginPresenter presenter) {
        if (result.getResultCode() == RESULT_OK) {
            presenter.setMessage("Launched Google sign up");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            takeToHomePage(presenter);
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

//put testing tickets at sprint 3


