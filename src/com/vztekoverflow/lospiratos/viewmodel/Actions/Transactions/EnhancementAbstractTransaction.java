package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.Actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class EnhancementAbstractTransaction extends ChangeShipAbstractTransaction implements ParameterizedAction {
    private List<ActionParameter> params = new ArrayList<>();

    protected EnhancementAbstractTransaction() {
        params.add(enhancementIcon);
        enhancement.addListener(__ -> cost.invalidate());
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return null;
    }
    private ObjectProperty<Class<? extends ShipEnhancement>> enhancement = new SimpleObjectProperty<Class<? extends ShipEnhancement>>(){
        @Override
        public void set(Class<? extends ShipEnhancement> newValue) {
            //support for enhIcon <> enhClass 2way binding
            EnhancementIcon icon = EnhancementsCatalog.getIcon(newValue);
            if(enhancementIcon.property().get().equals(icon)) return;
            //else
            enhancementIcon.property().set(icon);
        }
    };

    private EnhancementActionParameter enhancementIcon = new EnhancementActionParameter(
            new SimpleObjectProperty<EnhancementIcon>(){
                @Override
                public void set(EnhancementIcon newValue) {
                    //support for enhIcon <> enhClass 2way binding
                    Class<? extends ShipEnhancement> e = EnhancementsCatalog.getEnhancementFromIcon(newValue);
                    if(enhancement.get().equals(e)) return;
                    //else
                    enhancement.set(e);
                }
            }
    );

    public Class<? extends ShipEnhancement> getEnhancement() {
        return enhancement.get();
    }

    public void setEnhancement(Class<? extends ShipEnhancement> enhancement) {
        this.enhancement.set(enhancement);
    }

    /**
     * equivalent for calling params.get(0).property().get()
     */
    public EnhancementIcon getEnhancementIcon() {
        return enhancementIcon.get();
    }

    /**
     * equivalent for calling params.get(0).property().set()
     */
    public void setEnhancementIcon(EnhancementIcon enhancementIcon) {
        this.enhancementIcon.set(enhancementIcon);
    }

    /**
     * equivalent for calling params.get(0).property()
     */
    public ObjectProperty<EnhancementIcon> enhancementIconProperty(){
        return enhancementIcon.property();
    }
}
