package com.vztekoverflow.lospiratos.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class Map {
    public ListProperty<MapHexagon> hexagons = new SimpleListProperty<>(FXCollections.observableArrayList());
    public StringProperty backgroundColor = new SimpleStringProperty("");

    public Map(List<MapHexagon> hexagons) {
        this.hexagons.addAll(hexagons);
    }
    public Map() {
    }
}

