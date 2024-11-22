package com.example.planetze;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.planetze.databinding.ActivityPaymentBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPaymentBinding binding;
    Button btn;
    String PUSHABLE_KEY = "pk_test_51QNyLBEClbgNHzIwl1LvxmmIIbAJfT0jkrXaxorbPqbtTVGeqhKlEoeBNNNXltdSENhs9sVE2PNdR5GMowGXNB1j00WkAP3Xzi";
    String SECRET_KEY; // ur not allowed to see this

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

        String url = "cus_RGW7sv4XRGFzAT";

        // sets up a systsem to get networks reuqets
        RequestQueue requestQueue =  Volley.newRequestQueue(this);


        //sends a request and expects a string response , to in this case stripe
        //request.Method.POST specfices the type of request u want to make, this case send data (POST)
        // url the server where ur sending ur info to
        //the 2 functions handles a requets that was succesful, and one that was failed ie a error
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

            new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // will implement later

            }

        },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // will implement later

            }

        }){


            // provides addition info which may be needed when sending our request to the sever
            // in this case the authorizatio which tells stripe were allowed to use their application through our given api key
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map header = new HashMap();
                // provides proof that u are allowed to make this request to our servie ie STRIPE
                header.put("Authorization", "Bearer "+SECRET_KEY);
                return header;
            }
        };






    }




}