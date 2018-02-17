package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;

public class Sea extends BoardTile {
    public Sea(AxialCoordinate location, Board b) {
        super(location, b);
    }
    @Override
    public boolean mayBeSteppedOn() {
        return true;
    }
}
