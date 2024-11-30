package utilities;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserData {

    private static SharedPreferences p;
    public UserData() {

    }

    public static String getUserID(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("UserID", "");
    }

    public static String getUsername(Context context) {
        retrieveUsername(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("Username", "");
    }

    private static void retrieveUsername(Context context) {

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object name = user.child("name").getValue();
                boolean cond1 = name != null && user.getKey().toString().trim().equals(userID);
                if (cond1) {
                    setUsername(context, name.toString().trim());
                    break;
                }
            }
        });

    }

    private static void setUsername(Context context, String name) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString("Username", name);
        e.apply();
    }

    public static String getEmail(Context context) {
        retrieveEmail(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("Email", "");
    }

    private static void retrieveEmail(Context context) {

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object email = user.child("email").getValue();
                boolean cond1 = email != null && user.getKey().toString().trim().equals(userID);
                if (cond1) {
                    setEmail(context, email.toString().trim());
                    break;
                }
            }
        });

    }

    private static void setEmail(Context context, String email) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString("Email", email);
        e.apply();
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
        e.putString("Username", " ");
        e.commit();
    }

    public static boolean isLoggedIn(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("isLoggedIn", false);
    }

    public static boolean getSetting(Context context, String setting) {
        retrieveSetting(context,setting);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean(setting, false);
    }

    private static void retrieveSetting(Context context, String setting) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object s = user.child("setting/"+setting).getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = s != null && s.toString().equals("true");

                if (cond1 && cond2) {
                    setSetting(context, setting,true);
                    break;
                }
                else if (cond1) {
                    setSetting(context,setting,false);
                    break;
                }

            }
        });
    }

    private static void setSetting(Context context, String settingName, boolean settingValue) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(settingName, settingValue);
        e.apply();
    }


    public static void initialize(Context context) {
        getEmail(context);
        getUsername(context);

        retrieveSetting(context,"stay_logged_on");
        retrieveSetting(context,"interpolate_emission_data");
        retrieveSetting(context,"hide_grid_lines");
        retrieveSetting(context,"show_trend_line_points");
    }

}
