package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;

public class Chained extends ShipMechanics {
    @Override
    public String getČeskéJméno() {
        return "Zařetězeno";
    }

    @Override
    public com.vztekoverflow.lospiratos.model.ShipMechanics getModelDescription() {
        return com.vztekoverflow.lospiratos.model.ShipMechanics.chained;
    }

    int roundsActive = 0;

    @Override
    public void onNextRoundStarted(int roundNo) {
        super.onNextRoundStarted(roundNo);
        roundsActive++;
        if (roundsActive >= 2) {
            ship.mechanicsProperty().remove(this);
        }
    }

    @Override
    public boolean preventsFromBeingPlanned(Action preventedAction) {
        return preventedAction instanceof MoveForward;
    }
}
