package com.vztekoverflow.lospiratos.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MapHexagon {

    public MapHexagon(IntegerProperty coordinateQ, IntegerProperty coordinateR) {
        this.coordinateQ = coordinateQ;
        this.coordinateR = coordinateR;
    }

    public IntegerProperty coordinateQ;
    public IntegerProperty coordinateR;

    ///e.g. sea, shore, port, treasure etc
    public StringProperty content = new SimpleStringProperty("");

    //empty for sea and shore, used usually for ports
    public StringProperty contentName = new SimpleStringProperty("");

    //empty for sea and shore, used usually for treasures
    public IntegerProperty contentIntParam = new SimpleIntegerProperty(0);
}
