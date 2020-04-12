package com.example.groceryrouter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class TSPOutputActivity extends AppCompatActivity implements TaskLoadedCallback, OnMapReadyCallback {

    String[] warehouseCoordinate;
    DeliveryCoordinatesDB deliveryCoordinatesDB;
    DeliveryAgentsDB deliveryAgentsDB;
    GoogleMap mMap;
    Button done;
    Polyline currentPolyLine;
    ArrayList<LatLng> locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_s_p_output);

        Bundle extras = getIntent().getExtras();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.display_map);
        mapFragment.getMapAsync(this);
        warehouseCoordinate = extras.getString("warehouseCoordinates").split(" ", 2);
        deliveryCoordinatesDB = WelcomeActivity.deliveryCoordinatesDB;
        deliveryAgentsDB = WelcomeActivity.deliveryAgentsDB;
        done = findViewById(R.id.output_done);
        done.setOnClickListener(view -> {
            Intent intent = new Intent(TSPOutputActivity.this, MainActivity.class);
            startActivity(intent);
        });
        // Importing the data from databases to array lists
        Cursor deliveryData = deliveryCoordinatesDB.showData();
        Cursor agentData = deliveryAgentsDB.showData();

        ArrayList<ArrayList<Double>> deliveryList = new ArrayList<>();
        //ArrayList<Double> agentList = new ArrayList<>();
        ArrayList<String> deliveryCoordinates = new ArrayList<>();
        ArrayList<String> deliveryAgents = new ArrayList<>();

        while (deliveryData.moveToNext()) {
            ArrayList<Double> temp = new ArrayList<>();
            String latitude = deliveryData.getString(1);
            String longitude = deliveryData.getString(2);
            String demand = deliveryData.getString(3);
            String name = deliveryData.getString(4);
            temp.add(Double.valueOf(latitude));
            temp.add(Double.valueOf(longitude));
            temp.add(Double.valueOf(demand));
            deliveryCoordinates.add(latitude + " " + longitude + " " + demand + " " + name);
        }

        while (agentData.moveToNext()) {
            deliveryAgents.add(agentData.getString(1)+agentData.getString(2));
        }

        String csv_content = "lat_w,long_w,lat_d,long_d,cap_d,id_d,cap_v,id_v\n";
        csv_content += (warehouseCoordinate[0]).split(" ")[0] + "," + warehouseCoordinate[0].split(" ")[1] + "," + deliveryCoordinates.get(0).split(" ")[0] + "," +
                deliveryCoordinates.get(0).split(" ")[1] + "," + deliveryCoordinates.get(0).split(" ")[2] + "," + deliveryCoordinates.get(0).split(" ")[3] + ","
                + deliveryAgents.get(0).split(" ")[0] + "," + deliveryAgents.get(0).split(" ")[1];

        for (int i = 1; i < Math.max(deliveryAgents.size(), deliveryCoordinates.size()); i++) {
            csv_content += (warehouseCoordinate[0]).split(" ")[0] + "," + warehouseCoordinate[0].split(" ")[1];
            if (i < deliveryCoordinates.size())
                csv_content += "," + deliveryCoordinates.get(0).split(" ")[0] + "," +
                        deliveryCoordinates.get(0).split(" ")[1] + "," + deliveryCoordinates.get(0).split(" ")[2] + "," + deliveryCoordinates.get(0).split(" ")[3];
            else csv_content += ",x,x,x,x";

            if (i < deliveryAgents.size())
                csv_content += "," + deliveryAgents.get(0).split(" ")[0] + "," + deliveryAgents.get(0).split(" ")[1];
            else csv_content += ",x,x";


        }
        Log.d("csv_content", csv_content);



        // Find the route and location between each pair of locations
        locations = new ArrayList<>();
        locations.add(new LatLng(Double.parseDouble(warehouseCoordinate[0]), Double.parseDouble(warehouseCoordinate[1])));
        for(int i=0; i<deliveryList.size(); i++)
        {
            locations.add(new LatLng(deliveryList.get(i).get(0), deliveryList.get(i).get(1)));
        }

    }

    private String getURL(LatLng origin, LatLng destination, String directionMode)
    {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode="+directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key" + getString(R.string.map_key);
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyLine!=null)
            currentPolyLine.remove();
        currentPolyLine = mMap.addPolyline((PolylineOptions) values[0]);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for(int i=0; i<locations.size()-1; i++)
        {
            for(int j=i+1; j<locations.size(); j++)
            {
                mMap.addMarker(new MarkerOptions().position(locations.get(i)).title("Start"));
                mMap.addMarker(new MarkerOptions().position(locations.get(j)).title("End"));
                String url = getURL(locations.get(i), locations.get(j), "driving");
                new FetchURL(TSPOutputActivity.this).execute(url, "driving");
            }
        }
    }

}
