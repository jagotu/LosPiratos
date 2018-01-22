package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.ChainShot;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CannonsChainShotVolley extends CannonsAbstractVolley {

    public CannonsChainShotVolley(boolean leftCannons) {
        super(leftCannons);
    }

    @Override
    protected boolean recomputeVisible() {
        return  getRelatedShip().hasEnhancement(ChainShot.class);
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        if(useLeftCannons)
            return "salva řetězovou střelou na levoboku";
        else return "salva řetězovou střelou na pravoboku";
    }

    @Override
    protected Action createCopy() {
        return new CannonsChainShotVolley(useLeftCannons);
    }


}
