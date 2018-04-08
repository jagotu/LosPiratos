package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;

public class Frigate extends ShipType {
    @Override
    public int getBonusMaxHP() {
        return 105;
    }

    @Override
    public int getBonusCannonsCount() {
        return 20;
    }

    @Override
    public int getBonusCargoSpace() {
        return 300;
    }

    @Override
    public int getBonusGarrison() {
        return 100;
    }

    @Override
    public int getBonusSpeed() {
        return 3;
    }

    @Override
    public int getWeight() {
        return BASIC_SHIP_WEIGHT * 3;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(4000,200, 130, 0, 0, 300);
    }

    @Override
    public String getČeskéJméno() {
        return "Fregata";
    }

    @Override
    public ResourceReadOnly getBuyingCost() {
        return getCost();
    }


}

