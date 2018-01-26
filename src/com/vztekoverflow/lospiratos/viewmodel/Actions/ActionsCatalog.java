package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks.*;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.TurnLeft;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.TurnRight;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions.*;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class ActionsCatalog {

    /*
     * Contains all plannable actions that the game provides.
     * Every element in the list is an instance of class Action, thus
     *    casting to Action is guaranteed to be successful and may be convenient sometimes.
     * However, note that class Action implements also other interfaces and calling their
     *    functions randomly may corrupt the game.
     * Manipulating the Actions via interfaces PlannableAction and ParametrizedAction is safe.
     * To change the targetShip of the actions in the list, change ActionsCatalog.relatedShip
     */
    public final static Iterable<PlannableAction> allPossiblePlannableActions;

    public static final ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();

    static {
        List<PlannableAction> list = new ArrayList<>();
        list.add(new ActivatePrivilegedMode());
        //attacks
        list.add(new CannonsSimpleVolley(true));
        list.add(new CannonsSimpleVolley(false));
        list.add(new CannonsHeavyBallVolley(true));
        list.add(new CannonsHeavyBallVolley(false));
        list.add(new CannonsChainShotVolley(true));
        list.add(new CannonsChainShotVolley(false));
        list.add(new FrontalAssault());
        list.add(new MortarShot());
        //maneuvers
        list.add(new MoveForward());
        list.add(new TurnLeft());
        list.add(new TurnRight());
        //transactions
        list.add(new UnloadStorage());
        list.add(new BuyCommodity());
        list.add(new SellCommodity());
        list.add(new BuyNewEnhancement());
        list.add(new RepairEnhancement());
        list.add(new RepairShipViaDowngrade());
        list.add(new RepairShipViaRepayment());
        list.add(new UpgradeShip());
        allPossiblePlannableActions = list;

        for (PlannableAction a : allPossiblePlannableActions) {
            a.relatedShipProperty().bindBidirectional(relatedShip);
        }
    }
}
