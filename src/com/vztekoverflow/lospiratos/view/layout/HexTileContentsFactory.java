package com.vztekoverflow.lospiratos.view.layout;

import javafx.geometry.Point2D;

public interface HexTileContentsFactory {

    HexTileContents getContentsFor(Point2D coords, double tileWidth, double tileHeight);
}
