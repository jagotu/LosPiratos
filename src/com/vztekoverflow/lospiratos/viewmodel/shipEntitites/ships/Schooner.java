package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;


public class Schooner extends ShipType {
    @Override
    public int getBonusMaxHP() {
        return 30;
    }

    @Override
    public int getBonusCannonsCount() {
        return 8;
    }

    @Override
    public int getBonusCargoSpace() {
        return 250;
    }

    @Override
    public int getBonusGarrison() {
        return 30;
    }

    @Override
    public int getBonusSpeed() {
        return 4;
    }


    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly();
    }

    @Override
    public String getČeskéJméno() {
        return "Škuner";
    }

    @Override
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

}
