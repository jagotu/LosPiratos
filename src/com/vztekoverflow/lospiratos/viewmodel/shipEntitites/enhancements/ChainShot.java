package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class ChainShot extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.chain;}

    public static ResourceReadOnly getCost(){
        return new ResourceReadOnly();
    }
    @Override
    public String getČeskéJméno() {
        return "Řetězová střela";
    }
}
