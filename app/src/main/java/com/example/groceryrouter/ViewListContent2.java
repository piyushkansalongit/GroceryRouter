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

public class ViewListContent2 extends AppCompatActivity {
    DeliveryAgentsDB db;
    ArrayList<AgentEntry> agentEntryList;
    ListView listView;
    AgentEntry agentEntry;
    Button back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view2);

        db = MainActivity.deliveryAgentsDB;
        agentEntryList = new ArrayList<>();
        Cursor data = db.showData();
        int numRows = data.getCount();
        if(numRows==0){
            Toast.makeText(ViewListContent2.this, "The database is empty!", Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                agentEntry = new AgentEntry(data.getString(0), data.getString(1), data.getString(2));
                agentEntryList.add(agentEntry);
            }
            TwoColumn_ListAdapter adapter = new TwoColumn_ListAdapter(this, R.layout.list_adapter_view2, agentEntryList);
            listView = findViewById(R.id.listView2);
            listView.setAdapter(adapter);
        }

        back = findViewById(R.id.back_list_view2);
        back.setOnClickListener(view -> {
            String Capacity, ID, Label;
            Bundle extras = getIntent().getExtras();
            if(extras!=null)
            {
                Capacity = extras.getString("Capacity");
                ID = extras.getString("ID");
                Label = extras.getString("Label");
            }else
                Capacity = ID = Label = "";
            Intent intent = new Intent(ViewListContent2.this, DeliveryAgentActivity.class);
            intent.putExtra("Capacity", Capacity);
            intent.putExtra("ID", ID);
            intent.putExtra("Label", Label);
            startActivity(intent);
        });
    }

}
