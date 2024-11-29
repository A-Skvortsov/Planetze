package com.example.planetze;

import android.content.Intent;
import static androidx.navigation.fragment.FragmentKt.findNavController;
import static java.security.AccessController.getContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference userRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
        auth = FirebaseAuth.getInstance();

        /*
        DatabaseReference myRef = db.getReference("testDemo");
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference("user data");
        auth = FirebaseAuth.getInstance();
        
        */


        // TODO: Please DON'T delete the comments below

/*        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        NavController navController = navHostFragment.getNavController();
       NavigationUI.setupWithNavController(bottomNavigationView, navController);
*/
        //Intent intent = new Intent(MainActivity.this, SurveyResults.class);
        //startActivity(intent);

        initializeData();


        if (savedInstanceState == null) {
            onOpenApp();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void takeToHomePage() {
        //change this later
        String userID = UserData.getUserID(getApplicationContext());

        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            for(DataSnapshot user:users.getChildren()) {
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = user.child("is_new_user").getValue().toString().equals("true");
  
                if (cond1 && cond2) {
                    loadFragment(new SurveyFragment());
                    break;
                }
                else if (cond1) {
                    navigateToHomeActivity();
                    break;
                }

            }
        });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void onOpenApp() {
        if (!UserData.isLoggedIn(getApplicationContext())) {
            loadFragment(new LoginView());
        }
        else {
            takeToHomePage();
        }
    }


    private void initializeData() {
        boolean isLoggedIn = UserData.isLoggedIn(getApplicationContext());
        boolean stayLoggedOn = UserData.stayLoggedOn(getApplicationContext());
        if (isLoggedIn && !stayLoggedOn) {
            UserData.logout(getApplicationContext());
        }
    }
}