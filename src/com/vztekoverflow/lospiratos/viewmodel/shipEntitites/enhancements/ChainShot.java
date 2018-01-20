package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

public final class ChainShot extends ShipEnhancement {

    public static ResourceImmutable getCost(){
        return new ResourceImmutable();
    }
    @Override
    public String getČeskéJméno() {
        return "Řetězová střela";
    }
}
