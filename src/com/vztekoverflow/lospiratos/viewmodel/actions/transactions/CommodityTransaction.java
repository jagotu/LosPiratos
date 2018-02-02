package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.Transaction;
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
