package com.vztekoverflow.lospiratos.viewmodel.actions;

import java.util.HashSet;
import java.util.Set;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.DamageSufferedResponse;
import com.vztekoverflow.lospiratos.viewmodel.DamageableFigure;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.CannonsAbstractVolley;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;

/**
 * Generic abstract user action that may apply damage to other board figures.
 */
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
        if (target == null)
            return;
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
        DamageableFigure t = getRelatedShip().getTeam().getGame().getBoard().getDamageableFigure(targetPosition);
        if ((t == null) || !(t instanceof Ship)) {
            if (!(this instanceof CannonsAbstractVolley)) { // Do not log empty volleys - those are too common
                getEventLogger().logAttackingEmptyTile(getRelatedShip(), this, targetPosition);
            }
            return null;
        }
        Ship target = (Ship) t;

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
