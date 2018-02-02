package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Frigate extends ShipType {
    @Override
    public int getBonusMaxHP() {
        return 70;
    }

    @Override
    public int getBonusCannonsCount() {
        return 20;
    }

    @Override
    public int getBonusCargoSpace() {
        return 800;
    }

    @Override
    public int getBonusGarrison() {
        return 100;
    }

    @Override
    public int getBonusSpeed() {
        return 3;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly();
    }

    @Override
    public String getČeskéJméno() {
        return "Frigata";
    }

    @Override
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

}

