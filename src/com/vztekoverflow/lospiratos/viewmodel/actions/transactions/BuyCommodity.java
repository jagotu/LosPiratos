package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.util.PartialOrdering;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;

public class BuyCommodity extends CommodityTransaction {

    @Override
    protected Action createCopyAndResetThis() {
        BuyCommodity result = new BuyCommodity();
        result.setCommodities(this.getCommodities().createMutableCopy());
        this.commoditiesProperty().unbind();
        this.setCommodities(new Resource());
        return result;
    }

    @Override
    public void performOnShipInternal() {
        //price has already been paid by the caller
        getRelatedShip().getTeam().getOwnedResource().add(this.getCommodities());
    }

    @Override
    public String getČeskéJméno() {
        return "nákup komodit";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        if (getCommodities() == null) return ResourceReadOnly.ZERO;
        return ResourceReadOnly.fromMoney(getCommodities().scalarProduct(purchaseCoefficients));
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() ->
                {
                    if (getCommodities() == null) return false;
                    return !getCommodities().compare(Resource.ZERO).equals(PartialOrdering.Equal)
                            && getCommodities().getMetal() == 0
                            && getCommodities().getMoney() == 0;
                }
                , commoditiesProperty(), relatedShipProperty());
    }

    //todo opravdickou hodnotu
    static final ResourceReadOnly purchaseCoefficients = new ResourceReadOnly(1, 2, 2, 2, 2, 2);
}
