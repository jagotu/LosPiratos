package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.HeavyShot;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CannonsHeavyBallVolley extends CannonsAbstractVolley {

    public CannonsHeavyBallVolley(boolean leftCannons) {
        super(leftCannons);
    }

    @Override
    protected boolean recomputeVisible() {
        return  getRelatedShip().hasEnhancement(HeavyShot.class);
    }

    @Override
    protected boolean recomputePlannable() {
        return getRelatedShip().getPlannedActions().size() == 0;
        //todo also make sure that no other action is planned after this one
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        if(useLeftCannons)
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
