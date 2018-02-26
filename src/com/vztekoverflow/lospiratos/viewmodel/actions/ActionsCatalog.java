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
import java.util.List;

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
        Node attacks = new Node(false).withShortcut("a");
        attacks.addChild(new Node(new FrontalAssault()).withShortcut("w"));
        attacks.addChild(new Node(new CannonsSimpleVolley(false)).withShortcut("e"));
        attacks.addChild(new Node(new CannonsHeavyBallVolley(false)).withShortcut("d"));
        attacks.addChild(new Node(new CannonsChainShotVolley(false)).withShortcut("c"));
        attacks.addChild(new Node(new MortarShot()).withShortcut("x"));
        attacks.addChild(new Node(new CannonsChainShotVolley(true)).withShortcut("y"));
        attacks.addChild(new Node(new CannonsHeavyBallVolley(true)).withShortcut("a"));
        attacks.addChild(new Node(new CannonsSimpleVolley(true)).withShortcut("q"));
        attacks.icon = ActionIcon.attackGenericIcon;


        //transactions
        Node transactions = new Node(false).withShortcut("d");
        transactions.addChild(new Node(new UnloadStorage()).withShortcut("w"));
        transactions.addChild(new Node(new BuyCommodity()).withShortcut("e"));
        transactions.addChild(new Node(new SellCommodity()).withShortcut("d"));
        transactions.addChild(new Node(new BuyNewEnhancement()).withShortcut("c"));
        transactions.addChild(new Node(new RepairEnhancement()).withShortcut("x"));
        transactions.addChild(new Node(new RepairShipViaDowngrade()).withShortcut("y"));
        transactions.addChild(new Node(new RepairShipViaRepayment()).withShortcut("a"));
        transactions.addChild(new Node(new UpgradeShip()).withShortcut("q"));
        transactions.icon = ActionIcon.transactionGenericIcon;

        Node additionals = new Node(false).withShortcut("c");
        additionals.icon = ActionIcon.ellipsis;
        additionals.addChild(new Node(new EmptyAction()).withShortcut("w"));
        additionals.addChild(new Node(new ActivatePrivilegedMode()).withShortcut("x"));


        //maneuvers and categories, in the order in which they appear in UI
        root.addChild(new Node(new MoveForward()).withShortcut("w"));
        root.addChild(new Node(new TurnRight()).withShortcut("e"));
        root.addChild(transactions);
        root.addChild(additionals);
        //root.addChild(new Node(new ActivatePrivilegedMode()));
        root.addChild(new Node(new Plunder()).withShortcut("y"));
        root.addChild(attacks);
        root.addChild(new Node(new TurnLeft()).withShortcut("q"));

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

        public List<Node> getChildren() {
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

        public String getShortcut() {
            return shortcut;
        }

        public void setShortcut(String shortcut) {
            this.shortcut = shortcut;
        }

        String shortcut = "ě";

        public Node getParent() {
            return parent;
        }

        public ActionIcon getIcon() {
            if (action != null) {
                return action.getIcon();
            }
            return icon;
        }

        public Node withShortcut(String shortcut)
        {
            setShortcut(shortcut);
            return this;
        }
    }
}
