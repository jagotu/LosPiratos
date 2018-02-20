package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;


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

    @Override
    public int getWeight() {
        return BASIC_SHIP_WEIGHT;
    }


    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly();
    }

    @Override
    public String getČeskéJméno() {
        return "Škuner";
    }

    @Override
    public ResourceReadOnly getBuyingCost() {
        return getCost();
    }

    @Override
    public ResourceReadOnly getBasicRepairCost() {
        return ResourceReadOnly.MOCK_VALUE;
    }

    @Override
    public ResourceReadOnly getUpgradeCost() {
        return ResourceReadOnly.MOCK_VALUE;
    }

}
