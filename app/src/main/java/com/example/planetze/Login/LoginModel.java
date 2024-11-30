package com.example.planetze.Login;

import static android.app.Activity.RESULT_OK;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import static utilities.Constants.FIREBASE_LINK;
import static utilities.Constants.HIDE_GRID_LINES;
import static utilities.Constants.INTERPOLATE_EMISSIONS_DATA;
import static utilities.Constants.SHOW_TREND_LINE_POINTS;
import static utilities.Constants.STAY_LOGGED_ON;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import com.example.planetze.HomeActivity;
import com.example.planetze.SurveyFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import utilities.UserData;

public class LoginModel {

    private FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference userRef;

    public LoginModel() {

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance(FIREBASE_LINK);
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
                            }
                            else {
                                presenter.setMessage("Account needs to be verified");
                            }

                        } else {
                            presenter.setMessage("Invalid email or password");

                        }
                    }
                });


    }

    private void takeToHomePage(LoginPresenter presenter) {
        UserData.initialize(presenter.getViewContext());
        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(presenter.getViewContext());
            for(DataSnapshot user:users.getChildren()) {
                Object inu = user.child("is_new_user").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = inu != null && inu.toString().equals("true");

                if (cond1 && cond2) {
                    presenter.takeToSurvey();
                    break;
                }
                else if (cond1) {
                    presenter.takeToHub();
                    break;
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
                            googleSignin(presenter);
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

    private void setDefaultSettings(String userID) {
        userRef.child(userID+"/is_new_user").setValue(true);
        userRef.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(false);
        userRef.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(false);
        userRef.child(userID+"/settings/"+SHOW_TREND_LINE_POINTS).setValue(false);
        userRef.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(false);
        userRef.child(userID+"/calendar/0000-00-00/0").setValue(0);

    }

    private void googleSignin(LoginPresenter presenter) {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                UserData.login(presenter.getViewContext(), auth.getCurrentUser().getUid());
                UserData.initialize(presenter.getViewContext());
                DataSnapshot users = task.getResult();
                boolean equalsEmail = false;
                for(DataSnapshot user:users.getChildren()) {
                    String currentemail = " ";
                    if (user.hasChild("email")) {
                        currentemail = user.child("email").getValue(String.class).toString().trim();
                    }
                    if (currentemail.equals(auth.getCurrentUser().getEmail().trim())) {
                        equalsEmail = true;
                    }

                }

                if (!equalsEmail) {
                    String id = auth.getCurrentUser().getUid();
                    userRef.child(id+"/name").setValue(auth.getCurrentUser().getDisplayName());
                    userRef.child(id+"/email").setValue(auth.getCurrentUser().getEmail());
                    setDefaultSettings(id);
                    presenter.takeToSurvey();
                }
                else {
                    isNewUser(presenter);
                }

            }
        });
    }

    private void isNewUser(LoginPresenter presenter) {
        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = auth.getCurrentUser().getUid();
            for(DataSnapshot user:users.getChildren()) {
                Object inu = user.child("is_new_user").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = inu != null && inu.toString().equals("true");

                if (cond1 && cond2) {
                    presenter.takeToSurvey();
                    break;
                }
                else if (cond1) {
                    presenter.takeToHub();
                    break;
                }

            }
        });
    }

}

//put testing tickets at sprint 3


