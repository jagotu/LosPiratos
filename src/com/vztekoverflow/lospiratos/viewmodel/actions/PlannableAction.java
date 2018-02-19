package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.util.Translatable;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;

public interface PlannableAction extends Translatable {
    ActionIcon getIcon();

    /**
     * Indicates whether the Action should be shown to the user
     */
    BooleanExpression visibleProperty();

    /**
     * Indicates whether, with respect to current state, the Action may be planned
     */
    BooleanExpression plannableProperty();


    ObjectProperty<Ship> relatedShipProperty();

    /**
     * Indicates whether @preventedAction may be planned when this instance has already been planned
     */
    boolean preventsFromBeingPlanned(Action preventedAction);

    /**
     * @return PerformableAction that performs the action planned by this instance
     */
    PerformableAction asPerformableAction();

    Resource getCost();

}
