package com.example.groceryrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class DeliveryAgentActivity extends AppCompatActivity {

    String Capacity;
    EditText field1;
    Button nextAgentButton, enteredAgentsDataButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_agent);
        field1 = findViewById(R.id.capacity_agent_delivery);
        nextAgentButton = findViewById(R.id.nextAgent_agent_delivery);
        enteredAgentsDataButton = findViewById(R.id.done_agent_delivery);

        nextAgentButton.setOnClickListener(view -> {

            Capacity = field1.getText().toString();
            if(Capacity.equals(""))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Please fill the field", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                ArrayList<String> deliveryAgents = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("deliveryAgents");
                Intent intent = new Intent(DeliveryAgentActivity.this, DeliveryAgentActivity.class);
                assert deliveryAgents != null;
                deliveryAgents.add(Capacity);
                intent.putExtra("deliveryAgents", deliveryAgents);
                startActivity(intent);
            }
        });
        enteredAgentsDataButton.setOnClickListener(view->{

            Capacity = field1.getText().toString();
            if(Capacity.equals(""))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Please fill the field", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                ArrayList<String> deliveryAgents = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("deliveryAgents");
                Intent intent = new Intent(DeliveryAgentActivity.this, MainActivity.class);
                assert deliveryAgents != null;
                deliveryAgents.add(Capacity);
                intent.putExtra("deliveryAgents", deliveryAgents);
                startActivity(intent);
            }
        });
    }
}
