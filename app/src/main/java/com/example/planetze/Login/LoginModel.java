package com.example.planetze.Login;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import com.example.planetze.HomeFragment;
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

public class LoginModel {

    private FirebaseAuth auth;

    public LoginModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void loginUser(String email, String password, LoginPresenter presenter) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                presenter.takeToHomePage();
                                presenter.setMessage("Login successful");
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
                            presenter.takeToHomePage();
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


