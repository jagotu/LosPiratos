package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class RangedAxialCoordinateActionParameter extends AxialCoordinateActionParameter {
    private IntegerProperty range = new SimpleIntegerProperty();
    private ObjectProperty<AxialCoordinate> groundZero = new SimpleObjectProperty<>();

    public AxialCoordinate getGroundZero() {
        return groundZero.get();
    }

    public ObjectProperty<AxialCoordinate> groundZeroProperty() {
        return groundZero;
    }

    public void setGroundZero(AxialCoordinate groundZero) {
        this.groundZero.set(groundZero);
    }

    public int getRange() {
        return range.get();
    }

    public IntegerProperty rangeProperty() {
        return range;
    }

    public void setRange(int range) {
        this.range.set(range);
    }

    @Override
    public boolean isValidValue(AxialCoordinate parameter) {
        return parameter.distanceTo(groundZero.get()) <= range.get();
    }

}
