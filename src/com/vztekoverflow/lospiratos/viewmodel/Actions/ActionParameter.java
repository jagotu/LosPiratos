package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.util.Translatable;
import javafx.beans.property.ObjectProperty;

public interface ActionParameter<T> extends Translatable {
    Class<T> getParameterType();
    void set(T value);
    T get();
    ObjectProperty<T> property();
}


