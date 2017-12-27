package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

public class CannonUpgrade extends ShipEnhancement {

    private int bonusCannons = 0;

    private void recomupteBonusCannons(){
        if(ship.getShipType() instanceof Schooner){
            bonusCannons = 2;
        }
        else if(ship.getShipType() instanceof Brig){
            bonusCannons = 3;
        }
        else if(ship.getShipType() instanceof Frigate){
            bonusCannons = 5;
        }
        else if(ship.getShipType() instanceof Galleon){
            bonusCannons = 7;
        }
        else{
            Warnings.makeWarning("CannonUpgrade:","Unknown ship type: " + ship.getShipType());
        }
    }

    @Override
    protected void onAddedToShipInternal(){
        super.onAddedToShipInternal();
        recomupteBonusCannons();
    }

    @Override
    public void onShipTypeJustChanged(){
        super.onShipTypeJustChanged();
        recomupteBonusCannons();
    }

    @Override
    public int getBonusCannons() {
        if(this.isDestroyed()) return 0;
        return bonusCannons;
    }
}