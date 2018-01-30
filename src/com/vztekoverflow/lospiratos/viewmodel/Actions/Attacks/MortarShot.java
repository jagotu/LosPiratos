package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.Mortar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MortarShot extends Attack {
    //todo tohle je paramaterized, bude chtit souradnice
    @Override
    protected boolean recomputeVisible() {
        return getRelatedShip().hasEnhancement(Mortar.class);
    }

    @Override
    protected boolean recomputePlannable() {
        return shipHasPlannedLessThan(1, MortarShot.class);
    }

    @Override
    public void performOnTargetInternal() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "střela z houfnice";
    }

    @Override
    protected Action createCopy(){
        return new MortarShot();
    }
}
