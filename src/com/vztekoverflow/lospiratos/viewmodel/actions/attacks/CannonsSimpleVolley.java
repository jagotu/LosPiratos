package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;

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
        if (useLeftCannons)
            return "salva děl na levoboku";
        else return "salva děl na pravoboku";
    }

    @Override
    protected Action createCopy() {
        return new CannonsSimpleVolley(useLeftCannons);
    }

    @Override
    public ActionIcon getIcon() {
        if (useLeftCannons) {
            return ActionIcon.cannonLeft;
        }
        return ActionIcon.cannonRight;
    }
}
