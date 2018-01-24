package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
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

    ObjectProperty<Resource> r = new SimpleObjectProperty<>();
}
