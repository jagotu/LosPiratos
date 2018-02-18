package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Galleon extends ShipType {

    @Override
    public int getBonusMaxHP() {
        return 100;
    }

    @Override
    public int getBonusCannonsCount() {
        return 30;
    }

    @Override
    public int getBonusCargoSpace() {
        return 1200;
    }

    @Override
    public int getBonusGarrison() {
        return 200;
    }

    @Override
    public int getBonusSpeed() {
        return 2;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(42, 0, 0, 0, 0, 0);
    }

    @Override
    public String getČeskéJméno() {
        return "Galeona";
    }

    @Override
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

}
