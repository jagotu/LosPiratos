package com.vztekoverflow.lospiratos.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResourceM {
    public final IntegerProperty money = new SimpleIntegerProperty();
    public final IntegerProperty cloth = new SimpleIntegerProperty();
    public final IntegerProperty metal = new SimpleIntegerProperty();
    public final IntegerProperty rum = new SimpleIntegerProperty();
    public final IntegerProperty wood = new SimpleIntegerProperty();
}
