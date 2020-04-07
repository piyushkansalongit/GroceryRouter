package com.example.groceryrouter;

public class DeliveryEntry {
    private String ID;
    private String Latitude;
    private String Longitude;
    private String Demand;

    public DeliveryEntry(String ID, String Latitude, String Longitude, String Demand)
    {
        this.ID = ID;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Demand= Demand;
    }

    public String getID() {
        return ID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getDemand() {
        return Demand;
    }
}
