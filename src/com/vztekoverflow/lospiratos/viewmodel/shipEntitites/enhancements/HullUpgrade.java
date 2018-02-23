package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

public final class HullUpgrade extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon() {
        return EnhancementIcon.hull;
    }

    @Override
    public int getBonusMaxHP() {
        if (this.isDestroyed()) return 0;
        if (ship.getShipType() instanceof Schooner) {
            return 10;
        } else if (ship.getShipType() instanceof Brig) {
            return 20;
        } else if (ship.getShipType() instanceof Frigate) {
            return 30;
        } else if (ship.getShipType() instanceof Galleon) {
            return 40;
        } else {
            Warnings.makeWarning("CannonUpgrade", " Unknown ship type: " + ship.getShipType());
            return 0;
        }
    }

    /**
     * Returns universal cost of this enhancement that is same for all instances, by calling a static method getCost().
     */
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }


    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(50,0,10,0,0,100);
    }

    @Override
    public String getČeskéJméno() {
        return "Vyztužení trupu";
    }
}
