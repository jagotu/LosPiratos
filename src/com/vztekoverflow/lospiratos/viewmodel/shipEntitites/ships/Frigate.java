package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;

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

    @Override
    public int getWeight() {
        return BASIC_SHIP_WEIGHT * 3;
    }

    public static ResourceReadOnly getCost() {
        return ResourceReadOnly.MOCK_VALUE;
    }

    @Override
    public String getČeskéJméno() {
        return "Frigata";
    }

    @Override
    public ResourceReadOnly getBuyingCost() {
        return getCost();
    }

    @Override
    public ResourceReadOnly getBasicRepairCost() {
        return ResourceReadOnly.MOCK_VALUE.times(1/10);
    }

    @Override
    public ResourceReadOnly getUpgradeCost() {
        return ResourceReadOnly.MOCK_VALUE.times(1/2);
    }

}

