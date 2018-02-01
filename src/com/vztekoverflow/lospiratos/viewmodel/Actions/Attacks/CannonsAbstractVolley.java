package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.Position;

import java.util.ArrayList;
import java.util.List;

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

    protected final void applyDamageToCannonsTargets(int damage){
        for(AxialCoordinate target: getCannonsTargets()){
            applyDamageTo(damage, target);
        }
    }
    protected final List<AxialCoordinate> getCannonsTargets(){
        List<AxialCoordinate> result = new ArrayList<>(2);
        int sign = 1;
        if(useLeftCannons) sign = -1;

        //left/right top:
        Position p = getRelatedShip().getPosition().createCopy();
        p.setRotation(p.getRotation() + 60 * sign);
        p.moveForward();
        result.add(p.getCoordinate());

        //left/right bottom:
        p = getRelatedShip().getPosition().createCopy();
        p.setRotation(p.getRotation() + 120 * sign);
        p.moveForward();
        result.add(p.getCoordinate());

        return result;
    }
}
