package com.vztekoverflow.lospiratos.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Map {
    public ListProperty<MapHexagon> hexagons = new SimpleListProperty<>();
    public StringProperty backgroundColor = new SimpleStringProperty("");
}

