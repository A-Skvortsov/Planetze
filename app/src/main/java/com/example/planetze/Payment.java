package com.example.planetze;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.planetze.databinding.ActivityEcoBalanceDestinationBinding;
import com.example.planetze.databinding.ActivityPaymentBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    Button btn;
    String PUSHABLE_KEY = "pk_test_51QNjq9F4HnTs1FIdZQt1c9LRqmQTguGHdET56UYsLniR3h2QIrrY1gdZa80lF8tf8mge5idM5ghTdFOYLSTf420U00fJdiiGaD";
    String SECRET_KEY;

    PaymentSheet paymentSheet;
    StringRequest stringRequest;
    String customerId ="";
    String Ephemeral_key;
    String Client_secret;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @NonNull ActivityPaymentBinding binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // allows u to talk to stripe
        PaymentConfiguration.init(this, PUSHABLE_KEY);
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);
         getcustomerid();
        btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Client_secret != null) {
                    paymentSheet.presentWithPaymentIntent(Client_secret, new PaymentSheet.Configuration("Plantze", new PaymentSheet.CustomerConfiguration(customerId, Ephemeral_key)));
                }

                else {
                    Toast.makeText(Payment.this, "hello", Toast.LENGTH_SHORT).show();

                }
            }
        });




    }//end of on create

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof  PaymentSheetResult.Completed){
            Toast.makeText(Payment.this, "Payment Succesfull", Toast.LENGTH_SHORT).show();
        }
    }


    public void getcustomerid(){
        String url = "https://api.stripe.com/v1/customers";
        //prepares the app to send network requests




        //returns a strng of the servers repsone from us making a https request
        //Request.Method.POST --> tell us tht were provides info to the server
        //url --> where the https request is sent
        //onResponse wht to do after server has sent back data/(in this case string)
        //onError wht to do if the server return a error
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String customerId = jsonObject.getString("id");
                    Log.d("PaymentActivity", "Customer ID: " + customerId);
                    Toast.makeText(Payment.this, customerId, Toast.LENGTH_SHORT).show();
                    getEphemeral_key(customerId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();


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


    public void getEphemeral_key(String customerId) {
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
                error.printStackTrace();



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


    public void getClientSecret(String customerId, String ephemeralKey) {
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
                    //PaymentFlow();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();



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








