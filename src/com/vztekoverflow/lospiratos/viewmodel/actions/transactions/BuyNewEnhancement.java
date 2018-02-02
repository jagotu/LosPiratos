package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;

public class BuyNewEnhancement extends EnhancementAbstractTransaction {

    @Override
    protected Action createCopy() {
        return new BuyNewEnhancement();
    }

    @Override
    public void performOnTargetInternal() {
        //todo ne vsechny enhancementy jdou koupit vsem typum lodi
        getRelatedShip().addNewEnhancement(getEnhancement());
    }

    @Override
    public String getČeskéJméno() {
        return "zakoupení vylepšení";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return EnhancementsCatalog.createInstanceFromPersistentName(EnhancementsCatalog.getPersistentName(getEnhancement())).getCostUniversal();
    }
}
