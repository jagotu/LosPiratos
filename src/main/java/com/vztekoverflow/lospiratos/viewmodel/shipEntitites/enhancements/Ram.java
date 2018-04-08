package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.FRONTAL_ASSAULT_BONUS_DAMAGE_FRIGATE;
import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.FRONTAL_ASSAULT_BONUS_DAMAGE_GALLEON;

public final class Ram extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon() {
        return EnhancementIcon.ram;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(250,0,10,0,0,75);
    }

    public String getČeskéJméno() {
        return "Kloun";
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
            ship.removeEnhancement(this.getClass());
        } else if (ship.getShipType() instanceof Frigate) {
            frontalAssaultBonusDamage = FRONTAL_ASSAULT_BONUS_DAMAGE_FRIGATE;
        } else if (ship.getShipType() instanceof Galleon) {
            frontalAssaultBonusDamage = FRONTAL_ASSAULT_BONUS_DAMAGE_GALLEON;
        } else {
            Warnings.makeStrongWarning(toString() + "onShipTypeJustChanged()", "unknown ship type: " + ship.getShipType());
        }
    }

    @Override
    protected void onAddedToShipInternal() {
        super.onAddedToShipInternal();
        recompute();
    }



    private int frontalAssaultBonusDamage = 0;

    public int getFrontalAssaultBonusDamage() {
        return frontalAssaultBonusDamage;
    }

    @Override
    public boolean isAcquirableBy(ShipType type) {
        if (type instanceof Schooner || type instanceof Brig)
            return false;
        return true;
    }

}
