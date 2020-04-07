package com.example.groceryrouter;

public class AgentEntry {
    private String ID;
    private String Capacity;

    public AgentEntry(String ID, String Capacity) {
        this.ID = ID;
        this.Capacity = Capacity;
    }

    public String getID() {
        return ID;
    }

    public String getCapacity() {
        return Capacity;
    }
}
