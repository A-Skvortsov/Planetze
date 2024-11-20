package com.example.planetze.Signup;


import android.app.Activity;

public class signupPresenter {
    private signupView view;
    private signupModel model;

    private String message;

    public signupPresenter(signupView view, signupModel model) {
        this.view = view;
        this.model = model;
    }

    public void createAccount(String email, String password, String confirmPassword,String name) {
        model.createAccount(email, password, confirmPassword, name, this);
        setTextMessage(message);
    }

    public void setTextMessage(String msg) {
        view.setMessage(msg);
    }

    public void takeToLogin() {
        view.takeToLogin();
    }

    public Activity getViewActivity() {
        return view.getViewActivity();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
