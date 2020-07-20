package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.ValidableActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class EnhancementActionParameter implements ValidableActionParameter<Class<? extends ShipEnhancement>> {

    @Override
    public String getČeskéJméno() {
        return "Vylepšení";
    }

    @Override
    public void set(Class<? extends ShipEnhancement> value) {
        enh.set(value);
    }

    @Override
    public Class<? extends ShipEnhancement> get() {
        return enh.get();
    }

    @Override
    public ObjectProperty<Class<? extends ShipEnhancement>> property() {
        return enh;
    }

    @Override
    public String getJsonMapping() {
        return "enhancement";
    }

    private ObjectProperty<Class<? extends ShipEnhancement>> enh = new SimpleObjectProperty<>();

}
