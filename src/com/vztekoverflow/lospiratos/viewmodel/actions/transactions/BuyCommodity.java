package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.util.PartialOrdering;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.PURCHASE_COEFFICIENTS;

public class BuyCommodity extends CommodityTransaction {

    @Override
    protected Action createCopyAndResetThis() {
        BuyCommodity result = new BuyCommodity();
        result.getCommodities().setAll(this.getCommodities());
        getCommodities().clear();
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
    protected void recomputeCost() {
        if (getCommodities() == null) cost.clear();
        else
            cost.setAll(ResourceReadOnly.fromMoney(getCommodities().scalarProduct(PURCHASE_COEFFICIENTS)));
        int a = 10;
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() ->
                {
                    if (getCommodities() == null) return false;
                    if (getCommodities().compare(Resource.ZERO).equals(PartialOrdering.Equal)) return false;
                    if (getCommodities().getMetal() != 0
                            || getCommodities().getRum() != 0
                            || getCommodities().getTobacco() != 0
                            || getCommodities().getMoney() != 0) return false;
                    if(getRelatedShip() == null) return false;
                    int teamMoney = getRelatedShip().getTeam().getOwnedResource().getMoney();
                    if (getCost().getMoney() > teamMoney) {
                        if (shipHasPlannedAtLeast(1, SellCommodity.class) || shipHasPlannedAtLeast(1, UnloadStorage.class))
                            return true;
                        else return false;
                    } else return true;
                }
                ,
                getCommodities(), relatedShipProperty());
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.buyCommodity;
    }
}
