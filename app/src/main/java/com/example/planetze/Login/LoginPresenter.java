package com.example.planetze.Login;

import androidx.activity.result.ActivityResult;

public class LoginPresenter {

    private LoginModel model;
    private LoginView view;

    public LoginPresenter(LoginModel model, LoginView view) {
        this.view = view;
        this.model = model;
    }

    public void loginUser(String email, String password) {
        if (email == null || email.length() == 0) {
            setMessage("Email Cannot be empty");
        }
        else if (password == null || password.length() == 0) {
            setMessage("Password Cannot be empty");
        }
        else {
            model.loginUser(email, password, this);
        }

    }

    public void setSignUpLauncher() {
        view.setSignUpLauncher();
    }

    public void onSignInResult(ActivityResult result) {
        model.onSignInResult(result, this);
    }

    public void setMessage(String message) {
        view.setMessage(message);
    }

    public void takeToHomePage() {
        view.takeToHomePage();
    }


}
