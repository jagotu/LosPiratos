package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class HeavyShot extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.heavyBall;}

    public static ResourceReadOnly getCost(){
        return new ResourceReadOnly();
    }
    @Override
    public String getČeskéJméno() {
        return "Těžká kule";
    }
}
