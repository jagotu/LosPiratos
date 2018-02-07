package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class ResourceActionParameter implements ActionParameter<Resource> {

    @Override
    public String getČeskéJméno() {
        return "suroviny";
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
