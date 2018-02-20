package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

/**
 * Immutable class
 */
public class MapTile {

    public MapTile(AxialCoordinate location) {
        this.location = location;
        plantationsResource = null;
    }
    public MapTile(AxialCoordinate location, String content) {
        this.location = location;
        this.content = content;
        plantationsResource = null;
    }
    public MapTile(AxialCoordinate location, String content, ResourceM plantationsResource) {
        this.location = location;
        this.content = content;
        this.plantationsResource = plantationsResource;
    }

    private final AxialCoordinate location;

    public final ResourceM plantationsResource;


    ///e.g. sea, shore, port, treasure etc
    private String content = "";

    public final AxialCoordinate getLocation() {
        return location;
    }

    public String getContent() {
        return content;
    }

}
