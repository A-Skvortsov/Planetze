package com.example.planetze;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityPaymentBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

public class Payment extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPaymentBinding binding;
    Button btn;
    String PUSHABLE_KEY = "pk_test_51QNyLBEClbgNHzIwl1LvxmmIIbAJfT0jkrXaxorbPqbtTVGeqhKlEoeBNNNXltdSENhs9sVE2PNdR5GMowGXNB1j00WkAP3Xzi";

    PaymentSheet paymentSheet;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn = findViewById(R.id.button2);

        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        PaymentConfiguration.init(this, PUSHABLE_KEY);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers  ",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);


    }

}