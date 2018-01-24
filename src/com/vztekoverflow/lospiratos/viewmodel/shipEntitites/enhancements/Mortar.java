package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class Mortar extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.mortar;}

    public static ResourceImmutable getCost(){
        return new ResourceImmutable();
    }
    public String getČeskéJméno() {
        return "Houfnice";
    }
}
