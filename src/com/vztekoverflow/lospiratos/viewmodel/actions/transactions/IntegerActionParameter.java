package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

abstract public class IntegerActionParameter implements ActionParameter<Integer> {

    @Override
    public void set(Integer value) {
        this.value.set(value);
    }

    @Override
    public Integer get() {
        return value.get();
    }

    @Override
    public ObjectProperty<Integer> property() {
        return value;
    }

    private ObjectProperty<Integer> value = new SimpleObjectProperty<>();
}
