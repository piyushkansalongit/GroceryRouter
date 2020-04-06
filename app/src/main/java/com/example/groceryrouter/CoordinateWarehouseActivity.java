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
    Button done;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_warehouse);
        field1 = findViewById(R.id.latitude_coordinate_warehouse);
        field2 = findViewById(R.id.longitude_coordinate_warehouse);
        done = findViewById(R.id.done_coordinate_warehouse);
        done.setOnClickListener(view -> {
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
                intent.putExtra("isNotStart",true);
                startActivity(intent);
            }
        });
    }
}
