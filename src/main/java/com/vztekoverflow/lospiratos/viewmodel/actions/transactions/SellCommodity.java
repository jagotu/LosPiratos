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
        Resource teamResource = getRelatedShip().getTeam().getOwnedResource();
        teamResource.subtract(this.getCommodities());
        if (teamResource.anyComponentNegaitve()) {
            Resource negativeRes = teamResource.createMutableCopy();
            negativeRes.clamp(Integer.MIN_VALUE, 0);
            int moneyStolen = negativeRes.scalarProduct(SELL_COEFFICIENTS);
            teamResource.addMoney(-moneyStolen);
            teamResource.add(negativeRes.times(-1));
            getEventLogger().logMessage(getČeskéJméno() + " na lodi " + getRelatedShip().getName(), "Po prodeji byla některá ze surovin týmu záporná. Náprava: Odebírám týmu " + moneyStolen + " peněz a vracím mu suroviny " + negativeRes);
        }
    }

    @Override
    public String getČeskéJméno() {
        return "prodej komodit";
    }

    @Override
    protected void recomputeCost() {
        cost.clear();
        if (getCommodities() != null) {
            cost.setAll(ResourceReadOnly.fromMoney(getCommodities().scalarProduct(SELL_COEFFICIENTS) * -1));
            //set negative value, because this cost is actually a gain (we are selling something)
        }
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() -> {
                    if (getCommodities() == null) return false;
                    if (getCommodities().compare(Resource.ZERO).equals(PartialOrdering.Equal))
                        return false;
                    if (getCommodities().getMoney() != 0)
                        return false;
                    if (getRelatedShip().getTeam().getOwnedResource().minus(getCommodities()).anyComponentNegaitve()) {
                        if (shipHasPlannedAtLeast(1, UnloadStorage.class)
                                //&& shipHasPlannedLessThan(1, BuyCommodity.class)
                                )
                            return true;
                        else return false;
                    }
                    return true;
                }
                , getCommodities(), relatedShipProperty());
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.sellCommodity;
    }

}
