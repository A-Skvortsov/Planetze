package utilities;

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

    public static boolean isNewUser(Context context) {
        getIsNewUser(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("isNewUser", true);
    }

    private static void getIsNewUser(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object inu = user.child("is_new_user").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = inu != null && inu.toString().equals("true");

                if (cond1 && cond2) {
                    setIsNewUser(context, true);
                    break;
                }
                else if (cond1) {
                    setIsNewUser(context,false);
                    break;
                }

            }
        });
    }

    private static void setIsNewUser(Context context, boolean is_new_user) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("isNewUser", is_new_user);
        e.commit();
    }

    public static boolean stayLoggedOn(Context context) {
        getStayLoggedOn(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("stayLoggedOn", false);
    }

    private static void getStayLoggedOn(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object slo = user.child("settings/stay_logged_on").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = slo != null && slo.toString().equals("true");

                if (cond1 && cond2) {
                    setStayLoggedOn(context, true);
                    break;
                }
                else if (cond1) {
                    setStayLoggedOn(context,false);
                    break;
                }

            }
        });
    }

    private static void setStayLoggedOn(Context context, boolean stayLoggedOn) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("stayLoggedOn", stayLoggedOn);
        e.commit();
    }

    public static boolean interpolateEmissionsData(Context context) {
        getInterpolateEmissionsData(context);
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getBoolean("interpolateEmissionsData", false);
    }

    private static void getInterpolateEmissionsData(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object ied = user.child("settings/interpolate_emissions_data").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = ied != null && ied.toString().equals("true");

                if (cond1 && cond2) {
                    setInterpolateEmissionsData(context, true);
                    break;
                }
                else if (cond1) {
                    setInterpolateEmissionsData(context,false);
                    break;
                }

            }
        });
    }

    private static void setInterpolateEmissionsData(Context context, boolean interpolateEmissionsData) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("interpolateEmissionsData", interpolateEmissionsData);
        e.commit();
    }
}
