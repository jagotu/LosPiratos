package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.BASIC_SHIP_WEIGHT;

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
        return BASIC_SHIP_WEIGHT * 5;
        //todo jak tezka je custom lod?
        //custom lode neexistuji
        //nnezlob se na mne, ale na ne
        //ja za nic nemuzu
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
