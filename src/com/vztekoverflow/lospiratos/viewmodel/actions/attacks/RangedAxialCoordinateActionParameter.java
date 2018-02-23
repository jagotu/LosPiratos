package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public abstract class RangedAxialCoordinateActionParameter extends AxialCoordinateActionParameter {
    private IntegerProperty range = new SimpleIntegerProperty();
    private ObjectProperty<AxialCoordinate> groundZero = new SimpleObjectProperty<>();
    private final Action relatedAction;

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

    public RangedAxialCoordinateActionParameter(Action relatedAction) {
        this.relatedAction = relatedAction;
    }

    @Override
    public BooleanExpression validValueProperty(ObservableValue<AxialCoordinate> value) {

        return Bindings.createBooleanBinding(() -> {
                    if (value.getValue() == null) return false;
                    if(relatedAction == null || relatedAction.getRelatedShip() == null) return false;
                    return value.getValue().distanceTo(groundZero.get()) <= range.get()
                            && relatedAction.getRelatedShip().getTeam().getGame().getBoard().getTiles().get(value.getValue()).allowsFighting();
                }
                , groundZeroProperty(), rangeProperty(), value);
    }
}
