package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;

public abstract class Action implements PerformableAction, PlannableAction {

    protected final BooleanBinding visible = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            return recomputeVisible();
        }
    };
    protected final BooleanBinding plannable = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            return recomputePlannable();
        }
    };
    protected final ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();


    protected abstract boolean recomputeVisible();
    protected abstract boolean recomputePlannable();

    public Action() {
        relatedShip.addListener((__, old, newValue) -> {
            visible.invalidate();
            plannable.invalidate();
            newValue.plannedActionsProperty().addListener((InvalidationListener)  ___ -> {
                visible.invalidate();
                plannable.invalidate();
            });
        } );
    }

    public boolean getVisible() {
        return visible.get();
    }

    @Override
    public ObservableBooleanValue visibleProperty() {
        return visible;
    }

    public boolean getPlannable() {
        return plannable.get();
    }

    @Override
    public ObservableBooleanValue plannableProperty() {
        return plannable;
    }

    @Override
    public void setRelatedShip(Ship s){
        relatedShip.set(s);
    }

    @Override
    public ObjectProperty<Ship> relatedShipProperty() {
        return relatedShip;
    }

    public Ship getRelatedShip() {
        return relatedShip.get();
    }

    protected abstract Action createCopy();

    protected boolean shipHasPlannedLessThan(int count, Class<? extends Action> action){
        return getRelatedShip().getPlannedActions().stream().filter(a -> action.isAssignableFrom(a.getClass())).count() < count;
    }
    protected boolean shipHasPlannedExactly(int count, Class<? extends Action> action){
        return getRelatedShip().getPlannedActions().stream().filter(a -> action.isAssignableFrom(a.getClass())).count() == count;
    }

}