package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.*;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.TurnLeft;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.TurnRight;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

public class ActionsCatalog {

    /**
     * Contains all plannable actions that the game provides.
     * Every element in the list is an instance of class Action, thus
     * casting to Action is guaranteed to be successful and may be convenient sometimes.
     * However, note that class Action implements also other interfaces and calling their
     * functions randomly may corrupt the game.
     * Manipulating the Actions via interfaces PlannableAction and ParametrizedAction is safe.
     * To change the targetShip of the actions in the list, change ActionsCatalog.relatedShip
     */
    public final static Node allPossiblePlannableActions;

    public static final ObjectProperty<Ship> relatedShip = new SimpleObjectProperty<>();

    static {

        Node root = new Node(false);


        //attacks, in the order in which they appear in UI
        Node attacks = new Node(false);
        attacks.addChild(new Node(new FrontalAssault()));
        attacks.addChild(new Node(new CannonsSimpleVolley(false)));
        attacks.addChild(new Node(new CannonsHeavyBallVolley(false)));
        attacks.addChild(new Node(new CannonsChainShotVolley(false)));
        attacks.addChild(new Node(new MortarShot()));
        attacks.addChild(new Node(new CannonsChainShotVolley(true)));
        attacks.addChild(new Node(new CannonsHeavyBallVolley(true)));
        attacks.addChild(new Node(new CannonsSimpleVolley(true)));
        attacks.icon = ActionIcon.attackGenericIcon;


        //transactions
        Node transactions = new Node(false);
        transactions.addChild(new Node(new UnloadStorage()));
        transactions.addChild(new Node(new BuyCommodity()));
        transactions.addChild(new Node(new SellCommodity()));
        transactions.addChild(new Node(new BuyNewEnhancement()));
        transactions.addChild(new Node(new RepairEnhancement()));
        transactions.addChild(new Node(new RepairShipViaDowngrade()));
        transactions.addChild(new Node(new RepairShipViaRepayment()));
        transactions.addChild(new Node(new UpgradeShip()));
        transactions.icon = ActionIcon.transactionGenericIcon;

        Node additionals = new Node(false);
        additionals.icon = ActionIcon.ellipsis;


        //maneuvers and categories, in the order in which they appear in UI
        root.addChild(new Node(new MoveForward()));
        root.addChild(new Node(new TurnRight()));
        root.addChild(transactions);
        //root.addChild(additionals);
        root.addChild(new Node(new ActivatePrivilegedMode()));
        root.addChild(new Node(new Plunder()));
        root.addChild(attacks);
        root.addChild(new Node(new TurnLeft()));

        allPossiblePlannableActions = root;

        bindNode(root);
    }

    private static void bindNode(Node n) {
        if (n.leaf) {
            n.action.relatedShipProperty().bindBidirectional(relatedShip);
        } else {
            for (Node a : n.children) {
                bindNode(a);
            }
        }
    }

    public static class Node {
        public boolean isLeaf() {
            return leaf;
        }

        public Iterable<Node> getChildren() {
            return children;
        }

        public PlannableAction getAction() {
            return action;
        }

        void addChild(Node n) {
            this.children.add(n);
            n.parent = this;
        }

        Node(boolean leaf) {
            this.leaf = leaf;
            if (!leaf) {
                children = new ArrayList<>();
            }
        }

        Node(PlannableAction action) {
            this.action = action;
            this.leaf = true;
        }

        boolean leaf = false;
        ArrayList<Node> children = null;
        PlannableAction action = null;
        ActionIcon icon = null;
        Node parent = null;

        public Node getParent() {
            return parent;
        }

        public ActionIcon getIcon() {
            if (action != null) {
                return action.getIcon();
            }
            return icon;
        }
    }
}
