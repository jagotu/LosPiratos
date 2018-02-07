package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AxialCoordinateActionParameter implements ActionParameter<AxialCoordinate> {
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

    @Override
    public BooleanBinding isSatisfied() {
        return Bindings.createBooleanBinding(() -> coordinate.get() != null && isAvailable(coordinate.get()), coordinate);
    }

    private ObjectProperty<AxialCoordinate> coordinate = new SimpleObjectProperty<>();

    abstract public boolean isAvailable(AxialCoordinate coord);
}
