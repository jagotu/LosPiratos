package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.ChainShot;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics.Chained;

public class CannonsChainShotVolley extends CannonsAbstractVolley {

    public CannonsChainShotVolley(boolean leftCannons) {
        super(leftCannons);
    }

    @Override
    protected boolean recomputeVisible() {
        return getRelatedShip().hasEnhancement(ChainShot.class);
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() &&
                getRelatedShip().hasActiveEnhancement(ChainShot.class);
    }

    @Override
    public void performOnTargetInternal() {
        for (AxialCoordinate target : getCannonsTargets()) {
            applyMechanicsTo(new Chained(), target);
        }
    }

    @Override
    public String getČeskéJméno() {
        if (useLeftCannons)
            return "salva řetězovou střelou na levoboku";
        else return "salva řetězovou střelou na pravoboku";
    }

    @Override
    protected Action createCopy() {
        return new CannonsChainShotVolley(useLeftCannons);
    }


}
