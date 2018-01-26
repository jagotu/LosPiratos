package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class Ram extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.ram;}

    public static ResourceReadOnly getCost(){
        return new ResourceReadOnly();
    }
    public String getČeskéJméno() {
        return "Kloun";
    }
}
