package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

public final class HullUpgrade extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon(){return EnhancementIcon.hull;}

    @Override
    public int getBonusMaxHP() {
        if(this.isDestroyed()) return 0;
        if(ship.getShipType() instanceof Schooner){
            return 5;
        }
        else if(ship.getShipType() instanceof Brig){
            return 10;
        }
        else if(ship.getShipType() instanceof Frigate){
            return 15;
        }
        else if(ship.getShipType() instanceof Galleon){
            return 20;
        }
        else{
            Warnings.makeWarning("CannonUpgrade", " Unknown ship type: " + ship.getShipType());
            return 0;
        }
    }


    public static ResourceImmutable getCost(){
        return new ResourceImmutable();
    }
    @Override
    public String getČeskéJméno() {
        return "Vyztužení trupu";
    }
}
