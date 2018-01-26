package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;

public class UnloadStorage extends Transaction {
    @Override
    protected boolean recomputePlannable() {
        if(! super.recomputePlannable()) return false;
        //else
        int shipSpeed = getRelatedShip().getSpeed();
        int maneuversAlreadyPlanned = getRelatedShip().getPlannedActions().stream().mapToInt(Action::getManeuverSlotsTaken).sum();
        return maneuversAlreadyPlanned < shipSpeed;
    }
    @Override
    public int getManeuverSlotsTaken(){return 1;}

    @Override
    protected Resource recomputeCost() {
        return new Resource();
    }

    @Override
    protected Action createCopy() {
        return new UnloadStorage();
    }

    @Override
    public void performOnTarget() {
        Ship s = getRelatedShip();
        Team t = s.getTeam();
        t.getOwnedResource().add(s.getStorage());
        s.getStorage().setRum(0);
        s.getStorage().setCloth(0);
        s.getStorage().setMetal(0);
        s.getStorage().setMoney(0);
        s.getStorage().setWood(0);
        s.getStorage().setTobacco(0);
    }

    @Override
    public String getČeskéJméno() {
        return "vyložení nákladu";
    }
}
