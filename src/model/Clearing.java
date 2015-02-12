package model;

import java.util.Arrays;

public class Clearing {
    String type;
    String dwelling; // not sure what type this should actually be
    String location; // indicating the location on the board - should this be int?
    Clearing[] connections;

    Clearing(String type, String dwelling, String location) {
        this.type     = type;
        this.dwelling = dwelling;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDwelling() {
        return dwelling;
    }

    public void setDwelling(String dwelling) {
        this.dwelling = dwelling;
    }

    public boolean canChangeClearing(Clearing clearing) {
        // if they're connected
        if (Arrays.asList(connections).contains(clearing)) {
            return true;
        } else {
            return false;
        }
    }

}