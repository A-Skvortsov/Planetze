package com.example.planetze.Login;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class LoginPresenter {

    private LoginModel model;
    private LoginView view;

    ActivityResultLauncher<Intent> launcher;

    public LoginPresenter(LoginModel model, LoginView view) {
        this.view = view;
        this.model = model;
    }

    public void loginUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            setMessage("Email cannot be empty");
        }
        else if (password == null || password.trim().isEmpty()) {
            setMessage("Password cannot be empty");
        }
        else {
            model.loginUser(email, password, this);
        }

    }

    public void setSignUpLauncher() { view.setSignUpLauncher();}

    public void onSignInResult(ActivityResult result) {
        model.onSignInResult(result, this);
    }

    public void setMessage(String message) {
        view.setMessage(message);
    }

    public void takeToSurvey() {
        view.takeToSurvey();
    }

    public void takeToHub() {
        view.takeToHub();
    }

    public void startGoogleSignin() {
        view.startGoogleSignin();
    }

    public Context getViewContext() {return view.getContext();}

}