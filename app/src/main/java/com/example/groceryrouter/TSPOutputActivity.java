package com.example.groceryrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import java.util.ArrayList;

public class TSPOutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_s_p_output);
        try {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String warehouseCoordinates = extras.getString("warehouseCoordinates");
            ArrayList<String> deliveryCoordinates = extras.getStringArrayList("deliveryCoordinates");
            ArrayList<String> deliveryAgents = extras.getStringArrayList("deliveryAgents");

        }
        catch(Exception ignored){}
    }
}
