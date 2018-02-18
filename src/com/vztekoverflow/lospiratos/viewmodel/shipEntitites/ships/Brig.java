package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Brig extends ShipType {

    @Override
    public int getBonusMaxHP() {
        return 45;
    }

    @Override
    public int getBonusCannonsCount() {
        return 12;
    }

    @Override
    public int getBonusCargoSpace() {
        return 480;
    }

    @Override
    public int getBonusGarrison() {
        return 60;
    }

    @Override
    public int getBonusSpeed() {
        return 3;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(69, 0, 0, 0, 0, 0);
    }

    @Override
    public String getČeskéJméno() {
        return "Briga";
    }

    @Override
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

}
