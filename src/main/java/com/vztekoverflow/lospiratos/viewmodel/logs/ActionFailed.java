package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;

class ActionFailed extends TransactionLoggedEvent {
    @Override
    public String getTextualDescription(LogFormatter f) {
        return "Transakce " + f.format(action) + " na lodi " + f.format(owner) + "se neprovedla. Důvod: " + reason;
    }

    private Action action;
    private Ship owner;
    private String reason;

    ActionFailed(Action a, Ship owner, String reason) {
        this.action = a;
        this.owner = owner;
        this.reason = reason;
    }

    public Action getAction() {
        return action;
    }

    public Ship getOwner() {
        return owner;
    }

    public String getReason() {
        return reason;
    }
}
