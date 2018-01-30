package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Attack;

public abstract class CannonsAbstractVolley extends Attack {
    final protected boolean useLeftCannons; //false ~ right, true ~ left
    protected CannonsAbstractVolley(boolean useLeftCannons) {
        this.useLeftCannons = useLeftCannons;
    }

    @Override
    protected boolean recomputePlannable() {
        return getRelatedShip().getPlannedActions().stream().
                noneMatch(a -> CannonsAbstractVolley.class.isAssignableFrom(a.getClass()) &&
                        ((CannonsAbstractVolley)a).useLeftCannons == useLeftCannons
                );
    }
}
