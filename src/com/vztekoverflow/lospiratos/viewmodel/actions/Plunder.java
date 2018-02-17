package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.Plunderable;
import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.ResourceActionParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class Plunder extends Action implements ParameterizedAction {
    @Override
    protected Action createCopyAndResetThis() {
        return new Plunder();
    }

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    protected boolean recomputePlannable() {
        Position pos = getRelatedShipsFuturePosition();
        Plunderable p = getRelatedShip().getTeam().getGame().getBoard().getPlunderable(pos.getCoordinate());
        return p != null;
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return ResourceReadOnly.ZERO;
    }

    @Override
    protected void performOnShipInternal() {
        //todo ?? fakt nevim jak
    }


    private List<ActionParameter> params = new ArrayList<>();

    protected Plunder() {
        params.add(commodities);
        commoditiesProperty().addListener(__ -> cost.invalidate());
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return params;
    }

    private ResourceActionParameter commodities = new ResourceActionParameter() {
        @Override
        public String getČeskéJméno() {
            return "kolik vyplundrovat";
        }
    };

    public Resource getCommodities() {
        return commodities.get();
    }

    public ObjectProperty<Resource> commoditiesProperty() {
        return commodities.property();
    }

    public void setCommodities(Resource commodities) {
        this.commodities.set(commodities);
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() -> true);
        //this action just does not care how much you want to plunder
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.plunder;
    }

    @Override
    public String getČeskéJméno() {
        return "plundrování";
    }
}
