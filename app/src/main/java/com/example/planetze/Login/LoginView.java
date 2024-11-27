package com.example.planetze.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.planetze.EcoTrackerFragment;
import com.example.planetze.ForgotPasswordFragment;
import com.example.planetze.HomeFragment;
import com.example.planetze.R;
import com.example.planetze.SignUpFragment;
import com.example.planetze.SurveyFragment;
import com.example.planetze.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;

public class LoginView extends Fragment  {

    private EditText loginEmail, loginPass;

    private Button login;

    private FirebaseDatabase db;

    private TextView inputError, forgotpass;
    private Button googleSignUp;

    private LoginPresenter presenter;

    private ActivityResult r;

    ActivityResultLauncher<Intent> launcher;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");

        TextView signUpLink = view.findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(v -> loadFragment(new SignUpFragment()));

        loginEmail = view.findViewById(R.id.emailInput);
        loginPass = view.findViewById(R.id.passwordInput);
        login = view.findViewById(R.id.logInButton);
        inputError = view.findViewById(R.id.error);
        forgotpass = view.findViewById(R.id.forgotPasswordLink);

        googleSignUp = view.findViewById(R.id.signInWithGoogleButton);

        presenter = new LoginPresenter(new LoginModel(), this);

        presenter.setMessage(" ");
        presenter.setSignUpLauncher();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmail.getText().toString().trim();
                String pass = loginPass.getText().toString().trim();

                presenter.loginUser(email, pass);

            }
        });

        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startGoogleSignin();
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //auth.sendPasswordResetEmail(email);
                loadFragment(new ForgotPasswordFragment());
            }
        });
        return view;
    }

    public void setMessage(String msg) {
        inputError.setText(msg);
    }

    public Context getViewContext() {
        return getContext();
    }

    public void takeToSurvey() {
        loadFragment(new SurveyFragment());
    }

    public void takeToHub() {

        //Bundle bundle = new Bundle();
        System.out.println(getActivity().getSupportFragmentManager().findFragmentById(R.id.LoginView) == null);
        //requireActivity().getSupportFragmentManager().executePendingTransactions();
        Fragment f = requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
        NavController navController = NavHostFragment.findNavController(f);
        navController.navigate(R.id.EcoTrackerFragment);

        //loadFragment(new EcoTrackerFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Activity getViewActivity() {
        return getActivity();
    }

    public void setSignUpLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        presenter.onSignInResult(result);
                    }
                });
    }

    //take to home fragment and connect so bottem bar can show

    public void startGoogleSignin() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(getViewActivity(), options);

        Intent intent = client.getSignInIntent();
        launcher.launch(intent);
    }



}
