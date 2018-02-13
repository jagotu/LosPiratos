package com.vztekoverflow.lospiratos.viewmodel.actions;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public interface ValidableActionParameter<T> extends ActionParameter<T> {
    /**
     * Indicates whether @value is valid and thus could be assigned as this object's property's value.
     */
    default boolean isValidValue(T value){
        return validValueProperty(new SimpleObjectProperty<>(value)).get();
    }

    /**
     * Indicates whether the value that has already been set is valid.
     */
    default boolean isValid() {
        return isValidValue(get());
    }

    /**
     * Returns BooleanExpression indicating whether @value is valid and thus could be assigned as this object's property's value.
     * If some dependencies change, expression will be invalidated.
     */
    BooleanExpression validValueProperty(ObservableValue<T> value);

    /**
     * Returns BooleanExpression indicating whether the value that has already been set is valid.
     * If some dependencies change, expression will be invalidated.
     */
    default BooleanExpression validProperty(){
        return validValueProperty(property());
    }

}
