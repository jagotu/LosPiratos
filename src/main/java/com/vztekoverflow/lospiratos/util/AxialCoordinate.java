package com.vztekoverflow.lospiratos.util;

import javafx.geometry.Point2D;

/**
 *  Hexagonal grid Axial Coordinate, as defined on https://www.redblobgames.com/grids/hexagons/#coordinates
 */
public class AxialCoordinate {

    public AxialCoordinate(int q, int r) {
        Q = q;
        R = r;
    }

    public int getQ() {
        return Q;
    }

    protected int Q;

    public int getR() {
        return R;
    }


    protected int R;

    /**
     * classical 2D vector addition
     */
    public AxialCoordinate plus(AxialCoordinate arg){
        return new AxialCoordinate(Q+arg.Q,R+arg.R);
    }
    /**
     * classical 2D vector subtraction
     */
    public AxialCoordinate minus(AxialCoordinate arg){
        return new AxialCoordinate(Q-arg.Q,R-arg.R);
    }
    /**
     * classical 2D vector by scalar multiplication
     */
    public AxialCoordinate times(int coeff){
        return new AxialCoordinate(Q*coeff,R*coeff);
    }
    /**
     * classical 2D vector by scalar multiplication
     * @return AxialCoordinate with internal values truncated to int
     */
    public AxialCoordinate times(float coeff){
        return new AxialCoordinate((int)(Q*coeff),(int)(R*coeff));
    }
    /**
     * @return AxialCoordinate where each component is the square of its original value
     */
    public AxialCoordinate squareComponentwise(){
        return new AxialCoordinate((Q*Q),(R*R));
    }
    /**
     * @return minimal number of movements needed to reach @param from @this in an Euclidean geometry
     */
    public int distanceTo(AxialCoordinate arg){
        return distanceTo(arg.Q, arg.R);
    }
    /**
     * @return minimal number of movements needed to reach @param from @this in an Euclidean geometry
     */
    public int distanceTo(int q, int r){
        //the algorithm is explained here: https://www.redblobgames.com/grids/hexagons/#distances
        int S = -(Q+R);
        int bS = -(q+ r);
        return  (Math.abs(Q - q) + Math.abs(R - r) + Math.abs(S - bS)) / 2;
    }

    public CubeCoordinateMutable toCubeCoordinate(){
        return new CubeCoordinateMutable(Q, R, -(Q+R));
    }
    public AxialCoordinateMutable toMutableAxialCoordinate(){
        return new AxialCoordinateMutable(Q, R);
    }

    @Override
    public String toString() {
        return "(" + Q + "," + R + ")";
    }
    public boolean equals(int q, int r){
        return this.Q == q && this.R == r;
    }

    public boolean equals(AxialCoordinate other){
        return Q == other.Q && R == other.R;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!AxialCoordinate.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        AxialCoordinate c = (AxialCoordinate) obj;
        return Q == c.Q && R == c.R;
    }

    @Override
    public int hashCode() {return Integer.hashCode(Q) + Integer.hashCode(R);}

    public static Point2D hexToPixel(AxialCoordinate hexCoords, boolean pointy, double edgeLength) {
        double x, y;
        if (pointy) {
            x = edgeLength * Constants.SQRT_3 * (hexCoords.getQ() + hexCoords.getR() / 2.0);
            y = edgeLength * 3.0 / 2 * hexCoords.getR();
        } else {
            x = edgeLength * 3.0 / 2 * hexCoords.getQ();
            y = edgeLength * Constants.SQRT_3 * (hexCoords.getR() + hexCoords.getQ() / 2.0);
        }
        return new Point2D(x, y);
    }


    public static AxialCoordinate pixelToHex(Point2D coords, boolean pointy, double edgeLength)
    {
        double q, r;
        if (pointy) {
            q = (coords.getX() * Constants.SQRT_3 / 3 - coords.getY() / 3) / edgeLength;
            r = coords.getY() * 2 / 3 / edgeLength;
        } else {
            q = coords.getX() * 2 / 3 / edgeLength;
            r = (-coords.getX() / 3 + Constants.SQRT_3 / 3 * coords.getY()) / edgeLength;
        }
        return hexRound(q, r);
    }

    private static AxialCoordinate hexRound(double x, double z) {
        double y = -x - z;

        int rx = (int) Math.round(x);
        int rz = (int) Math.round(y);
        int ry = (int) Math.round(y);

        double x_diff = Math.abs(rx - x);
        double y_diff = Math.abs(ry - y);
        double z_diff = Math.abs(rz - z);

        if (x_diff > y_diff && x_diff > z_diff) {
            rx = -ry - rz;
        } else if (y_diff <= z_diff) {
            rz = -rx - ry;
        }
        return new AxialCoordinate(rx, rz);
    }

    public static final AxialCoordinate ZERO = new AxialCoordinate(0,0);



}
