package com.example.groceryrouter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FourColumn_ListAdapter extends ArrayAdapter<DeliveryEntry> {

    private LayoutInflater mInflater;
    private ArrayList<DeliveryEntry> deliveryEntryList;
    private int mViewResourceId;

    public FourColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<DeliveryEntry> deliveryEntryList)
    {
        super(context, textViewResourceId, deliveryEntryList);
        this.deliveryEntryList = deliveryEntryList;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents)
    {
         convertView = mInflater.inflate(mViewResourceId, null);
         DeliveryEntry deliveryEntry = deliveryEntryList.get(position);

         if(deliveryEntry!=null)
         {
             TextView id = convertView.findViewById(R.id.idCol);
             TextView latitude = convertView.findViewById(R.id.latitudeCol);
             TextView longitude = convertView.findViewById(R.id.longitudeCol);
             TextView demand = convertView.findViewById(R.id.demandCol);
             TextView label = convertView.findViewById(R.id.labelCol);
             if(id!=null){
                 id.setText(deliveryEntry.getID());
                 latitude.setText(deliveryEntry.getLatitude());
                 longitude.setText(deliveryEntry.getLongitude());
                 demand.setText(deliveryEntry.getDemand());
                 label.setText(deliveryEntry.getLabel());
             }
         }
         return convertView;
    }

}
