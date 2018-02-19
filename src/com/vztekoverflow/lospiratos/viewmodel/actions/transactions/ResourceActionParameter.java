package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class ResourceActionParameter implements ActionParameter<Resource> {

    @Override
    public String getČeskéJméno() {
        return "Suroviny";
    }

    /**
     * No. Don't call. The property is read only.
     * @param value
     */
    @Deprecated
    @Override
    public final void set(Resource value) {
        r.set(value);
    }

    @Override
    public final Resource get() {
        return r.get();
    }

    @Override
    public final ObjectProperty<Resource> property() {
        return r;
    }

    final ObjectProperty<Resource> r = new ReadOnlyObjectWrapper<>(new Resource());

}
