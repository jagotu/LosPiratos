package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.DamageSufferedResponse;
import com.vztekoverflow.lospiratos.viewmodel.DamageableFigure;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;

public abstract class Attack extends Action {

    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.attackGenericIcon;
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return new ResourceReadOnly();
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
     * @return value indicating whether the target has been destroyed
     */
    protected final DamageSufferedResponse applyDamageTo(int damageValue, AxialCoordinate targetPosition) {
        DamageableFigure target = getRelatedShip().getTeam().getGame().getBoard().getDamageableFigure(targetPosition);
        if (target == null) return null;

        ResourceReadOnly targetsResource = new ResourceReadOnly();
        if (target instanceof Ship) {
            targetsResource = ((Ship) target).getStorage().createCopy();
        }

        DamageSufferedResponse result = target.takeDamage(damageValue);
        //todo log the damage: getShip() has just destroyed target using this
        if (result == DamageSufferedResponse.hasJustBeenDestroyed) {
            //todo log: target has been destroyed; this gains targetsResource
            getRelatedShip().getStorage().add(targetsResource);
        }
        return result;
    }
}