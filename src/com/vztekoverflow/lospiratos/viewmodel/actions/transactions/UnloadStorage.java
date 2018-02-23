package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;

public class UnloadStorage extends ManeuverTransaction {

    @Override
    protected void recomputeCost() {
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

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.unload;
    }
}
