package com.example.groceryrouter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CoordinateInputActivity extends AppCompatActivity {

    // Variable Declarations
    String Latitude, Longitude, Demand;
    String ID;
    EditText field1;
    EditText field2;
    EditText field3;
    EditText field4;
    Button addCoordinate, updateCoordinate, deleteCoordinate, viewInputs, done, googleMap, importFromExcelButton;
    DeliveryCoordinatesDB db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_input);

        // Input Fields
        field1 = findViewById(R.id.latitude_coordinate_input);
        field2 = findViewById(R.id.longitude_coordinate_input);
        field3 = findViewById(R.id.demand_coordinate_input);
        field4 = findViewById(R.id.id_coordinate_input);


        //Database
        db =  MainActivity.deliveryCoordinatesDB;

        //Buttons
        addCoordinate = findViewById(R.id.add_coordinate_input);
        updateCoordinate = findViewById(R.id.update_coordinate_input);
        deleteCoordinate = findViewById(R.id.delete_coordinate_input);
        viewInputs = findViewById(R.id.view_coordinate_input);
        done = findViewById(R.id.done_coordinate_input);
        importFromExcelButton = findViewById(R.id.import_excel_coordinate_input);
        googleMap = findViewById(R.id.import_from_google_maps_coordinate_input);

        // OnClickListeners
        addCoordinate.setOnClickListener(view -> addHandle());
        updateCoordinate.setOnClickListener(view -> updateHandle());
        deleteCoordinate.setOnClickListener(view -> deleteHandle());
        viewInputs.setOnClickListener(view -> viewHandle());
        done.setOnClickListener(view -> doneHandle());
        importFromExcelButton.setOnClickListener(view -> importHandle());
        googleMap.setOnClickListener(view -> mapHandle());
    }

    private void addHandle(){
        Latitude = field1.getText().toString();
        Longitude = field2.getText().toString();
        Demand = field3.getText().toString();
        if(Latitude.equals("") || Longitude.equals("") || Demand.equals(""))
            toastMessage("Please fill all the fields correctly");
        else{
            boolean retFlag = db.addData(Latitude, Longitude, Demand);
            if(!retFlag)
                toastMessage("Something went wrong! Please try again.");
            else
                toastMessage("Data Successfully inserted.");
        }
        field1.setText("");
        field2.setText("");
        field3.setText("");
        field4.setText("");
    }

    private void updateHandle(){
        Latitude = field1.getText().toString();
        Longitude = field2.getText().toString();
        Demand = field3.getText().toString();
        ID = field4.getText().toString();
        if(Latitude.equals("") || Longitude.equals("") || Demand.equals(""))
            toastMessage("Please fill all the fields correctly");
        else if(ID==null)
            toastMessage("You must enter an ID to update");
        else{
            boolean retFlag = db.updateData(ID, Latitude, Longitude, Demand);
            if(!retFlag)
                toastMessage("Something went wrong! Please try again.");
            else
                toastMessage("Data Successfully Updated.");

        }
        field1.setText("");
        field2.setText("");
        field3.setText("");
        field4.setText("");
    }

    private void deleteHandle(){
        ID = field4.getText().toString();
        if(ID.equals(""))
            toastMessage("Please enter an ID to delete");
        else{
            Integer retFlag = db.deleteData(ID);
            if(retFlag > 0)
                toastMessage("Data Successfully Deleted.");
            else
                toastMessage("ID not present in the database.");

        }
        field1.setText("");
        field2.setText("");
        field3.setText("");
        field4.setText("");
    }

    private void viewHandle(){
        Log.d("view","view is pressed");
        Cursor data = db.showData();
        if(data.getCount()==0)
        {
            display("Error", "No Data Found");
            return;
        }
//        StringBuffer buffer = new StringBuffer();
//        while(data.moveToNext()){
//            buffer.append("ID: "+data.getString(0) + "\n");
//            buffer.append("Latitude: "+data.getString(1) + "\n");
//            buffer.append("Longitude: "+data.getString(2) + "\n");
//            buffer.append("Demand: "+data.getString(3) + "\n");
//
//        }
//        display("All Stored Data: ", buffer.toString());
        Intent displayIntent = new Intent(CoordinateInputActivity.this, ViewListContent.class);
        startActivity(displayIntent);
    }

    private void doneHandle()
    {
        Intent  intent = new Intent(CoordinateInputActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void mapHandle(){
        Intent intent = new Intent(CoordinateInputActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    private void importHandle()
    {
        Intent uploadExcelIntent = new Intent(CoordinateInputActivity.this,UploadExcelActivity.class);
        uploadExcelIntent.putExtra("c_max","3");
        startActivity(uploadExcelIntent);
    }
    private void toastMessage(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void display(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
