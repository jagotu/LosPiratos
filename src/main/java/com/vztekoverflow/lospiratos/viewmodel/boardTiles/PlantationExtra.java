package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PLANTATION_EXTRA_CAPACITY;
import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PLANTATION_EXTRA_QUOTIENT;

public class PlantationExtra extends Plantation {


    public PlantationExtra(AxialCoordinate location, Board b) {
        super(location, b);
    }

    @Override
    public ResourceReadOnly getCapacity() {
        return PLANTATION_EXTRA_CAPACITY;
    }

    @Override
    public float getQuotient() {
        return PLANTATION_EXTRA_QUOTIENT;
    }

    @Override
    protected ResourceReadOnly getInitialState() {
        return PLANTATION_EXTRA_CAPACITY;
    }
}
