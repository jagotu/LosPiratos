package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.ValidableActionParameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AxialCoordinateActionParameter implements ValidableActionParameter<AxialCoordinate> {
    @Override
    public void set(AxialCoordinate value) {
        coordinate.set(value);
    }

    @Override
    public AxialCoordinate get() {
        return coordinate.get();
    }

    @Override
    public ObjectProperty<AxialCoordinate> property() {
        return coordinate;
    }

    private ObjectProperty<AxialCoordinate> coordinate = new SimpleObjectProperty<>();

}
