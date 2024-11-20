package com.example.planetze;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class UserData {

    private static SharedPreferences p;
    public UserData() {

    }

    public static void setUserID(Context context, String userID) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString("UserID", userID);
        e.commit();
    }

    public static String getUserID(Context context, String userID) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("UserID", "");
    }




}
