package com.example.groceryrouter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String warehouseCoordinatesString;
    private ArrayList<String> deliveryCoordinatesList;
    private ArrayList<String> deliveryAgentsList;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            boolean isNotStart = bundle.getBoolean("isNotStart");
            if(isNotStart) {
                Log.d("Coming Here", "Coming Here");
                restore();
            }
        }


        try {
            String warehouseCoordinates_temp = bundle.getString("warehouseCoordinates");
            if (!warehouseCoordinates_temp.equals("")) {
                this.warehouseCoordinatesString = warehouseCoordinates_temp;
                this.flag1 = true;
                Toast toast = Toast.makeText(getApplicationContext(),"Warehouse Coordinates Registered", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception ignored) {}

        try {
            ArrayList<String> deliveryCoordinates_temp = bundle.getStringArrayList("deliveryCoordinates");
            if (!deliveryCoordinates_temp.isEmpty()) {
                this.deliveryCoordinatesList = new ArrayList<>(deliveryCoordinates_temp);
                this.flag2 = true;
                Toast toast = Toast.makeText(getApplicationContext(),"Delivery Coordinates Registered", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception ignored) {}


        try {
            ArrayList<String> deliveryAgents_temp = bundle.getStringArrayList("deliveryAgents");
            if (!deliveryAgents_temp.isEmpty()) {
                this.deliveryAgentsList = new ArrayList<>(deliveryAgents_temp);
                this.flag3 = true;
                Toast toast = Toast.makeText(getApplicationContext(),"Agent Capacities Registered", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception ignored) {}

        Button warehouseCoordinateButton = findViewById(R.id.warehouseLoc_main);
        warehouseCoordinateButton.setOnClickListener(view -> {
            save();
            Intent warehouseCoordinatedIntent = new Intent(MainActivity.this, CoordinateWarehouseActivity.class);
            startActivity(warehouseCoordinatedIntent);
        });

        Button deliveryCoordinatesButton = findViewById(R.id.deliveryLoc_main);
        deliveryCoordinatesButton.setOnClickListener(view -> {
            save();
            Intent deliveryCoordinatesIntent = new Intent(MainActivity.this, CoordinateInputActivity.class);
            deliveryCoordinatesIntent.putExtra("deliveryCoordinates", new ArrayList<String>());
            startActivity(deliveryCoordinatesIntent);
        });

        Button deliveryAgentsButton = findViewById(R.id.deliveryAgents_main);
        deliveryAgentsButton.setOnClickListener(view -> {
            save();
            Intent deliveryAgentsIntent = new Intent(MainActivity.this, DeliveryAgentActivity.class);
            deliveryAgentsIntent.putExtra("deliveryAgents", new ArrayList<String>());
            startActivity(deliveryAgentsIntent);
        });

        Button calculateRouteButton = findViewById(R.id.calculate_main);
        calculateRouteButton.setOnClickListener(view -> {
            if(this.flag1 && this.flag2 && this.flag3)
            {
                Intent outputIntent = new Intent(MainActivity.this, TSPOutputActivity.class);
                outputIntent.putExtra("warehouseCoordinates", this.warehouseCoordinatesString);
                outputIntent.putExtra("deliveryCoordinates", this.deliveryCoordinatesList);
                outputIntent.putExtra("deliveryAgents", this.deliveryAgentsList);
                save();
                startActivity(outputIntent);
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Please fill all the fields", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }


    protected void save()
    {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("whcs", warehouseCoordinatesString);
        myEdit.putString("dcl", ObjectSerializer.serialize(deliveryCoordinatesList));
        myEdit.putString("dal", ObjectSerializer.serialize(deliveryAgentsList));
        myEdit.putBoolean("f1", flag1);
        myEdit.putBoolean("f2", flag2);
        myEdit.putBoolean("f3", flag3);
        myEdit.apply();
    }

    protected void restore()
    {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        warehouseCoordinatesString = sh.getString("whcs", "");
        try {
            deliveryCoordinatesList = (ArrayList<String>) ObjectSerializer.deserialize(sh.getString("dcl", ""));
            deliveryAgentsList = (ArrayList<String>) ObjectSerializer.deserialize(sh.getString("dal", ""));
        }catch(Exception ignored){}

        flag1 = sh.getBoolean("f1", false);
        flag2 = sh.getBoolean("f2", false);
        flag3 = sh.getBoolean("f3", false);
    }

}


