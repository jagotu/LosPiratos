package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class Mortar extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon() {
        return EnhancementIcon.mortar;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly();
    }

    public String getČeskéJméno() {
        return "Houfnice";
    }

    /*
     * Returns universal cost of this enhancement that is same for all instances, by calling a static method getCost().
     */
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

}
