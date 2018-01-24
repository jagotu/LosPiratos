package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks.*;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.TurnLeft;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.TurnRight;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class ActionsCatalog {


    //Transactions are todo
    public final static Iterable<PlannableAction> allPossiblePlannableActions;

    public static ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();

    static {
        List<PlannableAction> list = new ArrayList<>();
        list.add(new ActivatePrivilegedMode());
        list.add(new CannonsSimpleVolley(true));
        list.add(new CannonsSimpleVolley(false));
        list.add(new CannonsHeavyBallVolley(true));
        list.add(new CannonsHeavyBallVolley(false));
        list.add(new CannonsChainShotVolley(true));
        list.add(new CannonsChainShotVolley(false));
        list.add(new FrontalAssault());
        list.add(new MortarShot());
        list.add(new MoveForward());
        list.add(new TurnLeft());
        list.add(new TurnRight());
        allPossiblePlannableActions = list;

        for (PlannableAction a : allPossiblePlannableActions) {
            a.relatedShipProperty().bindBidirectional(relatedShip);
        }
    }
}
