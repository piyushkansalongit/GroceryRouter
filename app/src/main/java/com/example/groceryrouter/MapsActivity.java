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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
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

        Places.initialize(getApplicationContext(),getString(R.string.map_key));
        PlacesClient placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteSupportFragment.setCountry("IN");
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        done = findViewById(R.id.back_fromMap);
        searchView = findViewById(R.id.sv_location);

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                moveCamera(latLng);
            }
            @Override public void onError(@NonNull Status status) {}
        });

        fetchLastLocation();

        done.setOnClickListener(view -> {
            doneHandle();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try
                {
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
                moveCamera(latLng);
                return false;
            }
            @Override public boolean onQueryTextChange(String newText) { return false; }});
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng = new LatLng(dLatitude, dLongitude);
        moveCamera(latlng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            }
        }
    }


    // Listeners
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doneHandle(){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        Intent intent;
        if(extras.getBoolean("warehouseActivity"))
            intent = new Intent(MapsActivity.this, CoordinateWarehouseActivity.class);
        else
            intent = new Intent(MapsActivity.this, CoordinateInputActivity.class);
        intent.putExtra("Latitude", String.valueOf(dLatitude));
        intent.putExtra("Longitude", String.valueOf(dLongitude));
        startActivity(intent);
    }

    private void taskSuccessHandle(Location location) {
        if(location != null){
            currentLocation = location;
            dLatitude = currentLocation.getLatitude();
            dLongitude = currentLocation.getLongitude();
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
            toastMessage("Coordinates Set at current Location");
        }
    }

    // Helper Functions
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

    private void moveCamera(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("selectedPos"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18), 5000, null);
    }

    private void toastMessage(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }
}