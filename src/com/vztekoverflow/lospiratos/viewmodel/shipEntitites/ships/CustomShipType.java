package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class CustomShipType extends ShipType {
    @Override
    public String getČeskéJméno() {
        return "uživatelský typ lodě";
    }

    @Override
    public ResourceReadOnly getBuyingCost() {
        return getCost();
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly();
    }

    @Override
    public int getWeight() {
        return ShipType.BASIC_SHIP_WEIGHT * 5;
        //todo jak tezka je custom lod?
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
