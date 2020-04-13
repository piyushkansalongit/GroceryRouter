package com.example.groceryrouter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class TSPOutputActivity extends AppCompatActivity{

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
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Bundle extras = getIntent().getExtras();
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.display_map);
        //mapFragment.getMapAsync(this);
        warehouseCoordinate = extras.getString("warehouseCoordinates").split(" ", 2);
        deliveryCoordinatesDB = WelcomeActivity.deliveryCoordinatesDB;
        deliveryAgentsDB = WelcomeActivity.deliveryAgentsDB;
        //done = findViewById(R.id.output_done);
        //done.setOnClickListener(view -> {
          //  Intent intent = new Intent(TSPOutputActivity.this, MainActivity.class);
           // startActivity(intent);
        //});
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
            deliveryAgents.add(agentData.getString(1)+" "+agentData.getString(2));
        }

        String csv_content = "lat_w,long_w,lat_d,long_d,cap_d,id_d,cap_v,id_v\n";
        csv_content += (warehouseCoordinate[0]) + "," + warehouseCoordinate[1] + "," + deliveryCoordinates.get(0).split(" ",4)[0] + "," +
                deliveryCoordinates.get(0).split(" ",4)[1] + "," + deliveryCoordinates.get(0).split(" ",4)[2] + "," + deliveryCoordinates.get(0).split(" ",4)[3] + ","
                + deliveryAgents.get(0).split(" ",2)[0] + "," + deliveryAgents.get(0).split(" ",2)[1]+'\n';

        for (int i = 1; i < Math.max(deliveryAgents.size(), deliveryCoordinates.size()); i++) {
            csv_content += (warehouseCoordinate[0]) + "," + warehouseCoordinate[1];
            if (i < deliveryCoordinates.size())
                csv_content += "," + deliveryCoordinates.get(i).split(" ",4)[0] + "," +
                        deliveryCoordinates.get(i).split(" ",4)[1] + "," + deliveryCoordinates.get(i).split(" ",4)[2] + "," + deliveryCoordinates.get(i).split(" ",4)[3];
            else csv_content += ",x,x,x,x";

            if (i < deliveryAgents.size())
                csv_content += "," + deliveryAgents.get(i).split(" ",2)[0] + "," + deliveryAgents.get(i).split(" ",2)[1];
            else csv_content += ",x,x";
            csv_content+='\n';

        }
        Log.d("csv_content", csv_content);
        try {
            File myfile = File.createTempFile("sample.csv",null,getApplicationContext().getCacheDir());
            FileOutputStream fOut = new FileOutputStream(myfile);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(csv_content);
            osw.close();
            Log.d("file_loc",myfile.getAbsolutePath());
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(myfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }

                br.close();

            }
            catch (Exception e) {
                Log.d("file_content","error");
                //You'll need to add proper error handling here
            }

            Log.d("xx","xx");
            Log.d("file_content",text.toString());
            httpclient client = new httpclient(this);
            client.execute(myfile.getAbsolutePath());
        }
        catch (IOException e)
        {
            Log.d("err_test","Unable to create file");
        }
        catch (Exception e)
        {
            Log.d("error in retrieiving","Please try again");
        }




    }


}
