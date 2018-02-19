package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.DamageSufferedResponse;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;

import java.util.HashSet;
import java.util.Set;

public abstract class Attack extends Action {

    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.attackGenericIcon;
    }

    @Override
    protected void recomputeCost() {
        //nothing, there this actions costs nothing
    }

    protected final void applyMechanicsTo(ShipMechanics mechanics, AxialCoordinate targetPosition) {
        Ship target = getRelatedShip().getTeam().getGame().getBoard().getShip(targetPosition);
        if (target == null) return;
        target.mechanicsProperty().add(mechanics);
    }

    /**
     * Applies damage to a figure at given position (if it exists).
     * If it is a Ship and is destroyed, the function also gives treasure of destroyed ship to relatedShip.
     *
     * @param damageValue    how much damage to do
     * @param targetPosition Position of the target. If there is no damageable figure at the position, nothing happens.
     * @return value indicating whether the target has been destroyed, or null if there is no ship on the @targetPosition
     */
    protected final DamageSufferedResponse applyDamageTo(int damageValue, AxialCoordinate targetPosition) {
        Ship target = getRelatedShip().getTeam().getGame().getBoard().getShip(targetPosition);
        if (target == null) return null;

        DamageSufferedResponse result = target.takeDamage(damageValue);
        for (OnDamageDoneListener l : onDamageDoneListeners) {
            l.onDamageDone(this, target, damageValue, result);
        }
        return result;
    }

    public void addListener(OnDamageDoneListener listener) {
        onDamageDoneListeners.add(listener);
    }

    public void removeListener(OnDamageDoneListener listener) {
        onDamageDoneListeners.remove(listener);
    }

    private Set<OnDamageDoneListener> onDamageDoneListeners = new HashSet<>();
}
