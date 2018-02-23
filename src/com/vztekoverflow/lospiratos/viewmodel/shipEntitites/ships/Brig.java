package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;

public class Brig extends ShipType {

    @Override
    public int getBonusMaxHP() {
        return 90;
    }

    @Override
    public int getBonusCannonsCount() {
        return 12;
    }

    @Override
    public int getBonusCargoSpace() {
        return 200;
    }

    @Override
    public int getBonusGarrison() {
        return 60;
    }

    @Override
    public int getBonusSpeed() {
        return 3;
    }

    @Override
    public int getWeight() {
        return BASIC_SHIP_WEIGHT * 2;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(200, 150, 0, 0, 0, 200);
    }

    @Override
    public String getČeskéJméno() {
        return "Briga";
    }

    @Override
    public ResourceReadOnly getBuyingCost() {
        return getCost();
    }

}
