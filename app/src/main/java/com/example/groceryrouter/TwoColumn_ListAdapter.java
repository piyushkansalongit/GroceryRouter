package com.example.groceryrouter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TwoColumn_ListAdapter extends ArrayAdapter<AgentEntry> {
    private LayoutInflater mInflater;
    private ArrayList<AgentEntry> agentEntryList;
    private int mViewResourceId;

    public TwoColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<AgentEntry> agentEntryList) {
        super(context, textViewResourceId, agentEntryList);
        this.agentEntryList = agentEntryList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);
        AgentEntry agentEntry = agentEntryList.get(position);

        if (agentEntry != null) {
            TextView id = convertView.findViewById(R.id.idCol2);
            TextView capacity = convertView.findViewById(R.id.capacityCol);

            if (id != null) {
                id.setText(agentEntry.getID());
                capacity.setText(agentEntry.getCapacity());
            }
        }
        return convertView;
    }
}