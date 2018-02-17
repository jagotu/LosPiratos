package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.*;

public class Plantation extends BoardTile implements Plunderable {
    public Plantation(AxialCoordinate location, Board b) {
        super(location, b);
    }

    public final ResourceReadOnly PLANTATION_GENERAL_CAPACITY = new ResourceReadOnly();
    public final ResourceReadOnly PLANTATION_GENERAL_INCREASE = new ResourceReadOnly();

    protected final Resource resource = new Resource();

    /**
     *
     * @return final object representing the resource hold by this board. The returned value is always the same.
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    public ResourceReadOnly getCapacity(){
        return PLANTATION_GENERAL_CAPACITY;
    }

    public ResourceReadOnly getIncrease(){
        return  PLANTATION_GENERAL_INCREASE;
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
