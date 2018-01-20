package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics;

import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;

public class Chained extends ShipMechanics {
    @Override
    public String getČeskéJméno() {
        return "Zařetězeno";
    }

    @Override
    public com.vztekoverflow.lospiratos.model.ShipMechanics getModelDescription() {
        return com.vztekoverflow.lospiratos.model.ShipMechanics.chained;
    }
}
