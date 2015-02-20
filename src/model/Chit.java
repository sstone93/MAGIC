package model;

import utils.Utility.Actions;

public class Chit {
    Actions type;
    String   name;
    Clearing location;
    boolean  visible;
    // also want the image, not sure what it should be stored as

    public String getName() {
        return name;
    }

    public void setLocation(Clearing newLocation) {
        location = newLocation;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Actions getType() {
        return type;
    }

    public void setType(Actions type) {
        this.type = type;
    }

}