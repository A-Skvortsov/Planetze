package com.example.planetze.ForgetPassword;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class forgetPasswordModel {

    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private FirebaseDatabase db;

    public forgetPasswordModel() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
    }

    private void sendPassResetEmail(String email, forgetPasswordPresenter presenter) {
        auth.sendPasswordResetEmail(email);
        presenter.setMessage("Password reset link sent! Please check your email");
    }

    public void sendPassReset(String email, forgetPasswordPresenter presenter) {

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                //emailArray = new ArrayList<String>();
                boolean equalsEmail = false;
                for(DataSnapshot user:users.getChildren()) {
                    String currentemail = " ";
                    if (user.hasChild("email")) {
                        currentemail = user.child("email").getValue(String.class).toString().trim();
                    }
                    if (currentemail.equals(email)) {
                        equalsEmail = true;
                    }

                }
                if (equalsEmail) {
                    sendPassResetEmail(email, presenter);
                }
                else {
                    presenter.setMessage("There isn't an account accociated with that email");
                }


            }
        });
    }
}
