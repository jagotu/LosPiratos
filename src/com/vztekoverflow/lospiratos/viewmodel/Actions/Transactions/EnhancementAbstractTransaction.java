package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.Actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementIcon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public abstract class EnhancementAbstractTransaction extends ChangeShipAbstractTransaction implements ParameterizedAction {
    private List<ActionParameter> params = new ArrayList<>();

    protected EnhancementAbstractTransaction() {
        //todo enhIcon <> enhClass 2way binding
        params.add(enhancementIcon);
        enhancement.addListener(__ -> cost.invalidate());
    }

    @Override
    public Iterable<ActionParameter> getAvailableParameters() {
        return null;
    }
    private ObjectProperty<Class<? extends ShipEnhancement>> enhancement = new SimpleObjectProperty<Class<? extends ShipEnhancement>>(){
        @Override
        public Class<? extends ShipEnhancement> get() {
            throw new NotImplementedException();
            //todo enhIcon <> enhClass 2way binding
        }

        @Override
        public void set(Class<? extends ShipEnhancement> newValue) {
            throw new NotImplementedException();
            //todo enhIcon <> enhClass 2way binding
        }
    };

    private EnhancementActionParameter enhancementIcon = new EnhancementActionParameter(
            new SimpleObjectProperty<EnhancementIcon>(){
                @Override
                public EnhancementIcon get() {
                    throw new NotImplementedException();
                    //todo enhIcon <> enhClass 2way binding
                }

                @Override
                public void set(EnhancementIcon newValue) {
                    throw new NotImplementedException();
                    //todo enhIcon <> enhClass 2way binding
                }
            }
    );

    public Class<? extends ShipEnhancement> getEnhancement() {
        return enhancement.get();
    }

    public void setEnhancement(Class<? extends ShipEnhancement> enhancement) {
        this.enhancement.set(enhancement);
    }

    public EnhancementIcon getEnhancementIcon() {
        return enhancementIcon.get();
    }

    public void setEnhancementIcon(EnhancementIcon enhancementIcon) {
        this.enhancementIcon.set(enhancementIcon);
    }

    public ObjectProperty<EnhancementIcon> enhancementIconProperty(){
        return enhancementIcon.property();
    }
}
