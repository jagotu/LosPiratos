package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;

public class RepairEnhancement extends EnhancementAbstractTransaction {

    @Override
    protected Action createCopyAndResetThis() {
        RepairEnhancement result = new RepairEnhancement();
        result.setEnhancement(this.getEnhancement());
        this.setEnhancement(null);
        return result;
    }

    @Override
    public void performOnTargetInternal() {
        getRelatedShip().getEnhancement(getEnhancement()).setDestroyed(false);
    }

    private EnhancementActionParameter parameter;

    @Override
    protected EnhancementActionParameter getEnhancementParameter() {
        if (parameter == null) {
            parameter = new EnhancementActionParameter() {
                @Override
                public boolean isValidValue(Class<? extends ShipEnhancement> value) {
                    return getRelatedShip().getEnhancementStatus(getEnhancement()).equals(ShipEnhancementStatus.destroyed);
                }
            };
        }
        return parameter;
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

    private BooleanBinding satisfied = Bindings.createBooleanBinding(() ->
                    parameter.isValid(),
            relatedShipJustChanged, parameter.property());

    @Override
    public ObservableBooleanValue isSatisfied() {
        return satisfied;
    }

    static final double repairCostCoefficient = 0.1;
}
