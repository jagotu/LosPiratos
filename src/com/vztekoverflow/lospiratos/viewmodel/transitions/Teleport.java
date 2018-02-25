package com.vztekoverflow.lospiratos.viewmodel.transitions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public class Teleport extends Transition {

    public Teleport(AxialCoordinate originPositionAbsolute, AxialCoordinate newPositionAbsolute) {
        this.originPositionAbsolute = originPositionAbsolute;
        this.newPositionAbsolute = newPositionAbsolute;
        originPositionRelative = newPositionAbsolute.minus(originPositionAbsolute);
    }

    private final AxialCoordinate originPositionRelative;
    private final AxialCoordinate originPositionAbsolute;
    private final AxialCoordinate newPositionAbsolute;

    public AxialCoordinate getOriginPositionRelative() {
        return originPositionRelative;
    }

    public AxialCoordinate getOriginPositionAbsolute() {
        return originPositionAbsolute;
    }

    public AxialCoordinate getNewPositionAbsolute() {
        return newPositionAbsolute;
    }
}
