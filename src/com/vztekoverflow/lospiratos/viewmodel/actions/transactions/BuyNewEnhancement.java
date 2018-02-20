package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;

public class BuyNewEnhancement extends EnhancementAbstractTransaction {

    public BuyNewEnhancement() {
    }

    @Override
    protected Action createCopyAndResetThis() {
        BuyNewEnhancement result = new BuyNewEnhancement();
        result.setEnhancement(this.getEnhancement());
        this.enhancementProperty().unbind();
        this.setEnhancement(null);
        return result;
    }

    @Override
    public void performOnShipInternal() {
        getRelatedShip().addNewEnhancement(getEnhancement());
    }

    private EnhancementActionParameter parameter;

    @Override
    protected EnhancementActionParameter getEnhancementParameter() {
        if (parameter == null) {
            parameter = new EnhancementActionParameter() {
                @Override
                public BooleanExpression validValueProperty(ObservableValue<Class<? extends ShipEnhancement>> value) {
                    return Bindings.createBooleanBinding(() -> {
                                if (getRelatedShip() == null || value.getValue() == null) return false;
                                return EnhancementsCatalog.isAcquirableBy(value.getValue(), getRelatedShip().getShipType());
                            }, relatedShipProperty(), value
                    );
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
    protected void recomputeCost() {
        if (getEnhancement() == null) cost.clear();
        else
            cost.setAll(EnhancementsCatalog.createInstance(getEnhancement()).getCostUniversal());
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return parameter.validProperty();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.buyEnhancement;
    }
}
