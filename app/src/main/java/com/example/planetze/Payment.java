package com.example.planetze;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.Toast;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.planetze.databinding.ActivityPaymentBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    Button btn;
    String PUSHABLE_KEY = "pk_test_51QNyLBEClbgNHzIwl1LvxmmIIbAJfT0jkrXaxorbPqbtTVGeqhKlEoeBNNNXltdSENhs9sVE2PNdR5GMowGXNB1j00WkAP3Xzi";
    String SECRET_KEY;

    PaymentSheet paymentSheet;
    StringRequest stringRequest;
    String customerId;
    String Ephemeral_key;
    String Client_secret;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn = findViewById(R.id.button2);

        com.example.planetze.databinding.ActivityPaymentBinding binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // allows u to talk to stripe
        PaymentConfiguration.init(this, PUSHABLE_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

        });


        //1. We will first here create a customer

        String url = "https://api.stripe.com/v1/customers";
        //prepares the app to send network requests


        //returns a strng of the servers repsone from us making a https request
        //Request.Method.POST --> tell us tht were provides info to the server
        //url --> where the https request is sent
        //onResponse wht to do after server has sent back data/(in this case string)
        //onError wht to do if the server return a error
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String customerId = jsonObject.getString("id");
                    Toast.makeText(Payment.this, customerId, Toast.LENGTH_SHORT).show();
                    getEphemeral_key(customerId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+ SECRET_KEY);
                return map;            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest1);



    }

    // Once we have created a customer, we will use the customer id, to get the  Ephemeral key
    //Ephemeral_key -- helps to connect our app to stripe, gives u temperary acces to stripes payment system for a specfic user

    private void getEphemeral_key(String customerId) {
        //1. We will first here create a customer

        String url = "https://api.stripe.com/v1/ephemeral_keys";

        //prepares the app to send network requests


        //returns a strng of the servers repsone from us making a https request
        //Request.Method.POST --> tell us tht were provides info to the server
        //url --> where the https request is sent
        //onResponse wht to do after server has sent back data/(in this case string)
        //onError wht to do if the server return a error
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Ephemeral_key = jsonObject.getString("id");
                    getClientSecret(customerId, Ephemeral_key);
                    Toast.makeText(Payment.this,Ephemeral_key, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+ SECRET_KEY);
                map.put("Stripe-Version", "2024-11-20.acacia");
                return map; }

            // send data to the https for the POST
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer", customerId);
                return map;            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest1);

    }

    // a key to finalize payment and cofirm transaction on the client side
    // recives client sceret and uses this to finalize payment
    //clientscrete a temporary pswd for the customer to approve payment, and complete purhcase
    private void getClientSecret(String customerId, String ephemeralKey) {
        String url = "https://api.stripe.com/v1/payment_intents";

        //prepares the app to send network requests


        //returns a strng of the servers repsone from us making a https request
        //Request.Method.POST --> tell us tht were provides info to the server
        //url --> where the https request is sent
        //onResponse wht to do after server has sent back data/(in this case string)
        //onError wht to do if the server return a error
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Client_secret = jsonObject.getString("client_secret");
                    Toast.makeText(Payment.this,Client_secret, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> head = new HashMap<>();
                head.put("Authorization", "Bearer "+ SECRET_KEY);
                return head; }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer", customerId);
                map.put("amount", "1000" + "00");
                map.put("currency", "usd");
                map.put("automatic_payment_methods[enabled]", "true");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest2);
    }


}




