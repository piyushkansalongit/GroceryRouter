package com.example.groceryrouter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewListContent extends AppCompatActivity {
    DeliveryCoordinatesDB db;
    ArrayList<DeliveryEntry> deliveryEntryList;
    ListView listView;
    DeliveryEntry deliveryEntry;
    Button back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view);

        db = MainActivity.deliveryCoordinatesDB;
        deliveryEntryList = new ArrayList<>();
        Cursor data = db.showData();
        int numRows = data.getCount();
        if(numRows==0){
            Toast.makeText(ViewListContent.this, "The database is empty!", Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                deliveryEntry = new DeliveryEntry(data.getString(0), data.getString(1), data.getString(2), data.getString(3));
                deliveryEntryList.add(deliveryEntry);
            }
            FourColumn_ListAdapter adapter = new FourColumn_ListAdapter(this, R.layout.list_adapter_view, deliveryEntryList);
            listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }

        back = findViewById(R.id.back_list_view);
        back.setOnClickListener(view -> {
            String Latitude, Longitude, Demand, ID;
            Bundle extras = getIntent().getExtras();
            if(extras!=null)
            {
                Latitude = extras.getString("Latitude");
                Longitude = extras.getString("Longitude");
                Demand = extras.getString("Demand");
                ID = extras.getString("ID");
            }else
                Latitude = Longitude = Demand = ID = "";

            Intent intent = new Intent(ViewListContent.this, CoordinateInputActivity.class);
            intent.putExtra("Latitude", Latitude);
            intent.putExtra("Longitude", Longitude);
            intent.putExtra("Demand", Demand);
            intent.putExtra("ID", ID);
            startActivity(intent);
        });
    }

}
