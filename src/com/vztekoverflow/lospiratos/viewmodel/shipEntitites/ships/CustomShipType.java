package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class CustomShipType extends ShipType {
    @Override
    public String getČeskéJméno() {
        return "uživatelský typ lodě";
    }

    @Override
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

    public static ResourceReadOnly getCost(){
        return new ResourceReadOnly();
    }
}
