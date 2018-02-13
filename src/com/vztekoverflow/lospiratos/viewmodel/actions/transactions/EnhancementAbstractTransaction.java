package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import javafx.beans.property.ObjectProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class EnhancementAbstractTransaction extends ChangeShipAbstractTransaction implements ParameterizedAction {
    private List<ActionParameter> params = new ArrayList<>();

    protected EnhancementAbstractTransaction() {
        params.add(getEnhancementParameter());
        getEnhancementParameter().property().addListener(__ -> cost.invalidate());
    }

    // concrete implementation of parameter should be supported by inheritors
    abstract protected EnhancementActionParameter getEnhancementParameter();

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return params;
    }

    /**
     * equivalent for calling params.get(0).property().get()
     */
    public Class<? extends ShipEnhancement> getEnhancement() {
        return getEnhancementParameter().get();
    }

    /**
     * equivalent for calling params.get(0).property().set()
     */
    public void setEnhancement(Class<? extends ShipEnhancement> enhancement) {
        this.getEnhancementParameter().set(enhancement);
    }

    /**
     * equivalent for calling params.get(0).property()
     */
    public ObjectProperty<Class<? extends ShipEnhancement>> enhancementProperty() {
        return getEnhancementParameter().property();
    }
}
