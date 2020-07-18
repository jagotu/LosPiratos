package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

/**
 * Immutable class
 */
public class MapTile {

    public MapTile(AxialCoordinate location) {
        this.location = location;
        plantationsResource = null;
        this.portName = null;
    }
    public MapTile(AxialCoordinate location, String content) {
        this.location = location;
        this.content = content;
        plantationsResource = null;
        this.portName = null;
    }
    public MapTile(AxialCoordinate location, String content, ResourceM plantationsResource) {
        this.location = location;
        this.content = content;
        this.plantationsResource = plantationsResource;
        this.portName = null;
    }
    public MapTile(AxialCoordinate location, String content, String portName) {
        this.location = location;
        this.content = content;
        this.portName = portName;
        plantationsResource = null;
    }

    private final AxialCoordinate location;

    public final ResourceM plantationsResource;

    ///e.g. sea, shore, port, treasure etc
    private String content = "";

    /// defined if content == "Port"
    private final String portName;

    public String getPortName() {
        return portName;
    }

    public final AxialCoordinate getLocation() {
        return location;
    }

    public String getContent() {
        return content;
    }

}
