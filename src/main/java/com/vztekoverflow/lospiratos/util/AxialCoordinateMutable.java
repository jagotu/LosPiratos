package com.vztekoverflow.lospiratos.util;

/**
 * As its parent, and in addition its value may change.
 */
public class AxialCoordinateMutable extends AxialCoordinate {

    public AxialCoordinateMutable(int q, int r) {
        super(q,r);
    }
    public void setR(int r) {
        R = r;
    }
    public void setQ(int q) {
        Q = q;
    }
    public AxialCoordinateMutable newCopy(){
        return new AxialCoordinateMutable(Q, R);
    }

}
