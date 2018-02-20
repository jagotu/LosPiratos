package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;

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

    @Override
    public int getWeight() {
        return BASIC_SHIP_WEIGHT * 4;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(42, 0, 0, 0, 0, 0);
    }

    @Override
    public String getČeskéJméno() {
        return "Galeona";
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
