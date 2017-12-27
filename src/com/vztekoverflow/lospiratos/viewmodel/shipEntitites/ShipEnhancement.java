package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.CannonUpgrade;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.HullUpgrade;

public class ShipEnhancement extends ShipEntity {

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
    private boolean destroyed = false;


    public static ShipEnhancement getInstaceFromString(String type){
        if(type.equals(CannonUpgrade.class.toString())){
            return  new CannonUpgrade();
        }
        if(type.equals(HullUpgrade.class.toString())){
            return  new HullUpgrade();
        }
        else throw new IllegalArgumentException("Illegal argument: this is not a known ship enhancement type: " + type);
    }
}

//ehancements needed:
    // upgCanon
    // upgHull
    //enhMortar
    //chain shot
    //heavy shot
    //enh ram
