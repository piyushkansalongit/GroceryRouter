package com.example.groceryrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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
        }
        catch (Exception ignored){}
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
