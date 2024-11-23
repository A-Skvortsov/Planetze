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
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

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
    private ActivityPaymentBinding binding;
    Button btn;
    String PUSHABLE_KEY = "pk_test_51QNyLBEClbgNHzIwl1LvxmmIIbAJfT0jkrXaxorbPqbtTVGeqhKlEoeBNNNXltdSENhs9sVE2PNdR5GMowGXNB1j00WkAP3Xzi";
    String SECRET_KEY; // ur not allowed to see this

    PaymentSheet paymentSheet;
    StringRequest stringRequest;
    String customerId;
    String Ephemeral_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn = findViewById(R.id.button2);

        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // allows u to talk to stripe
        PaymentConfiguration.init(this,PUSHABLE_KEY );

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

        });


        //1. We will first here create a customer

        String url = "https://api.stripe.com/v1/customers";
        //prepares the app to send network requests
        RequestQueue requestQueue = Volley.newRequestQueue(this);


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
                    customerId = jsonObject.getString("id");
                    getEphemeral_key(customerId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }


        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("Authorization", "Bearer "+ SECRET_KEY);
                return map;
            }
        };





    }
    // Once we have created a customer, we will use the customer id, to get the  Ephemeral key
    //Ephemeral_key -- helps to connect our app to stripe
    private void getEphemeral_key(String customerId) {
        String url = "https://api.stripe.com/v1/ephemeral_keys";
        //prepares the app to send network requests
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                    //getEphemeral_key(Ephemeral_key);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }


        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("Authorization", "Bearer "+ SECRET_KEY);
                map.put("Stripe-Version", "2024-11-20.acacia");
                return map;
            }

            // send data to the https for the POST
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer", customerId);
                return map;
            }
        };




    }


}