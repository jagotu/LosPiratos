package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.util.Translatable;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableBooleanValue;

public interface PlannableAction extends Translatable {
    ActionIcon getIcon();

    /*
     * Indicates whether the Action should be shown to the user
     */
    ObservableBooleanValue visibleProperty();

    /*
     * Indicates whether, with respect to current state, the Action may be planned
     */
    ObservableBooleanValue plannableProperty();


    ObjectProperty<Ship> relatedShipProperty();

    /*
     * Indicates whether @preventedAction may be planned when this instance has already been planned
     */
    boolean preventsFromBeingPlanned(Action preventedAction);

    /*
     * @returns PerformableAction that performs the action planned by this instance
     */
    PerformableAction asPerformableAction();

}
