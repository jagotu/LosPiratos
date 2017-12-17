package com.vztekoverflow.lospiratos.util;

import javafx.beans.property.ObjectProperty;

/*
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

    /*
     * classical 2D vector addition
     */
    public AxialCoordinate plus(AxialCoordinate arg){
        return new AxialCoordinate(Q+arg.Q,R+arg.R);
    }
    /*
     * classical 2D vector substraction
     */
    public AxialCoordinate minus(AxialCoordinate arg){
        return new AxialCoordinate(Q-arg.Q,R-arg.R);
    }
    /*
     * classical 2D vector by scalar multiplication
     */
    public AxialCoordinate times(int coeff){
        return new AxialCoordinate(Q*coeff,R*coeff);
    }
    /*
     * classical 2D vector by scalar multiplication
     * @returns AxialCoordinate with internal values truncated to int
     */
    public AxialCoordinate times(float coeff){
        return new AxialCoordinate((int)(Q*coeff),(int)(R*coeff));
    }
    /*
     * @returns AxialCoordinate where each component is the square of its original value
     */
    public AxialCoordinate squareComponenetwise(){
        return new AxialCoordinate((Q*Q),(R*R));
    }
    /*
     * @returns minimal number of movements needed to reach @param from @this in an Euclidean geometry
     */
    public int distanceTo(AxialCoordinate arg){
        //the algorithm is explained here: https://www.redblobgames.com/grids/hexagons/#distances
        AxialCoordinate b = arg;
        int S = -(Q+R);
        int bS = -(b.Q+b.R);
        return  (Math.abs(Q - b.Q) + Math.abs(R - b.R) + Math.abs(S - bS)) / 2;
    }

    public CubeCoordinateMutable toCubeCoordinate(){
        return new CubeCoordinateMutable(Q, R, (Q+R));
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



}
