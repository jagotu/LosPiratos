package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.CustomShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UpgradeShip extends ChangeShipAbstractTransaction {
    @Override
    protected Action createCopy() {
        return new UpgradeShip();
    }

    @Override
    protected boolean recomputePlannable() {
        if(getRelatedShip().getShipType().getClass().equals(Galleon.class))
            return false;
        if(getRelatedShip().getShipType().getClass().equals(CustomShipType.class))
            return false;
        return super.recomputePlannable();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "upgrade lodě";
    }

    @Override
    protected Resource recomputeCost() {
        throw new NotImplementedException();
    }
}
