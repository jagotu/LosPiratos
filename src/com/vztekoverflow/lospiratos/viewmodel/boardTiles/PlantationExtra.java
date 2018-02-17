package com.vztekoverflow.lospiratos.viewmodel.boardTiles;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;

public class PlantationExtra extends Plantation {
    public final ResourceReadOnly PLANTATION_EXTRA_CAPACITY = new ResourceReadOnly();
    public final ResourceReadOnly PLANTATION_EXTRA_INCREASE = new ResourceReadOnly();

    public PlantationExtra(AxialCoordinate location, Board b) {
        super(location, b);
    }

    /**
     *
     * @return final object representing the resource hold by this board. The returned value is always the same.
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public ResourceReadOnly getCapacity(){
        return PLANTATION_EXTRA_CAPACITY;
    }

    @Override
    public ResourceReadOnly getIncrease(){
        return  PLANTATION_EXTRA_INCREASE;
    }
}
