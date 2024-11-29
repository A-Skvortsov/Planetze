package com.example.planetze;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

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
                    if (cond1 && cond2) {
                        set_is_new_user(context, true);
                        break;
                    }
                    else if (cond1) {
                        set_is_new_user(context,false);
                        break;
                    }
                }
            }
        });
    }

    private static void set_is_new_user(Context context, boolean is_new_user) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("isNewUser", is_new_user);
        e.commit();
    }

    public static boolean stayLoggedOn(Context context) {
        get_stayLoggedOn(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("stayLoggedOn", false);
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
                    Object slo = user.child("settings/stay_logged_on").getValue();
                    boolean cond1 = user.getKey().toString().trim().equals(userID);
                    boolean cond2 = slo != null && slo.toString().equals("true");
                    if (cond1 && cond2) {
                        set_stayLoggedOn(context, true);
                        break;
                    }
                    else if (cond1) {
                        set_stayLoggedOn(context,false);
                        break;
                    }
                }
            }
        });
    }

    public static void set_stayLoggedOn(Context context, boolean stayLoggedOn) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("stayLoggedOn", stayLoggedOn);
        e.commit();
    }

    public static boolean interpolateEmissionsData(Context context) {
        get_interpolateEmissionsData(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("interpolateEmissionsData", false);
    }

    private static void get_interpolateEmissionsData(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                String userID = UserData.getUserID(context);
                for(DataSnapshot user:users.getChildren()) {
                    Object ied = user.child("settings/interpolate_emissions_data").getValue();
                    boolean cond1 = user.getKey().toString().trim().equals(userID);
                    boolean cond2 = ied != null && ied.toString().equals("true");
                    if (cond1 && cond2) {
                        set_interpolateEmissionsData(context, true);
                        break;
                    }
                    else if (cond1) {
                        set_interpolateEmissionsData(context,false);
                        break;
                    }
                }
            }
        });
    }

    public static void set_interpolateEmissionsData(Context context, boolean stayLoggedOn) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("interpolateEmissionsData", stayLoggedOn);
        e.commit();
    }



}
