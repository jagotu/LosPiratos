package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;

public class RepairEnhancement extends EnhancementAbstractTransaction {

    @Override
    protected Action createCopy() {
        return new RepairEnhancement();
    }

    @Override
    public void performOnTargetInternal() {
        getRelatedShip().getEnhancement(getEnhancement()).setDestroyed(false);
    }

    @Override
    public String getČeskéJméno() {
        return "oprava vylepšení";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        ShipEnhancement e = EnhancementsCatalog.createInstanceFromPersistentName(EnhancementsCatalog.getPersistentName(getEnhancement()));
        //the null pointer exception (reported here by code check) should not happen, based on Catalog's contract
        return e.getCostUniversal().times(repairCostCoefficient);
    }

    static final double repairCostCoefficient = 0.1;
}
