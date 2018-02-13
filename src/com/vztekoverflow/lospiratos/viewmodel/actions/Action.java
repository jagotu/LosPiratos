package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.util.SimpleObservable;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

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
            if(getRelatedShip() == null)
                return false;
            return recomputeVisible();
        }
    };
    protected final BooleanBinding plannable = new BooleanBinding() {
        @Override
        protected boolean computeValue() {
            if(getRelatedShip() == null)
                return false;
            if (isPrivilegedModeActive())
                return true;
            if (getRelatedShip().getPlannedActions().stream().anyMatch(a -> a.preventsFromBeingPlanned(Action.this)))
                return false;
            if (getRelatedShip().getMechanics().stream().anyMatch(m -> m.preventsFromBeingPlanned(Action.this)))
                return false;
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
        cost.invalidate();
        privilegedModeActive.invalidate();
        ((SimpleObservable) relatedShipJustChanged).fireListenerChange();
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

    protected abstract ResourceReadOnly recomputeCost();

    protected final ObjectBinding<ResourceReadOnly> cost = new ObjectBinding<ResourceReadOnly>() {
        @Override
        protected ResourceReadOnly computeValue() {
            if(getRelatedShip() == null)
                return ResourceReadOnly.ZERO;
            return recomputeCost();
        }
    };

    public final ResourceReadOnly getCost() {
        return cost.get();
    }

    public final ObjectBinding<ResourceReadOnly> costProperty() {
        return cost;
    }

    /**
     * @return true if action's cost has successfully been paid.
     */
    protected final boolean performPayment() {
        ResourceReadOnly cost = getCost();
        if (cost.isLesserThanOrEqual(getRelatedShip().getTeam().getOwnedResource())) {
            getRelatedShip().getTeam().getOwnedResource().subtract(cost);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void performOnTarget() {
        if (isPrivilegedModeActive())
            performOnTargetInternal();
        else if (performPayment())
            performOnTargetInternal();
        else
            Warnings.makeWarning(toString() + ".performOnTarget()", "Action has not been performed because there is not enough resource");
        //todo tell also some info to game user?
    }

    /**
     * Should be overridden by inheritors to add custom behaviour.
     */
    protected abstract void performOnTargetInternal();

    //endregion

}