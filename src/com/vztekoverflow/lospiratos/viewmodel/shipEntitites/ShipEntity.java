package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.util.Translatable;
import com.vztekoverflow.lospiratos.viewmodel.Ship;

public abstract class ShipEntity implements Translatable {

    //this is just a proof of concept and should be later specialized into more specific classes:


    protected ShipEntity() {

    }
    protected Ship ship;

    /**
     * Should be called just after the entity has been added to the ship, before any other function is called.
     */
    public final void onAddedToShip(Ship ship){
        this.ship = ship;
        onAddedToShipInternal();
    }
    /**
     * Can be inherited to add custom implementation.
     * Overridden implementations should always call super.onAddedToShipInternal() first.
     */
    protected void onAddedToShipInternal(){}

    /**
     * Should be called whenever the ship type changes.
     * Overridden implementations should always call super.onShipTypeJustChanged() first.
     */
    public void onShipTypeJustChanged(){}

    /**
     * Is called by the owner whenever the game proceeds to a next round.
     * Can be inherited to add custom implementation.
     * Overridden implementations should always call super.onNextRoundStarted() first.
     */
    public void onNextRoundStarted(int roundNo) {
    }

    public int getBonusCannonsCount() {return 0;}
    public int getBonusMaxHP() {return 0;}
    public int getBonusCargoSpace() {return 0;}
    public int getBonusGarrison() {return 0;}
    public int getBonusSpeed() {return 0;}

}
