package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;

public class CannonsSimpleVolley extends CannonsAbstractVolley {

    public CannonsSimpleVolley(boolean leftCannons) {
        super(leftCannons);
    }


    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    public void performOnTargetInternal() {
        applyDamageToCannonsTargets(getRelatedShip().getCannonsCount());
    }

    @Override
    public String getČeskéJméno() {
        if(useLeftCannons)
            return "salva děl na levoboku";
        else return "salva děl na pravoboku";
    }
    @Override
    protected Action createCopy() {
        return new CannonsSimpleVolley(useLeftCannons);
    }
}
