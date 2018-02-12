package com.vztekoverflow.lospiratos.viewmodel.actions;

public interface ValidableActionParameter<T> extends ActionParameter<T> {
    /**
     * Indicates whether @value is valid and thus could be assigned as this object's property's value
     *
     * @param value
     * @return
     */
    boolean isValidValue(T value);

    /**
     * Indicates whether the value that has already been set is valid
     *
     * @return
     */
    default boolean isValid() {
        return isValidValue(get());
    }
}
