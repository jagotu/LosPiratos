package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

public final class Mortar extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon() {
        return EnhancementIcon.mortar;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(750,0,50,0,0,0);
    }

    public String getČeskéJméno() {
        return "Houfnice";
    }

    /**
     * Returns universal cost of this enhancement that is same for all instances, by calling a static method getCost().
     */
    public ResourceReadOnly getCostUniversal() {
        return getCost();
    }

    @Override
    public void onShipTypeJustChanged() {
        super.onShipTypeJustChanged();
        recompute();
    }

    private void recompute() {
        if (ship.getShipType() instanceof Schooner) {
            ship.removeEnhancement(this.getClass());
        } else if (ship.getShipType() instanceof Brig) {
            mortarsCount = 1;
            range = 2;
        } else if (ship.getShipType() instanceof Frigate) {
            mortarsCount = 2;
            range = 2;
        } else if (ship.getShipType() instanceof Galleon) {
            mortarsCount = 4;
            range = 3;
        } else {
            Warnings.makeStrongWarning(toString() + "onShipTypeJustChanged()", "unknown ship type: " + ship.getShipType());
        }
    }

    @Override
    protected void onAddedToShipInternal() {
        super.onAddedToShipInternal();
        recompute();
    }

    private int mortarsCount = 0;
    private int range = 0;

    public int getMortarsCount() {
        return mortarsCount;
    }

    public int getRange() {
        return range;
    }

    @Override
    public boolean isAcquirableBy(ShipType type) {
        if (type instanceof Schooner)
            return false;
        return true;
    }
}
