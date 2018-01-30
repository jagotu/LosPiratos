package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class ChainShot extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.chain;}

    @Override
    public String getČeskéJméno() {
        return "Řetězová střela";
    }

    /*
     * Returns universal cost of this enhancement that is same for all instances, by calling a static method getCost().
     */
    public ResourceReadOnly getCostUniversal(){
        return getCost();
    }

    public static ResourceReadOnly getCost(){
        return new ResourceReadOnly();
    }
}
