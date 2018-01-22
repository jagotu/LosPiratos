package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;

abstract class ShipChangeAbstractTransaction extends Transaction {
    @Override
    public boolean preventsFromBeingPlanned(Action preventedAction) {
        return Maneuver.class.isAssignableFrom(preventedAction.getClass()) ||
                Attack.class.isAssignableFrom(preventedAction.getClass()) ;
    }
}
