package com.example.planetze.ForgetPassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.planetze.ForgotPasswordFragment;
import com.example.planetze.LogInFragment;
import com.example.planetze.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgetPasswordView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private ArrayList<String> emailArray;
    private FirebaseDatabase db;

    // TODO: Rename and change types of parameters

    private EditText emailInput;
    private String email;

    private String messagetext;
    private TextView message;

    private Button login, sendlink;

    private forgetPasswordPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        login = view.findViewById(R.id.returnButton);
        sendlink = view.findViewById(R.id.resetPasswordButton);
        emailInput = view.findViewById(R.id.emailInput);
        message = view.findViewById(R.id.msg);

        presenter = new forgetPasswordPresenter(this, new forgetPasswordModel());
        presenter.setMessage(" ");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new LogInFragment());
            }
        });

        sendlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailInput.getText().toString().trim();
                presenter.sendPassReset(email);

            }
        });

        return view;

    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void setMessage(String msg) {
        message.setText(msg);
    }

}
