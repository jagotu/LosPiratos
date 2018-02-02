package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.actions.Transaction;

abstract class ChangeShipAbstractTransaction extends Transaction {
    @Override
    public boolean preventsFromBeingPlanned(Action preventedAction) {
        return Maneuver.class.isAssignableFrom(preventedAction.getClass()) ||
                Attack.class.isAssignableFrom(preventedAction.getClass());
    }
}
