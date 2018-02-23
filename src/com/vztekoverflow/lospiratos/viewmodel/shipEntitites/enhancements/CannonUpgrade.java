package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

public final class CannonUpgrade extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon() {
        return EnhancementIcon.cannon;
    }

    private int bonusCannons = 0;

    private void recomputeBonusCannons() {
        if (ship.getShipType() instanceof Schooner) {
            bonusCannons = 2;
        } else if (ship.getShipType() instanceof Brig) {
            bonusCannons = 4;
        } else if (ship.getShipType() instanceof Frigate) {
            bonusCannons = 6;
        } else if (ship.getShipType() instanceof Galleon) {
            bonusCannons = 8;
        } else {
            Warnings.makeWarning("CannonUpgrade:", "Unknown ship type: " + ship.getShipType());
        }
    }

    @Override
    protected void onAddedToShipInternal() {
        super.onAddedToShipInternal();
        recomputeBonusCannons();
    }

    @Override
    public void onShipTypeJustChanged() {
        super.onShipTypeJustChanged();
        recomputeBonusCannons();
    }

    @Override
    public int getBonusCannonsCount() {
        if (this.isDestroyed()) return 0;
        return bonusCannons;
    }

    /**
     * Returns universal cost of this enhancement that is same for all instances, by calling a static method getCost().
     */
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }


    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(50,0,50,0,0,50);
    }

    @Override
    public String getČeskéJméno() {
        return "Přídavná děla";
    }
}
