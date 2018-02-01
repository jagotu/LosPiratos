package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AxialCoordinateActionParameter implements ActionParameter<AxialCoordinate>{
    @Override
    public Class<AxialCoordinate> getParameterType() {
        return AxialCoordinate.class;
    }

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
