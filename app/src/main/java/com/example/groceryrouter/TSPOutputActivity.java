package com.example.groceryrouter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TSPOutputActivity extends AppCompatActivity {

    ListView warehouseCoordinatesList, deliveryLocationsList, deliveryAgentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_s_p_output);
        warehouseCoordinatesList = findViewById(R.id.warehouse_coordinates_list);
        deliveryLocationsList = findViewById(R.id.delivery_coordinates_list);
        deliveryAgentsList = findViewById(R.id.delivery_agents_list);

        try {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String warehouseCoordinate = extras.getString("warehouseCoordinates");
            ArrayList<String> warehouseCoordinates = new ArrayList<>();
            warehouseCoordinates.add(warehouseCoordinate);
            ArrayList<String> deliveryCoordinates = extras.getStringArrayList("deliveryCoordinates");
            ArrayList<String> deliveryAgents = extras.getStringArrayList("deliveryAgents");

            ArrayAdapter<String> warehouseCoordinatesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,warehouseCoordinates);
            ArrayAdapter<String> deliveryCoordinatesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  deliveryCoordinates);
            ArrayAdapter<String> deliveryAgentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deliveryAgents);

            warehouseCoordinatesList.setAdapter(warehouseCoordinatesAdapter);
            deliveryLocationsList.setAdapter(deliveryCoordinatesAdapter);
            deliveryAgentsList.setAdapter(deliveryAgentsAdapter);
            for(int i=0; i<deliveryCoordinates.size(); i++)
                Log.d("a", deliveryCoordinates.get(i));
            for(int i=0; i<deliveryAgents.size(); i++)
                Log.d("a", deliveryAgents.get(i));
        }
        catch(Exception ignored){}
    }
}
