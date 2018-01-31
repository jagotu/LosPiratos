package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.BoardTiles.*;

public abstract class BoardTile {

    private AxialCoordinate location;

    protected BoardTile(AxialCoordinate location) {
        this.location = location;
    }

    public AxialCoordinate getLocation() {
        return location;
    }
    //static:

    private static String persistentNamePlantation = "Plantation";
    private static String persistentNamePort = "Port";
    private static String persistentNameSea = "Sea";
    private static String persistentNameShipwreck = "Shipwreck";
    private static String persistentNameShore = "Shore";

    public static String getPersistentName(Class<? extends BoardTile> tileType) {
        if (tileType.equals(Plantation.class)) {
            return persistentNamePlantation;
        }
        if (tileType.equals(Port.class)) {
            return persistentNamePort;
        }
        if (tileType.equals(Sea.class)) {
            return persistentNameSea;
        }
        if (tileType.equals(Shipwreck.class)) {
            return persistentNameShipwreck;
        }
        if (tileType.equals(Shore.class)) {
            return persistentNameShore;
        }
        //else
        Warnings.panic("BoardTile.getPersistentName()", "Unknown tileType class: " + tileType.getCanonicalName());
        return "UnknownTileType";
    }

    /**
     * @return null if the {@code tileName} is unknown
     */
    public static BoardTile createInstanceFromPersistentName(String tileName, AxialCoordinate location) {
        if (tileName == null || tileName.isEmpty()) {
            Warnings.makeWarning("BoardTile.createInstanceFromPersistentName()", "Empty or null tile name: " + tileName);
            return null;
        }
        if (tileName.equalsIgnoreCase(persistentNamePlantation)) {
            return new Plantation(location);
        }
        if (tileName.equalsIgnoreCase(persistentNamePort)) {
            return new Port(location);
        }
        if (tileName.equalsIgnoreCase(persistentNameSea)) {
            return new Sea(location);
        }
        if (tileName.equalsIgnoreCase(persistentNameShipwreck)) {
            return new Shipwreck(location);
        }
        if (tileName.equalsIgnoreCase(persistentNameShore)) {
            return new Shore(location);
        }
        //else
        Warnings.makeWarning("ShipEnhancement.createInstanceFromPersistentName()", "Unknown enhancement: " + tileName);
        return null;

    }
}
