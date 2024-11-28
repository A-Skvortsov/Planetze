package com.example.planetze;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserData {

    private static SharedPreferences p;
    public UserData() {

    }

    public static String getUserID(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("UserID", "");
    }

    public static void login(Context context, String userID) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString("UserID", userID);
        e.putBoolean("isLoggedIn", true);
        e.commit();
    }

    public static void logout(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("isLoggedIn", false);
        e.putString("UserID", " ");
        e.commit();
    }

    public static boolean isLoggedIn(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("isLoggedIn", false);
    }

    public static boolean is_new_user(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("isNewUser", true);
    }

    private static void get_is_new_user(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                String userID = UserData.getUserID(context);
                for(DataSnapshot user:users.getChildren()) {
                    boolean cond1 = user.getKey().toString().trim().equals(userID);
                    boolean cond2 = user.child("is_new_user").getValue().toString().equals("true");
                    //System.out.println(user.getKey().toString().trim() + "       " +userID);
                    if (cond1 && cond2) {
                        set_is_new_user(context, true);
                        break;
                    }
                    else if (cond1) {
                        set_is_new_user(context,false);
                        break;
                    }
                }
                //presenter.takeToHub();
            }
        });
    }

    public static boolean stayLoggedOn(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("stayLoggedOn", true);
    }

    private static void set_is_new_user(Context context, boolean is_new_user) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("isNewUser", is_new_user);
        e.commit();
    }

    private static void get_stayLoggedOn(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                String userID = UserData.getUserID(context);
                for(DataSnapshot user:users.getChildren()) {
                    boolean cond1 = user.getKey().toString().trim().equals(userID);
                    boolean cond2 = user.child("Settings/stayLoggedOn").getValue().toString().equals("true");
                    //System.out.println(user.getKey().toString().trim() + "       " +userID);
                    if (cond1 && cond2) {
                        set_stayLoggedOn(context, true);
                        break;
                    }
                    else if (cond1) {
                        set_stayLoggedOn(context,false);
                        break;
                    }
                }
                //presenter.takeToHub();
            }
        });
    }

    public static void set_stayLoggedOn(Context context, boolean stayLoggedOn) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("stayLoggedOn", stayLoggedOn);
        e.commit();
    }

    /*
    public void logout() {
        UserData.logout(getContext());
        loadFragment(new LoginView());
    }

     */



}
