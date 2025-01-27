package com.example.groceryrouter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class CoordinateWarehouseActivity extends AppCompatActivity {
    String Latitude, Longitude;
    EditText field1;
    EditText field2;
    Button googleMap, done;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_warehouse);
        field1 = findViewById(R.id.latitude_coordinate_warehouse);
        field2 = findViewById(R.id.longitude_coordinate_warehouse);
        done = findViewById(R.id.done_coordinate_warehouse);
        googleMap = findViewById(R.id.set_coordinate_warehouse);


        // OnClickListeners
        done.setOnClickListener(view -> doneHandle());
        googleMap.setOnClickListener(view -> mapHandle());

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            String latitude = extras.getString("Latitude");
            String longitude = extras.getString("Longitude");

            if(!latitude.equals("") && !longitude.equals("")){
                field1.setText(latitude);
                field2.setText(longitude);
            }
        }
    }

    private void mapHandle() {
        Intent intent = new Intent(CoordinateWarehouseActivity.this, MapsActivity.class);
        intent.putExtra("warehouseActivity", true);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doneHandle() {
        Latitude = field1.getText().toString();
        Longitude = field2.getText().toString();
        if(Latitude.equals("") || Longitude.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Please fill all the fields", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Intent intent = new Intent(CoordinateWarehouseActivity.this, MainActivity.class);
            intent.putExtra("warehouseCoordinates",String.join(" ",Latitude, Longitude));
            startActivity(intent);
        }
    }

//    private void toastMessage(String message){
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
}
