package com.example.groceryrouter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static DeliveryCoordinatesDB deliveryCoordinatesDB;
    public static DeliveryAgentsDB deliveryAgentsDB;
    private String warehouseCoordinatesString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deliveryCoordinatesDB = WelcomeActivity.deliveryCoordinatesDB;
        deliveryAgentsDB = WelcomeActivity.deliveryAgentsDB;
        // Buttons
        Button warehouseCoordinateButton = findViewById(R.id.warehouseLoc_main);
        Button deliveryCoordinatesButton = findViewById(R.id.deliveryLoc_main);
        Button deliveryAgentsButton = findViewById(R.id.deliveryAgents_main);
        Button calculateRouteButton = findViewById(R.id.calculate_main);

        // Button Handlers
        warehouseCoordinateButton.setOnClickListener(view -> whcHandle());
        deliveryCoordinatesButton.setOnClickListener(view -> deliveryCoordinatesHandle());
        deliveryAgentsButton.setOnClickListener(view -> deliveryAgentsHandle());
        calculateRouteButton.setOnClickListener(view -> calculateOutputHandle());

        try {
            Intent intent = getIntent();
            assert intent != null;
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            String warehouseCoordinates_temp = bundle.getString("warehouseCoordinates");
            if (!warehouseCoordinates_temp.equals("")) {
                this.warehouseCoordinatesString = warehouseCoordinates_temp;
                toastMessage("Warehouse Coordinates Registered");
                save();
            }
        } catch (Exception ignored) {
        }

    }

    private void whcHandle() {
        Intent warehouseCoordinatedIntent = new Intent(MainActivity.this, CoordinateWarehouseActivity.class);
        startActivity(warehouseCoordinatedIntent);
    }

    private void deliveryCoordinatesHandle() {
        Intent deliveryCoordinatesIntent = new Intent(MainActivity.this, CoordinateInputActivity.class);
        startActivity(deliveryCoordinatesIntent);
    }

    private void deliveryAgentsHandle() {
        Intent deliveryAgentsIntent = new Intent(MainActivity.this, DeliveryAgentActivity.class);
        startActivity(deliveryAgentsIntent);
    }

    private void calculateOutputHandle() {
        Intent outputIntent = new Intent(MainActivity.this, TSPOutputActivity.class);
        restore();
        outputIntent.putExtra("warehouseCoordinates", warehouseCoordinatesString);
        startActivity(outputIntent);
    }

    // Helper Functions
    private void toastMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void save()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("whcs", warehouseCoordinatesString);
        myEdit.apply();
    }

    protected void restore()
    {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        warehouseCoordinatesString = sh.getString("whcs", "");
    }

}








