package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public interface MovableFigure extends Figure {
    /**
     * returns a Position object that can be modified to change figure's position
     */
    Position getPosition();

    @Override
    default AxialCoordinate getCoordinate() {
        return getPosition().getCoordinate();
    }
}
