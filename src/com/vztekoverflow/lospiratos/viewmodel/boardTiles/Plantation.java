package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.*;

public class Plantation extends BoardTile implements Plunderable {
    public Plantation(AxialCoordinate location, Board b) {
        super(location, b);
    }

    final static ResourceReadOnly PLANTATION_GENERAL_CAPACITY = new ResourceReadOnly(100, 0, 0, 0, 0, 0);
    final static ResourceReadOnly PLANTATION_GENERAL_INCREASE = new ResourceReadOnly(30, 0, 0, 0, 0, 0);

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
