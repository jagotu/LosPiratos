package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.ObjectProperty;

public interface MovableFigure {
    AxialCoordinate getPosition();
    ObjectProperty<AxialCoordinate> positionProperty();
}
