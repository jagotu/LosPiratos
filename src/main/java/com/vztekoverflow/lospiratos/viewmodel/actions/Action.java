package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.util.SimpleObservable;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.RepairShip;
import com.vztekoverflow.lospiratos.viewmodel.logs.EventLogger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Generic abstract class representing an action that may be planned by the user and the performed.
 */
public abstract class Action implements PerformableAction, PlannableAction {


    @Override
    public PerformableAction asPerformableAction() {
        return createCopyAndResetThis();
    }

    protected abstract Action createCopyAndResetThis();

    private InvalidationListener invalidationListener = ___ -> {
        invalidateBindings();
        //this is not the most efficient solution (invalidations could be granulated and called only some of them), but is easy to implement
    };

    public Action() {
        relatedShip.addListener((__, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.plannedActionsProperty().removeListener(invalidationListener);
            }
            if (newValue != null) {
                newValue.plannedActionsProperty().addListener(invalidationListener);
                invalidateBindings();
            }
        });
    }

    private final ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();

    @Override
    public final ObjectProperty<Ship> relatedShipProperty() {
        return relatedShip;
    }

    public final Ship getRelatedShip() {
        return relatedShip.get();
    }

    protected final BooleanBinding privilegedModeActive = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            return getRelatedShip().getPlannedActions().stream().anyMatch(a -> a.getClass().equals(ActivatePrivilegedMode.class));
        }
    };

    protected boolean isPrivilegedModeActive() {
        return privilegedModeActive.get();
    }

    //region plannable

    protected final BooleanBinding visible = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            if (getRelatedShip() == null)
                return false;
            return recomputeVisible();
        }
    };
    protected final BooleanBinding plannable = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            if (getRelatedShip() == null)
                return false;
            if (isPrivilegedModeActive())
                return true;
            if (getRelatedShip().getPlannedActions().stream().anyMatch(a -> a.preventsFromBeingPlanned(Action.this)))
                return false;
            if (getRelatedShip().getMechanics().stream().anyMatch(m -> m.preventsFromBeingPlanned(Action.this)))
                return false;
            if(getRelatedShip().isDestroyed() && ! (Action.this instanceof RepairShip) && (! (Action.this instanceof ActivatePrivilegedMode)))
                return false; //todo this is temporal workaround, should be implemented by the RepairShip class and ActivatePrivlegedMode
            return recomputePlannable();
        }
    };

    public boolean getVisible() {
        return visible.get();
    }

    @Override
    public BooleanExpression visibleProperty() {
        return visible;
    }

    public boolean getPlannable() {
        return plannable.get();
    }

    @Override
    public BooleanExpression plannableProperty() {
        return plannable;
    }

    @Override
    public void setRelatedShip(Ship s) {
        relatedShip.set(s);
    }

    //region inheritors' API

    protected Position getRelatedShipsFuturePosition() {
        Position p = getRelatedShip().getPosition().createCopy();
        getRelatedShip().getPlannedActions().stream().
                filter(a -> Maneuver.class.isAssignableFrom(a.getClass())).
                map(a -> (Maneuver) a).forEach(a -> a.performOn(p));
        return p;
    }

    protected final EventLogger getEventLogger() {
        return getRelatedShip().getTeam().getGame().getLogger();
    }

    //may be overridden by children
    public int getManeuverSlotsTaken() {
        return 0;
    }

    protected abstract boolean recomputeVisible();

    protected abstract boolean recomputePlannable();

    /**
     * An Observable that you can bind to.
     * It will be triggered every time the related ship or its planned actions change.
     */
    protected final Observable relatedShipJustChanged = new SimpleObservable();

    protected final void invalidateBindings() {
        visible.invalidate();
        plannable.invalidate();
        privilegedModeActive.invalidate();
        ((SimpleObservable) relatedShipJustChanged).fireListenerChange();
        recomputeCost();
    }

    final protected boolean shipHasPlannedAtLeast(int count, Class<? extends Action> action) {
        return ! shipHasPlannedLessThan(count, action);
    }

    final protected boolean shipHasPlannedLessThan(int count, Class<? extends Action> action) {
        return getRelatedShip().getPlannedActions().stream().filter(a -> action.isAssignableFrom(a.getClass())).count() < count;
    }

    final protected boolean shipHasPlannedExactly(int count, Class<? extends Action> action) {
        return getRelatedShip().getPlannedActions().stream().filter(a -> action.isAssignableFrom(a.getClass())).count() == count;
    }

    //may be overridden by children
    //this is public only because I want to use it in a lambda in an inheritor. Otherwise it should be protected. Sadly, it is the only option
    public boolean preventsFromBeingPlanned(Action preventedAction) {
        return false;
    }

    //endregion
    //endregion
    //region performable

    protected abstract void recomputeCost();

    public final Resource getCost() {
        return cost;
    }

    protected final Resource cost = new Resource();



    /**
     * @return true if action's cost has successfully been paid.
     */
    protected final boolean performPayment() {
        ResourceReadOnly cost = getCost();
        Resource teamMoney = getRelatedShip().getTeam().getOwnedResource().createMutableCopy();
        teamMoney.clamp(ResourceReadOnly.ZERO, ResourceReadOnly.MAX);

        if (cost.isLesserThanOrEqual(teamMoney)) {
            getRelatedShip().getTeam().getOwnedResource().subtract(cost);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void performOnShip() {
        if (isPrivilegedModeActive())
            performOnShipInternal();
        else if (performPayment())
            performOnShipInternal();
        else {
            Warnings.makeWarning(toString() + ".performOnShip()", "Action has not been performed because there is not enough resource");
            getEventLogger().logActionFailed(this, getRelatedShip(), "nedostatek surovin");

        }
        //todo tell also some info to game user?
    }

    /**
     * Should be overridden by inheritors to add custom behaviour.
     */
    protected abstract void performOnShipInternal();

    //endregion

}