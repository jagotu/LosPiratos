package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.*;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PLANTATION_GENERAL_CAPACITY;
import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PLANTATION_GENERAL_INCREASE;

public class Plantation extends BoardTile implements Plunderable {
    public Plantation(AxialCoordinate location, Board b) {
        super(location, b);
    }

    protected final Resource resource = ResourceReadOnly.MOCK_VALUE.createMutableCopy();

    /**
     * @return final object representing the resource hold by this board. The returned value is always the same.
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    public ResourceReadOnly getCapacity() {
        return PLANTATION_GENERAL_CAPACITY;
    }

    public ResourceReadOnly getIncrease() {
        return PLANTATION_GENERAL_INCREASE;
    }

    @Override
    public void onNextRoundStarted(int roundNo) {
        super.onNextRoundStarted(roundNo);
        getResource().add(getIncrease());
        getResource().clamp(ResourceReadOnly.ZERO, getCapacity());
    }

    @Override
    public boolean mayBeSteppedOn() {
        return true;
    }
}
