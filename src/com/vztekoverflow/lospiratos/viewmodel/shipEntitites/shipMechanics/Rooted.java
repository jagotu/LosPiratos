package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics;

import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;

public class Rooted extends ShipMechanics {
    @Override
    public String getČeskéJméno() {
        return "Zarootováno";
    }

    @Override
    public com.vztekoverflow.lospiratos.model.ShipMechanics getModelDescription() {
        return com.vztekoverflow.lospiratos.model.ShipMechanics.rooted;
    }
}
