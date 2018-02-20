package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.util.PartialOrdering;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.SELL_COEFFICIENTS;

public class SellCommodity extends CommodityTransaction {

    @Override
    protected Action createCopyAndResetThis() {
        SellCommodity result = new SellCommodity();
        result.getCommodities().setAll(this.getCommodities());
        getCommodities().clear();
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
    protected void recomputeCost() {
        cost.clear();
        if (getCommodities() != null){
            cost.setAll( ResourceReadOnly.fromMoney(getCommodities().scalarProduct(SELL_COEFFICIENTS) * -1));
            //set negative value, because this cost is actually a gain (we are selling something)
        }
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() -> {
                    if (getCommodities() == null) return false;
                    return !getCommodities().compare(Resource.ZERO).equals(PartialOrdering.Equal)
                            && getCommodities().getMoney() == 0;
                }
                , getCommodities(), relatedShipProperty());
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.sellCommodity;
    }

}
