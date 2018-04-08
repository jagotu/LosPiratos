package com.vztekoverflow.lospiratos.util;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a direction in Axial coordinate system through a unit axial direction.
 * distance between AxialDirection and (0,0) is always 1.
 */
public final class AxialDirection extends AxialCoordinate{

    private  AxialDirection(int P, int Q){
        super(P,Q);
    }

    /**
     * returns degree representation of the direction, as understood by the DirectionFromDegree functions
     * 0 represents top, 90 represents right etc
     * returns values that are multiple of 60 plus 15, thus working both for pointy and flat-topped representation
     */
    public int toDegrees(){
        if(this.equals(0,-1))
            return  15;

        if(this.equals(1,-1))
            return  75;

        if(this.equals(1,0))
            return  135;

        if(this.equals(0,1))
            return  195;

        if(this.equals(-1,1))
            return  255;

        if(this.equals(-1,0))
            return  315;

        /* unreachable code */
        throw new IllegalStateException("Unreachable code reached.");

    }

    /**
     * Returns unit AxialCoordinate pointing in the direction defined by {@code degree}
     * @param degree degree representing the direction. 30 means topRight, 90 right etc.
     * if {@code degree} is not multiple of 60 plus 30, nearest value will be used
     */
    public static AxialDirection directionFromDegree_Pointy(int degree){
        return  directionFromDegree_Flat(degree - 30);
    }
    /**
     * Returns unit AxialCoordinate pointing in the direction defined by @{code degree}
     * @param degree representing the direction. 0 means top, 60 rightTop etc.
     * if {@code degree} is not multiple of 60, nearest value will be used
     */
    public static AxialDirection directionFromDegree_Flat(int degree){
        degree = ((degree % 360) + 360) % 360; //to get an always-positive representation of original value

        degree += 30; //for the next step to round correctly
        degree /= 60; //to divide into 6 basic directions
        degree %= 6;

        switch (degree){
            case 0: return FlatUp;
            case 1: return FlatRightUp;
            case 2: return FlatRightDown;
            case 3: return FlatDown;
            case 4: return FlatLeftDown;
            case 5: return FlatLeftUp;
        }
        /* unreachable */
        throw new IllegalStateException("Unreachable code reached.");
    }

    //pre-made unit vector directions:

    public static AxialDirection PointyUpLeft = new AxialDirection(0,-1);
    public static AxialDirection PointyUpRight = new AxialDirection(+1,-1);
    public static AxialDirection PointyRight = new AxialDirection(+1,0);
    public static AxialDirection PointyDownRight = new AxialDirection(0,+1);
    public static AxialDirection PointyDownLeft =  new AxialDirection(-1,+1);
    public static AxialDirection PointyLeft= new AxialDirection(-1,0);

    public static AxialDirection FlatUp = new AxialDirection(0,-1);
    public static AxialDirection FlatRightUp = new AxialDirection(+1,-1);
    public static AxialDirection FlatRightDown =  new AxialDirection(+1,0);
    public static AxialDirection FlatDown =  new AxialDirection(0,+1);
    public static AxialDirection FlatLeftDown = new AxialDirection(-1,+1);
    public static AxialDirection FlatLeftUp =  new AxialDirection(-1,0);


    private static List<AxialDirection> allDirections;
    /**
     * Returns all available unit directions for a hexagon. When drawn, those would appear as a circle.
     */
    public static Iterable<AxialDirection> getAllDirections(){
        if(allDirections == null){
            allDirections = new ArrayList<>();
            allDirections.add(FlatUp);
            allDirections.add(FlatRightUp);
            allDirections.add(FlatRightDown);
            allDirections.add(FlatDown);
            allDirections.add(FlatLeftDown);
            allDirections.add(FlatLeftUp);
        }
        return allDirections;
    }

}
