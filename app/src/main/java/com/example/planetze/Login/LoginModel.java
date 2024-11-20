package com.example.planetze.Login;

import androidx.annotation.NonNull;

import com.example.planetze.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
}

//put testing tickets at sprint 3


