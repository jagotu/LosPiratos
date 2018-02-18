package com.vztekoverflow.lospiratos.viewmodel;

public interface Plunderable {

    default void addResource(ResourceReadOnly amount) {
        getResource().add(amount);
    }

    default void setResource(ResourceReadOnly amount) {
        getResource().setAll(amount);
    }

    /**
     * @return final object representing the resource hold by this board. The returned value is always the same.
     */
    Resource getResource();
}
