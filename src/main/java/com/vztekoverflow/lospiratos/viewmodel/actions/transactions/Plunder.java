package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Plunderable;
import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Plunder extends Transaction implements ParameterizedAction {
    @Override
    protected Action createCopyAndResetThis() {
        Plunder result = new Plunder();
        result.getCommodities().setAll(this.getCommodities());
        getCommodities().setAll(ResourceReadOnly.MAX);
        return result;
    }

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    public int getManeuverSlotsTaken() {
        return 1;
    }

    @Override
    protected boolean recomputePlannable() {
        int shipSpeed = getRelatedShip().getSpeed();
        int maneuversAlreadyPlanned = getRelatedShip().getPlannedActions().stream().mapToInt(Action::getManeuverSlotsTaken).sum();
        boolean enoughSpeed = maneuversAlreadyPlanned < shipSpeed;
        Position pos = getRelatedShipsFuturePosition();
        Plunderable p = getRelatedShip().getTeam().getGame().getBoard().getPlunderable(pos.getCoordinate());

        if(p!= null && getCommodities() != null && getCommodities().equals(ResourceReadOnly.MAX))
            getCommodities().setAll(p.getResource());
        return p != null && enoughSpeed;
    }

    @Override
    protected void recomputeCost() {
        //nothing, there this actions costs nothing
    }

    @Override
    protected void performOnShipInternal() {
        AxialCoordinate position = getRelatedShip().getPosition().getCoordinate();
        Plunderable target = getRelatedShip().getTeam().getGame().getBoard().getPlunderable(position);
        if(target == null){
            getEventLogger().logActionNotPerformed(this,"na políčku " + position + " nelze plundrovat");
            return;
        }
        for(OnPlunderRequestedListener l : onPlunderRequestedListeners){
            l.onPlunderRequested(target,this);
        }
    }


    private List<ActionParameter> params = new ArrayList<>();

    public Plunder() {
        params.add(commodities);
        getCommodities().addListener(__ -> recomputeCost());
        commodities.get().setAll(ResourceReadOnly.MAX);
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
    }
    ;

    public Resource getCommodities() {
        return commodities.get();
    }

    @Override
    public BooleanExpression satisfiedProperty() {
        return Bindings.createBooleanBinding(() -> {
            if(getRelatedShip() == null || getCommodities() == null) return false;
            return getRelatedShip().getStorage().getCapacityLeft() >= getCommodities().scalarProduct(new ResourceReadOnly(0,1,1,1,1,1));
        }, getCommodities(), getRelatedShip().getStorage().capacityLeftProperty());
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.plunder;
    }

    @Override
    public String getČeskéJméno() {
        return "plundrování";
    }

    public void addListener(OnPlunderRequestedListener listener) {
        onPlunderRequestedListeners.add(listener);
    }

    public void removeListener(OnPlunderRequestedListener listener) {
        onPlunderRequestedListeners.remove(listener);
    }

    private Set<OnPlunderRequestedListener> onPlunderRequestedListeners = new HashSet<>();
}
