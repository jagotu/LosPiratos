package com.vztekoverflow.lospiratos.model;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResourceM {
    public final IntegerProperty money = new SimpleIntegerProperty();
    public final IntegerProperty cloth = new SimpleIntegerProperty();
    public final IntegerProperty metal = new SimpleIntegerProperty();
    public final IntegerProperty rum = new SimpleIntegerProperty();
    public final IntegerProperty wood = new SimpleIntegerProperty();

    public ResourceM(Resource from) {
        money.setValue(from.getMoney());
        cloth.setValue(from.getCloth());
        metal.setValue(from.getMetal());
        rum.setValue(from.getRum());
        wood.setValue(from.getWood());
    }

    public ResourceM(ResourceReadOnly from) {
        money.setValue(from.getMoney());
        cloth.setValue(from.getCloth());
        metal.setValue(from.getMetal());
        rum.setValue(from.getRum());
        wood.setValue(from.getWood());
    }

    public ResourceM() {
    }
}
