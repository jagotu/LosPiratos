package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.HeavyShot;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.HEAVY_BALL_DAMAGE_COEFFICIENT;

public class CannonsHeavyBallVolley extends CannonsAbstractVolley {



    public CannonsHeavyBallVolley(boolean leftCannons) {
        super(leftCannons);
    }

    @Override
    protected boolean recomputeVisible() {
        return getRelatedShip().hasEnhancement(HeavyShot.class);
    }

    @Override
    protected boolean recomputePlannable() {
        return getRelatedShip().hasActiveEnhancement(HeavyShot.class) &&
                getRelatedShip().getPlannedActions().size() == 0;
    }

    @Override
    public void performOnShipInternal() {
        applyDamageToCannonsTargets((int) Math.round(getRelatedShip().getCannonsCount() * HEAVY_BALL_DAMAGE_COEFFICIENT));
    }

    @Override
    public String getČeskéJméno() {
        if (useLeftCannons)
            return "salva těžkou kulí na levoboku";
        else return "salva těžkou kulí na pravoboku";
    }

    @Override
    public boolean preventsFromBeingPlanned(Action preventedAction) {
        return true; //HeavyBall prevents any action from being planned
    }

    @Override
    protected Action createCopyAndResetThis() {
        return new CannonsHeavyBallVolley(useLeftCannons);
    }

    @Override
    public ActionIcon getIcon() {
        if (useLeftCannons) {
            return ActionIcon.ballLeft;
        }
        return ActionIcon.ballRight;
    }
}
