package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public interface HexTileContentsFactory {

    HexTileContents getContentsFor(AxialCoordinate coords, double tileWidth, double tileHeight);
}
