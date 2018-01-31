package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public abstract class ShipEnhancement extends ShipEntity {

    private BooleanProperty destroyed = new SimpleBooleanProperty(false);

    public boolean isDestroyed() {
        return destroyed.getValue();
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed.set(destroyed);
    }

    public BooleanProperty destroyedProperty() {
        return destroyed;
    }

    public abstract EnhancementIcon getIcon();

    /**
     * Returns universal cost of this enhancement that is same for all instances.
     */
    abstract public ResourceReadOnly getCostUniversal();

}

