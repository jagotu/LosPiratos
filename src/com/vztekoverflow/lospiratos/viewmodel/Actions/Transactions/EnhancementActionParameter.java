package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementIcon;
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
    private ObjectProperty<EnhancementIcon> enh;

    public EnhancementActionParameter(ObjectProperty<EnhancementIcon> property) {
        this.enh = property;
    }
}
