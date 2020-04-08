package com.example.groceryrouter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private double dLatitude = 0.0;
    private double dLongitude = 0.0;
    Button done;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SearchView searchView;
    private static final int REQUEST_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        done = findViewById(R.id.back_fromMap);
        done.setOnClickListener(view -> {
            doneHandle();
        });

        searchView = findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try{
                    addressList = geocoder.getFromLocationName(location, 1);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                assert addressList != null;
                Address address = addressList.get(0);
                dLatitude = address.getLatitude();
                dLongitude =  address.getLongitude();
                LatLng latLng = new LatLng(dLatitude, dLongitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("selectedPos"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18), 5000, null);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng = new LatLng(dLatitude, dLongitude);
        mMap.addMarker(new MarkerOptions().position(latlng).title("currentPos"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,18),5000, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            }
        }
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this::taskSuccessHandle);
    }

    private void taskSuccessHandle(Location location) {
        if(location != null){
            currentLocation = location;
            dLatitude = currentLocation.getLatitude();
            dLongitude = currentLocation.getLongitude();
            Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doneHandle(){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        if(extras.getBoolean("warehouseActivity")) {
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            intent.putExtra("warehouseCoordinates", String.join(" ", String.valueOf(dLatitude), String.valueOf(dLatitude)));
            startActivity(intent);
        }else{
            Intent intent = new Intent(MapsActivity.this, CoordinateInputActivity.class);
            intent.putExtra("Latitude", String.valueOf(dLatitude));
            intent.putExtra("Longitude", String.valueOf(dLongitude));
            startActivity(intent);
        }
    }
}
