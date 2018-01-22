package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks.*;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.TurnLeft;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.TurnRight;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ActionsCatalogue {
    //Transactions are todo
    public final static PlannableAction[] allPossiblePlannableActions = {
            new ActivatePrivilegedMode(),
            new CannonsSimpleVolley(true),
            new CannonsSimpleVolley(false),
            new CannonsHeavyBallVolley(true),
            new CannonsHeavyBallVolley(false),
            new CannonsChainShotVolley(true),
            new CannonsChainShotVolley(false),
            new FrontalAssault(),
            new MortarShot(),
            new MoveForward(),
            new TurnLeft(),
            new TurnRight()
    };

    public static ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();

    static{
        for(PlannableAction a : allPossiblePlannableActions){
            a.relatedShipProperty().bindBidirectional(relatedShip);
        }
    }
}
