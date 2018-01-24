package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.Actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import javafx.beans.property.ObjectProperty;

import java.util.ArrayList;
import java.util.List;

abstract public class CommodityTransaction extends Transaction implements ParameterizedAction {

    private List<ActionParameter> params = new ArrayList<>();

    protected CommodityTransaction() {
        params.add(commodities);
        commoditiesProperty().addListener(__ -> cost.invalidate());
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return params;
    }
    private ResourceActionParameter commodities = new ResourceActionParameter();

    public Resource getCommodities() {
        return commodities.get();
    }

    public ObjectProperty<Resource> commoditiesProperty() {
        return commodities.property();
    }

    public void setCommodities(Resource commodities) {
        this.commodities.set(commodities);
    }

}
