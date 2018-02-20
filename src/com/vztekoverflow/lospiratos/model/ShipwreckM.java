package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public class ShipwreckM {
    public final ResourceM resource = new ResourceM();
    public final AxialCoordinate position;

    public ShipwreckM(AxialCoordinate position) {
        this.position = position;
    }
}
