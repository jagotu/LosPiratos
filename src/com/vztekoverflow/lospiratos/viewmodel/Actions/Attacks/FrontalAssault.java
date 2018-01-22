package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.Ram;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FrontalAssault extends Attack {

    @Override
    protected boolean recomputeVisible() {
        return getRelatedShip().hasEnhancement(Ram.class);
    }

    @Override
    protected boolean recomputePlannable() {
        return shipHasPlannedLessThan(1, FrontalAssault.class);
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "čelní útok";
    }

    @Override
    protected Action createCopy(){
        return new FrontalAssault();
    }

}
