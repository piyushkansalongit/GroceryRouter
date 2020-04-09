package com.example.groceryrouter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TSPOutputActivity extends AppCompatActivity {

    String warehouseCoordinate;
    DeliveryCoordinatesDB deliveryCoordinatesDB;
    DeliveryAgentsDB deliveryAgentsDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_s_p_output);


        try {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            warehouseCoordinate = extras.getString("warehouseCoordinates");
            deliveryCoordinatesDB = MainActivity.deliveryCoordinatesDB;
            deliveryAgentsDB = MainActivity.deliveryAgentsDB;

        }
        catch(Exception ignored){}
    }
}
