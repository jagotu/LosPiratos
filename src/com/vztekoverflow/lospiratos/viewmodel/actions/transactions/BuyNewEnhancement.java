package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;

public class BuyNewEnhancement extends EnhancementAbstractTransaction {

    public BuyNewEnhancement() {
    }

    @Override
    protected Action createCopyAndResetThis() {
        BuyNewEnhancement result = new BuyNewEnhancement();
        result.setEnhancement(this.getEnhancement());
        this.setEnhancement(null);
        return result;
    }

    @Override
    public void performOnTargetInternal() {
        getRelatedShip().addNewEnhancement(getEnhancement());
    }

    private EnhancementActionParameter parameter;

    @Override
    protected EnhancementActionParameter getEnhancementParameter() {
        if(parameter == null){
            parameter = new EnhancementActionParameter() {
                @Override
                public boolean isValidValue(Class<? extends ShipEnhancement> value) {
                    return EnhancementsCatalog.isAcquirableBy(parameter.get(), getRelatedShip().getShipType());
                }
            };
        }
        return parameter;
    }

    @Override
    public String getČeskéJméno() {
        return "zakoupení vylepšení";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return EnhancementsCatalog.createInstanceFromPersistentName(EnhancementsCatalog.getPersistentName(getEnhancement())).getCostUniversal();
    }

    private BooleanBinding satisfied = Bindings.createBooleanBinding(() ->
                    parameter.isValid(),
            relatedShipJustChanged, parameter.property());

    @Override
    public ObservableBooleanValue isSatisfied() {
        return satisfied;
    }
}
