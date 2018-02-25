package com.vztekoverflow.lospiratos.viewmodel.transitions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public class Teleport extends Transition {

    public Teleport(int originalRotationAbsolute, int newRotationAbsolute, AxialCoordinate originPositionAbsolute, AxialCoordinate newPositionAbsolute) {
        this.originalRotationAbsolute = originalRotationAbsolute;
        this.newRotationAbsolute = newRotationAbsolute;
        this.originPositionAbsolute = originPositionAbsolute;
        this.newPositionAbsolute = newPositionAbsolute;
        originPositionRelative = newPositionAbsolute.minus(originPositionAbsolute);
    }

    private final int originalRotationAbsolute;
    private final int newRotationAbsolute;
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

    public int getOriginalRotationAbsolute() {
        return originalRotationAbsolute;
    }

    public int getNewRotationAbsolute() {
        return newRotationAbsolute;
    }
}
