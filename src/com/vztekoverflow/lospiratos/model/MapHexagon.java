package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.*;

public class MapHexagon {

    public MapHexagon(int coordinateQ, int coordinateR) {
        this.coordinateQ.set(coordinateQ);
        this.coordinateR.set(coordinateR);
    }
    public MapHexagon(AxialCoordinate coordinate) {
        this.coordinateQ.set(coordinate.getQ());
        this.coordinateR.set(coordinate.getR());
    }
    public MapHexagon(AxialCoordinate coordinate, String content) {
        this.coordinateQ.set(coordinate.getQ());
        this.coordinateR.set(coordinate.getR());
        this.content.set(content);
    }
    public MapHexagon(int coordinateQ, int coordinateR, String content) {
        this.coordinateQ.set(coordinateQ);
        this.coordinateR.set(coordinateR);
        this.content.set(content);
    }

    public IntegerProperty coordinateQ = new SimpleIntegerProperty();
    public IntegerProperty coordinateR = new SimpleIntegerProperty();

    ///e.g. sea, shore, port, treasure etc
    public StringProperty content = new SimpleStringProperty("");

    public MapProperty<String, String> customExtensions = new SimpleMapProperty<>();
}
