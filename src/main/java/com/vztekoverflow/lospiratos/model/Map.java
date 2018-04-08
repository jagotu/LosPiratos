package com.vztekoverflow.lospiratos.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Map {

    public Map(List<MapTile> tiles) {
        this.tiles.addAll(tiles);
    }
    public Map() {
    }

    private ListProperty<MapTile> tiles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty backgroundColor = new SimpleStringProperty("");

    //getters:

    public ObservableList<MapTile> getTiles() {
        return tiles.get();
    }

    public ListProperty<MapTile> tilesProperty() {
        return tiles;
    }

    public String getBackgroundColor() {
        return backgroundColor.get();
    }

    public StringProperty backgroundColorProperty() {
        return backgroundColor;
    }


}

