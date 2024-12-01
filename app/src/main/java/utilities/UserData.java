package utilities;

import static java.lang.Thread.sleep;

import static utilities.Constants.FIREBASE_LINK;
import static utilities.Constants.HIDE_GRID_LINES;
import static utilities.Constants.INTERPOLATE_EMISSIONS_DATA;
import static utilities.Constants.HIDE_TREND_LINE_POINTS;
import static utilities.Constants.STAY_LOGGED_ON;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class UserData {

    private static SharedPreferences p;

    public UserData() {

    }

    public static String getUserID(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("UserID", "");
    }

    public static String getUsername(Context context) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("Username", "");
    }

    private static void retrieveUsername(Context context) {

        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_LINK);
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
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return p.getString("Email", "");
    }

    private static void retrieveEmail(Context context) {

        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_LINK);
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

    public static void login(Context context, String userID, String key) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString("UserID", userID);
        e.putBoolean("isLoggedIn", true);
        e.putString("privateKey", key);

        e.apply();
    }

    public static void googleLogin(Context context, String userID) {
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString("UserID", userID);
        e.putBoolean("isLoggedIn", true);

        e.apply();
    }

    public static void logout(Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
      
        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean("isLoggedIn", false);
        e.putString("UserID", " ");
        e.putString("credentials", " ");
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
        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_LINK);
        DatabaseReference userRef = db.getReference("user data");

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(context);
            for(DataSnapshot user:users.getChildren()) {
                Object s = user.child("settings/"+setting).getValue();
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
        retrieveEmail(context);
        retrieveUsername(context);

        retrieveSetting(context,STAY_LOGGED_ON);
        retrieveSetting(context,INTERPOLATE_EMISSIONS_DATA);
        retrieveSetting(context,HIDE_GRID_LINES);
        retrieveSetting(context, HIDE_TREND_LINE_POINTS);
    }

    public static void deleteAccount(Context context) {
        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_LINK);
        DatabaseReference userRef = db.getReference("user data");
        FirebaseAuth auth = FirebaseAuth.getInstance();

        p = context.getSharedPreferences("User", Context.MODE_PRIVATE);

        String key = p.getString("privateKey","");
        AuthCredential credential = EmailAuthProvider.getCredential(getEmail(context), key);
        auth.getCurrentUser().reauthenticate(credential);

        auth.getCurrentUser().delete();
        auth.signOut();
        userRef.child(UserData.getUserID(context)).removeValue();
        UserData.logout(context);
    }

    public static void addUserstoDatabase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_LINK);
        DatabaseReference unverifiedRef = db.getReference("unverified users");

        unverifiedRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                for(DataSnapshot user:users.getChildren()) {
                    String userID = user.getKey();
                    if (user.hasChild("email")) {
                        String email = user.child("email").getValue().toString().trim();
                        String password = user.child("password").getValue().toString().trim();
                        String name = user.child("name").getValue().toString().trim();
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                boolean cond = task.isSuccessful() && auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified();
                                if (cond) {
                                    String userID = auth.getCurrentUser().getUid().toString().trim();
                                    unverifiedRef.child(userID).removeValue();
                                    setDefaultSettings(userID, email, name);
                                    auth.signOut();
                                }
                            }
                        });
                    }

                }

            }
        });
    }

    public static void setDefaultSettings(String userID, String email, String name) {
        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_LINK);
        DatabaseReference userRef = db.getReference("user data");

        userRef.child(userID+"/email").setValue(email);
        userRef.child(userID+"/name").setValue(name);
        userRef.child(userID+"/is_new_user").setValue(true);
        userRef.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(false);
        userRef.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(false);
        userRef.child(userID+"/settings/"+ HIDE_TREND_LINE_POINTS).setValue(false);
        userRef.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(false);
        userRef.child(userID+"/calendar/0000-00-00/0").setValue(0);

    }

}
