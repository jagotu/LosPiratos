package com.vztekoverflow.lospiratos.util;

/*
 * represents a direction in Axial coordinate system through a unit axial direction.
 * distance between AxialDirection and (0,0) is always 1.
 */
public class AxialDirection extends AxialCoordinate{

    private  AxialDirection(int P, int Q){
        super(P,Q);
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
     * Returns unit AxialCoordinate pointing in the direction defined by @degree
     * @degree degree representing the direction. 30 means topRight, 90 right etc.
     * if @degree is not multiple of 60 plus 30, nearest value will be used
     */
    public static AxialDirection DirectionFromDegree_Pointy(int degree){
        return  DirectionFromDegree_Flat(degree - 30);
    }
    /*
     * Returns unit AxialCoordinate pointing in the direction defined by @degree
     * @param degree representing the direction. 0 means top, 60 rightTop etc.
     * if @degree is not multiple of 60, nearest value will be used
     */
    public static AxialDirection DirectionFromDegree_Flat(int degree){
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
            case 5: return FlatLeftDown;
        }
        /* unrecheable */
        throw new IllegalArgumentException("Invalid operation. Unrecheable code reached.");
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

}
