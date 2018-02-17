package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;

public class RepairEnhancement extends EnhancementAbstractTransaction {

    static final double repairCostCoefficient = 0.1;

    @Override
    protected Action createCopyAndResetThis() {
        RepairEnhancement result = new RepairEnhancement();
        result.setEnhancement(this.getEnhancement());
        this.setEnhancement(null);
        return result;
    }

    @Override
    public void performOnShipInternal() {
        getRelatedShip().getEnhancement(getEnhancement()).setDestroyed(false);
    }

    private EnhancementActionParameter parameter;

    @Override
    protected EnhancementActionParameter getEnhancementParameter() {
        if (parameter == null) {
            parameter = new EnhancementActionParameter() {
                @Override
                public BooleanExpression validValueProperty(ObservableValue<Class<? extends ShipEnhancement>> value) {
                    return Bindings.createBooleanBinding(() -> {
                                if (getRelatedShip() == null) return false;
                                return getRelatedShip().getEnhancementStatus(value.getValue()).equals(ShipEnhancementStatus.destroyed);
                            }, relatedShipProperty(), value
                    );
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
        if(!isSatisfied()) return null;
        ShipEnhancement e = EnhancementsCatalog.createInstanceFromPersistentName(EnhancementsCatalog.getPersistentName(getEnhancement()));
        if(e == null) return null;
        return e.getCostUniversal().times(repairCostCoefficient);
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return getEnhancementParameter().validProperty();
    }

}
