package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;

import java.util.ArrayList;
import java.util.List;

abstract public class CommodityTransaction extends ManeuverTransaction implements ParameterizedAction {

    private List<ActionParameter> params = new ArrayList<>();

    protected CommodityTransaction() {
        params.add(commodities);
        getCommodities().addListener(__ ->
                recomputeCost()
        );
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return params;
    }

    private final ResourceActionParameter commodities = new ResourceActionParameter();

    public Resource getCommodities() {
        return commodities.get();
    }

}
