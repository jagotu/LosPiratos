package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class HeavyShot extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.heavyBall;}

    public static ResourceImmutable getCost(){
        return new ResourceImmutable();
    }
    @Override
    public String getČeskéJméno() {
        return "Těžká kule";
    }
}
