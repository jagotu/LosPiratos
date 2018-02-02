package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics.Chained;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics.Rooted;

public abstract class ShipMechanics extends ShipEntity {
    public abstract com.vztekoverflow.lospiratos.model.ShipMechanics getModelDescription();

    public static ShipMechanics getInstanceFromDescription(com.vztekoverflow.lospiratos.model.ShipMechanics m) {
        if (m == com.vztekoverflow.lospiratos.model.ShipMechanics.rooted) {
            return new Rooted();
        }
        if (m == com.vztekoverflow.lospiratos.model.ShipMechanics.chained) {
            return new Chained();
        }
        //todo warning
        return null; //implement this if you have more mechanics
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!ShipMechanics.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        ShipMechanics m = (ShipMechanics) obj;
        return this.getModelDescription().equals(m.getModelDescription());
    }

    @Override
    public int hashCode() {
        return getModelDescription().hashCode();
    }

    public abstract boolean preventsFromBeingPlanned(Action preventedAction);

}
