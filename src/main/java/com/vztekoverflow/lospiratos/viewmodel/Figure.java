package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

/**
 * Abstract interface of a board's figure that can move on the board and knows its position.
 */
public interface Figure {
    AxialCoordinate getCoordinate();
}
