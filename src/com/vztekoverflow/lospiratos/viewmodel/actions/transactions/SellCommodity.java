package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.util.PartialOrdering;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;

public class SellCommodity extends CommodityTransaction {

    @Override
    protected Action createCopyAndResetThis() {
        SellCommodity result = new SellCommodity();
        result.setCommodities(this.getCommodities().createMutableCopy());
        this.commoditiesProperty().unbind();
        this.setCommodities(new Resource());
        return result;
    }

    @Override
    public void performOnShipInternal() {
        //price (negative value, ie. gain) has already been paid by the caller
        getRelatedShip().getTeam().getOwnedResource().subtract(this.getCommodities());
    }

    @Override
    public String getČeskéJméno() {
        return "prodej komodit";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        if (getCommodities() == null) return ResourceReadOnly.ZERO;
        return ResourceReadOnly.fromMoney(getCommodities().scalarProduct(sellCoefficients) * -1);
        //return negative value, because this cost is actually a gain (we are selling something)
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() -> {
                    if (getCommodities() == null) return false;
                    return !getCommodities().compare(Resource.ZERO).equals(PartialOrdering.Equal)
                            && getCommodities().getMoney() == 0;
                }
                , commoditiesProperty(), relatedShipProperty());
    }

    //todo opravdickou hodnotu
    static final ResourceReadOnly sellCoefficients = new ResourceReadOnly(1, 2, 2, 2, 2, 2);
}
