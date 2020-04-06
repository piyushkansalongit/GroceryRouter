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


public class CoordinateInputActivity extends AppCompatActivity {

    String Latitude, Longitude, Demand;
    EditText field1;
    EditText field2;
    EditText field3;
    Button nextCoordinate, done, importFromExcelButton;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_input);

        field1 = findViewById(R.id.latitude_coordinate_input);
        field2 = findViewById(R.id.longitude_coordinate_input);
        field3 = findViewById(R.id.demand_coordinate_input);
        nextCoordinate = findViewById(R.id.nextCoordinate_coordinate_input);
        done = findViewById(R.id.done_coordinate_input);
        nextCoordinate.setOnClickListener(view -> {

            Latitude = field1.getText().toString();
            Longitude = field2.getText().toString();
            Demand = field3.getText().toString();
            if(Latitude.equals("") || Longitude.equals("") || Demand.equals(""))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Please fill all the fields", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                ArrayList<String> deliveryCoordinates = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("deliveryCoordinates");
                Intent  intent = new Intent(CoordinateInputActivity.this, CoordinateInputActivity.class);
                assert deliveryCoordinates != null;
                deliveryCoordinates.add(String.join(" ", Latitude, Longitude));
                intent.putExtra("deliveryCoordinates", deliveryCoordinates);
                startActivity(intent);
            }
        });

        done.setOnClickListener(view -> {
            Latitude = field1.getText().toString();
            Longitude = field2.getText().toString();
            Demand = field3.getText().toString();
            if(Latitude.equals("") || Longitude.equals("") || Demand.equals(""))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Please fill all the fields", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                ArrayList<String> deliveryCoordinates = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("deliveryCoordinates");
                Intent  intent = new Intent(CoordinateInputActivity.this, MainActivity.class);
                assert deliveryCoordinates != null;
                deliveryCoordinates.add(String.join(" ",Latitude,Longitude,Demand));
                intent.putExtra("deliveryCoordinates", deliveryCoordinates);
                startActivity(intent);
            }
        });

        importFromExcelButton = findViewById(R.id.import_excel_coordinate_input);
        importFromExcelButton.setOnClickListener(view -> {
            Intent uploadExcelIntent = new Intent(CoordinateInputActivity.this,UploadExcelActivity.class);
            startActivity(uploadExcelIntent);

        });

    }
}
