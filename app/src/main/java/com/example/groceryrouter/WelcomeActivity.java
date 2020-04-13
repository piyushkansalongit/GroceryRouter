package com.example.groceryrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WelcomeActivity extends AppCompatActivity {


    public static DeliveryCoordinatesDB deliveryCoordinatesDB;
    public static DeliveryAgentsDB deliveryAgentsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        deliveryCoordinatesDB = new DeliveryCoordinatesDB(this);
        deliveryAgentsDB = new DeliveryAgentsDB(this);
        try {
            deliveryCoordinatesDB.deleteAll();
            deliveryAgentsDB.deleteAll();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://vehicleroutingproblem.herokuapp.com/";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        catch (Exception ignored){}
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
