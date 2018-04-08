package com.vztekoverflow.lospiratos.viewmodel.transitions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public class Death extends Teleport {
    public Death(int originalRotationAbsolute, int newRotationAbsolute, AxialCoordinate originPositionAbsolute, AxialCoordinate newPositionAbsolute) {
        super(originalRotationAbsolute, newRotationAbsolute, originPositionAbsolute, newPositionAbsolute);
    }
}
