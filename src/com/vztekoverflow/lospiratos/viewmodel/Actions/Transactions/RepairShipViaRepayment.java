package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RepairShipViaRepayment extends RepairShip {
    @Override
    protected Action createCopy() {
        return new RepairShipViaRepayment();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí zaplacení";
    }

    @Override
    protected Resource recomputeCost() {
        throw new NotImplementedException();
    }
}
