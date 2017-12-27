package com.vztekoverflow.lospiratos.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Map {

    public Map(List<MapHexagon> hexagons) {
        this.hexagons.addAll(hexagons);
    }
    public Map() {
    }

    private ListProperty<MapHexagon> hexagons = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty backgroundColor = new SimpleStringProperty("");

    //getters:

    public ObservableList<MapHexagon> getHexagons() {
        return hexagons.get();
    }

    public ListProperty<MapHexagon> hexagonsProperty() {
        return hexagons;
    }

    public String getBackgroundColor() {
        return backgroundColor.get();
    }

    public StringProperty backgroundColorProperty() {
        return backgroundColor;
    }




}

