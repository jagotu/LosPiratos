package com.vztekoverflow.lospiratos.viewmodel.transitions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public class Teleport extends Transition {

    public Teleport(AxialCoordinate originAbsolute) {
        this.originAbsolute = originAbsolute;
    }

    private AxialCoordinate originAbsolute;

    public AxialCoordinate getOriginAbsolute() {
        return originAbsolute;
    }
}
