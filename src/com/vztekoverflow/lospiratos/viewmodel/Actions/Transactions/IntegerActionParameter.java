package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

abstract public class IntegerActionParameter implements ActionParameter<Integer> {

    @Override
    public Class<Integer> getParameterType() {
        return Integer.class;
    }

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
