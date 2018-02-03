package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class ResourceActionParameter implements ActionParameter<Resource> {

    @Override
    public String getČeskéJméno() {
        return "Suroviny";
    }

    @Override
    public Class<Resource> getParameterType() {
        return Resource.class;
    }

    @Override
    public void set(Resource value) {
        r.set(value);
    }

    @Override
    public Resource get() {
        return r.get();
    }

    @Override
    public ObjectProperty<Resource> property() {
        return r;
    }

    @Override
    public BooleanBinding isSatisfied() {
        //TODO
        return Bindings.createBooleanBinding(() -> true);
    }

    ObjectProperty<Resource> r = new SimpleObjectProperty<>();
}
