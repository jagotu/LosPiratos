package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;

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

    private IntegerProperty coordinateQ = new SimpleIntegerProperty();
    private IntegerProperty coordinateR = new SimpleIntegerProperty();

    ///e.g. sea, shore, port, treasure etc
    private StringProperty content = new SimpleStringProperty("");

    private MapProperty<String, String> customExtensions = new SimpleMapProperty<>();

    public int getCoordinateQ() {
        return coordinateQ.get();
    }

    public IntegerProperty coordinateQProperty() {
        return coordinateQ;
    }

    public int getCoordinateR() {
        return coordinateR.get();
    }

    public IntegerProperty coordinateRProperty() {
        return coordinateR;
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public ObservableMap<String, String> getCustomExtensions() {
        return customExtensions.get();
    }

    public MapProperty<String, String> customExtensionsProperty() {
        return customExtensions;
    }


}
