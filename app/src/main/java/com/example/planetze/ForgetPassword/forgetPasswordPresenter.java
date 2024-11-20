package com.example.planetze.ForgetPassword;

public class forgetPasswordPresenter {

    forgetPasswordModel model;
    forgetPasswordView view;

    public forgetPasswordPresenter(forgetPasswordView view, forgetPasswordModel model) {
        this.view = view;
        this.model = model;
    }

    public void sendPassReset(String email) {
        if (email.length() == 0) {
            setMessage("Email cannot be empty");
        }
        else {
            model.sendPassReset(email, this);
        }

    }

    public void setMessage(String msg) {
        view.setMessage(msg);
    }
}
