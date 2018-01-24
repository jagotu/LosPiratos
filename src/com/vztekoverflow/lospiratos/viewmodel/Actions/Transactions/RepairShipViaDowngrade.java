package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RepairShipViaDowngrade extends RepairShip {
    @Override
    protected Action createCopy() {
        return new RepairShipViaDowngrade();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí downgrade";
    }

    @Override
    protected Resource recomputeCost() {
        throw new NotImplementedException();
    }
}
