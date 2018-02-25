package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.*;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PLANTATION_GENERAL_CAPACITY;
import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PLANTATION_GENERAL_QUOTIENT;

public class Plantation extends BoardTile implements Plunderable {
    public Plantation(AxialCoordinate location, Board b) {
        super(location, b);
    }

    protected final Resource resource = getInitialState().createMutableCopy();

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

    protected ResourceReadOnly getInitialState()
    {
        return PLANTATION_GENERAL_CAPACITY;
    }

    public float getQuotient() {
        return PLANTATION_GENERAL_QUOTIENT;
    }

    @Override
    public void onNextRoundStarted(int roundNo) {
        super.onNextRoundStarted(roundNo);
        ResourceReadOnly H = getCapacity().plus(getResource().times(getQuotient())).timesComponentWise(getCapacity());
        ResourceReadOnly L = getCapacity().times(getQuotient()).plus(getResource());
        getResource().setAll(H.divideComponentWise(L));
        //getResource().clamp(ResourceReadOnly.ZERO, getCapacity());
    }

    @Override
    public boolean mayBeSteppedOn() {
        return true;
    }
}
