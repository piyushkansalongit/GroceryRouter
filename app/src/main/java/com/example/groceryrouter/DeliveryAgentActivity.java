package com.example.groceryrouter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class DeliveryAgentActivity extends AppCompatActivity {

    String Capacity;
    String ID;
    EditText field1;
    EditText field2;
    Button addButton, updateButton, deleteButton, viewAgentsButton, doneAgentsButton, importFromExcelButton;
    DeliveryAgentsDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_agent);

        field1 = findViewById(R.id.capacity_agent_delivery);
        field2 = findViewById(R.id.id_agent_delivery);
        addButton = findViewById(R.id.add_agent_delivery);
        updateButton = findViewById(R.id.update_agent_delivery);
        deleteButton = findViewById(R.id.delete_agent_delivery);
        viewAgentsButton = findViewById(R.id.view_agent_delivery);
        doneAgentsButton = findViewById(R.id.done_agent_delivery);
        importFromExcelButton = findViewById(R.id.import_excel_agent_delivery);

        //Database
        db = MainActivity.deliveryAgentsDB;

        //OnClickListeners
        addButton.setOnClickListener(view -> addHandle());
        updateButton.setOnClickListener(view -> updateHandle());
        deleteButton.setOnClickListener(view -> deleteHandle());
        viewAgentsButton.setOnClickListener(view -> viewHandle());
        doneAgentsButton.setOnClickListener(view-> doneHandle());
        importFromExcelButton.setOnClickListener(view -> importHandle());
    }

    private void addHandle(){
        Capacity = field1.getText().toString();
        ID = field1.getText().toString();
        if(Capacity==null)
            toastMessage("Please fill all the fields correctly");
        else{
            boolean retFlag = db.addData(Capacity);
            if(!retFlag)
                toastMessage("Something went wrong! Please try again.");
            else
                toastMessage("Data Successfully inserted.");

        }
        field1.setText("");
        field2.setText("");
    }

    private void updateHandle(){
        Capacity = field1.getText().toString();
        ID = field1.getText().toString();
        if(Capacity==null)
            toastMessage("Please fill all the fields correctly");
        else if(ID==null)
            toastMessage("You must enter an ID to update");
        else{
            boolean retFlag = db.updateData(ID, Capacity);
            if(!retFlag)
                toastMessage("Something went wrong! Please try again.");
            else
                toastMessage("Data Successfully Updated.");

        }
        field1.setText("");
        field2.setText("");
    }

    private void deleteHandle(){
        Capacity = field1.getText().toString();
        ID = field1.getText().toString();
        if(ID==null)
            toastMessage("Please enter an ID to delete");
        else{
            Integer retFlag = db.deleteData(ID);
            if(retFlag > 0)
                toastMessage("Data Successfully Deleted.");
            else
                toastMessage("Something Went Wrong! Please try again.");

        }
        field1.setText("");
        field2.setText("");
    }

    private void viewHandle(){
        Cursor data = db.showData();
        if(data.getCount()==0)
        {
            display("Error", "No Data Found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(data.moveToNext()){
            buffer.append("ID: "+data.getString(0) + "\n");
            buffer.append("Latitude: "+data.getString(1) + "\n");
        }
        display("All Stored Data: ", buffer.toString());
    }

    private void doneHandle(){
        Intent  intent = new Intent(DeliveryAgentActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void importHandle(){
        Intent uploadExcelIntent = new Intent(DeliveryAgentActivity.this,UploadExcelActivity.class);
        uploadExcelIntent.putExtra("c_max","1");
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
