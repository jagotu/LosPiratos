package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;

public class Port extends BoardTile {
    public Port(AxialCoordinate location, Board board, String portName) {
        super(location, board);
        this.portName = portName;
    }

    private String portName;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public boolean mayBeSteppedOn() {
        return true;
    }

    @Override
    public boolean allowsFighting() {
        return false;
    }
}
