package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;

public class EnhancementActionParameter implements ActionParameter<EnhancementIcon> {

    @Override
    public String getČeskéJméno() {
        return "Rozšíření";
    }

    @Override
    public Class<EnhancementIcon> getParameterType() {
        return EnhancementIcon.class;
    }

    @Override
    public void set(EnhancementIcon value) {
        enh.set(value);
    }

    @Override
    public EnhancementIcon get() {
        return enh.get();
    }

    @Override
    public ObjectProperty<EnhancementIcon> property() {
        return enh;
    }

    @Override
    public BooleanBinding isSatisfied() {
        //TODO
        return Bindings.createBooleanBinding(() -> true);
    }

    private ObjectProperty<EnhancementIcon> enh;

    public EnhancementActionParameter(ObjectProperty<EnhancementIcon> property) {
        this.enh = property;
    }
}
