package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.*;

public abstract class BoardTile implements OnNextRoundStartedListener {

    private AxialCoordinate location;
    private Board board;

    protected BoardTile(AxialCoordinate location, Board board) {
        this.location = location;
        this.board = board;
        board.getGame().addOnNextRoundStartedListener(this);
    }

    public AxialCoordinate getLocation() {
        return location;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void onNextRoundStarted(int roundNo) {
        //do nothing, may be overridden by inheritors
    }

    public abstract boolean mayBeSteppedOn();

    //may be overridden by inheritors
    public boolean allowsFighting(){
        return true;
    }

    //static:

    private static String persistentNamePlantation = "Plantation";
    private static String persistentNamePlantationExtra = "PlantationExtra";
    private static String persistentNamePort = "Port";
    private static String persistentNameSea = "Sea";
    private static String persistentNameShore = "Shore";

    public static String getPersistentName(Class<? extends BoardTile> tileType) {
        if (tileType.equals(PlantationExtra.class)) {
            return persistentNamePlantationExtra;
        }
        if (tileType.equals(Plantation.class)) {
            return persistentNamePlantation;
        }
        if (tileType.equals(Port.class)) {
            return persistentNamePort;
        }
        if (tileType.equals(Sea.class)) {
            return persistentNameSea;
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
    static BoardTile createInstanceFromPersistentName(String tileName, AxialCoordinate location, Board owner) {
        if (tileName == null || tileName.isEmpty()) {
            Warnings.makeWarning("BoardTile.createInstanceFromPersistentName()", "Empty or null tile name: " + tileName);
            return null;
        }
        if (tileName.equalsIgnoreCase(persistentNamePlantation)) {
            return new Plantation(location, owner);
        }
        if (tileName.equalsIgnoreCase(persistentNamePlantationExtra)) {
            return new PlantationExtra(location, owner);
        }
        if (tileName.equalsIgnoreCase(persistentNamePort)) {
            return new Port(location, owner);
        }
        if (tileName.equalsIgnoreCase(persistentNameSea)) {
            return new Sea(location, owner);
        }
        if (tileName.equalsIgnoreCase(persistentNameShore)) {
            return new Shore(location, owner);
        }
        //else
        Warnings.makeWarning("ShipEnhancement.createInstanceFromPersistentName()", "Unknown enhancement: " + tileName);
        return null;

    }

}
