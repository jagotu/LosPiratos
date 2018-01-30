package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class UpgradeShip extends ChangeShipAbstractTransaction {
    @Override
    protected Action createCopy() {
        return new UpgradeShip();
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && ShipType.increment(getRelatedShip().getShipType().getClass()) != null;
    }

    @Override
    public void performOnTargetInternal() {
        getRelatedShip().setShipType(ShipType.increment(getRelatedShip().getShipType().getClass()));
    }

    @Override
    public String getČeskéJméno() {
        return "upgrade lodě";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        //todo tady by to asi melo byt jinak?
        return getRelatedShip().getShipType().getCostUniversal();
    }
}
