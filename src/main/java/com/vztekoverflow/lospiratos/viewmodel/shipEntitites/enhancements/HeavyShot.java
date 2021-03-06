package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;

public final class HeavyShot extends ShipEnhancement {

    @Override
    public EnhancementIcon getIcon() {
        return EnhancementIcon.heavyBall;
    }

    public static ResourceReadOnly getCost() {
        return new ResourceReadOnly(1000,0,75,0,0,0);
    }

    @Override
    public String getČeskéJméno() {
        return "Těžká kule";
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
        if (!(ship.getShipType() instanceof Galleon)) {
            ship.removeEnhancement(this.getClass());
        }
    }

    @Override
    public boolean isAcquirableBy(ShipType type) {
        return type instanceof Galleon;
    }
}
