package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.HeavyShot;

public class CannonsHeavyBallVolley extends CannonsAbstractVolley {

    public static final double HEAVY_BALL_DAMAGE_COEFFICIENT = 1.5;

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
    public void performOnTargetInternal() {
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
    protected Action createCopy() {
        return new CannonsHeavyBallVolley(useLeftCannons);
    }
}
