package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.Transaction;

public class UnloadStorage extends Transaction {
    @Override
    protected boolean recomputePlannable() {
        if (!super.recomputePlannable()) return false;
        //else
        int shipSpeed = getRelatedShip().getSpeed();
        int maneuversAlreadyPlanned = getRelatedShip().getPlannedActions().stream().mapToInt(Action::getManeuverSlotsTaken).sum();
        return maneuversAlreadyPlanned < shipSpeed;
    }

    @Override
    public int getManeuverSlotsTaken() {
        return 1;
    }

    @Override
    protected Resource recomputeCost() {
        return new Resource();
    }

    @Override
    protected Action createCopyAndResetThis() {
        return new UnloadStorage();
    }

    @Override
    public void performOnShipInternal() {
        Ship s = getRelatedShip();
        Team t = s.getTeam();
        t.getOwnedResource().add(s.getStorage());
        s.getStorage().setAll(ResourceReadOnly.ZERO);
    }

    @Override
    public String getČeskéJméno() {
        return "vyložení nákladu";
    }
}
