package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;

public class Galleon extends ShipType {

    @Override
    public int getBonusMaxHP() {
        return 150;
    }

    @Override
    public int getBonusCannonsCount() {
        return 30;
    }

    @Override
    public int getBonusCargoSpace() {
        return 600;
    }

    @Override
    public int getBonusGarrison() {
        return 200;
    }

    @Override
    public int getBonusSpeed() {
        return 2;
    }

    @Override
    public int getWeight() {
        return BASIC_SHIP_WEIGHT * 4;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(8000,250, 250, 0, 0, 400);
    }

    @Override
    public String getČeskéJméno() {
        return "Galeona";
    }

    @Override
    public ResourceReadOnly getBuyingCost() {
        return getCost();
    }

}
