package com.vztekoverflow.lospiratos.util;


/*
 *  Hexagonal grid Cube Coordinate, as defined on https://www.redblobgames.com/grids/hexagons/#coordinates
 */
public class CubeCoordinateMutable extends AxialCoordinateMutable {
    /*
     * Creates new CubeCoordinateMutable.
     * If (q + r + s != 0), throws IllegalArgumentException
     */
    public CubeCoordinateMutable(int q, int r, int s) throws IllegalArgumentException {
        super(q, r);
        if(q + r + s != 0) throw new IllegalArgumentException();
    }

    public int getS() {
        return -(R + Q);
    }

    @Override
    public String toString() {
        return "(" + Q + "," + R + "," + getS() + ")";
    }
    public CubeCoordinateMutable newCopy(){
        return new CubeCoordinateMutable(Q, R, -(Q+R));
    }
}
