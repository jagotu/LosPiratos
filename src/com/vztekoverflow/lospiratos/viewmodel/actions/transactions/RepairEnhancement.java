package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.REPAIR_ENHANCEMENT_COST_COEFFICIENT;

public class RepairEnhancement extends EnhancementAbstractTransaction {



    @Override
    protected Action createCopyAndResetThis() {
        RepairEnhancement result = new RepairEnhancement();
        result.setEnhancement(this.getEnhancement());
        this.enhancementProperty().unbind();
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
    protected void recomputeCost() {
        cost.clear();
        if(isSatisfied()){
            ShipEnhancement e = EnhancementsCatalog.createInstance(getEnhancement());
            if(e != null)
                cost.setAll(e.getCostUniversal().times(REPAIR_ENHANCEMENT_COST_COEFFICIENT));
        }
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return getEnhancementParameter().validProperty();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.repairEnhnacement;
    }

}
