package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;

import java.util.List;

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
            if(getRelatedShip().getPlannedActions().stream().anyMatch(a -> a.getClass().equals(ActivatePrivilegedMode.class)) ) return true;
            if(getRelatedShip().getPlannedActions().stream().anyMatch(a -> a.preventsFromBeingPlanned(Action.this))) return false;
            return recomputePlannable();
        }
    };
    protected final ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();


    protected abstract boolean recomputeVisible();
    protected abstract boolean recomputePlannable();

    public Action() {
        relatedShip.addListener((__, old, newValue) -> {
            invalidateBindings();
            newValue.plannedActionsProperty().addListener((InvalidationListener)  ___ -> {
                invalidateBindings();
                //todo this is not the most efficient solution (invalidations could be granulated and called only some of them), but is easy to implement
            });
        } );
    }

    /*
     * Overridden implementations should ALWAYS call super.invalidateBindings() first
     */
    protected void invalidateBindings(){
        visible.invalidate();
        plannable.invalidate();
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

    final protected boolean shipHasPlannedLessThan(int count, Class<? extends Action> action){
        return getRelatedShip().getPlannedActions().stream().filter(a -> action.isAssignableFrom(a.getClass())).count() < count;
    }
    final protected boolean shipHasPlannedExactly(int count, Class<? extends Action> action){
        return getRelatedShip().getPlannedActions().stream().filter(a -> action.isAssignableFrom(a.getClass())).count() == count;
    }
    //may be overridden by children
    public boolean preventsFromBeingPlanned(Action preventedAction){return false;}

    @Override
    public PerformableAction asPerformableAction() {
        return createCopy();
    }
    protected abstract Action createCopy();

    //may be overridden by children
    public List<PlannableAction> getActionSpecifiers(){return null;}
}