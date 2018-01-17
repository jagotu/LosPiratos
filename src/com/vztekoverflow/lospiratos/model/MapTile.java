package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;

/*
 * Immutable class
 */
public class MapTile {

    public MapTile(AxialCoordinate location) {
        this.location = location;
    }
    public MapTile(AxialCoordinate location, String content) {
        this.location = location;
        this.content = content;
    }


    private final AxialCoordinate location;

    ///e.g. sea, shore, port, treasure etc
    private String content = "";

    private MapProperty<String, String> customExtensions = new SimpleMapProperty<>();

    public final AxialCoordinate getLocation() {
        return location;
    }

    public String getContent() {
        return content;
    }

    public ObservableMap<String, String> getCustomExtensions() {
        return customExtensions.get();
    }

    public MapProperty<String, String> customExtensionsProperty() {
        return customExtensions;
    }

}
