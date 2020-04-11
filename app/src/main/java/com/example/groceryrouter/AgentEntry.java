package com.example.groceryrouter;

public class AgentEntry {
    private String ID;
    private String Capacity;
    private String Label;

    public AgentEntry(String ID, String Capacity, String Label) {
        this.ID = ID;
        this.Capacity = Capacity;
        this.Label = Label;
    }

    public String getID() {
        return ID;
    }

    public String getCapacity() {
        return Capacity;
    }

    public String getLabel(){ return Label; }
}
