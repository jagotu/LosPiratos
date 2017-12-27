package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;


import com.vztekoverflow.lospiratos.util.BijectiveMap;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;


public abstract class ShipType extends ShipEntity {

    public static ShipType getInstaceFromString(String type){
        if(type.equals(Brig.class.toString())){
            return  new Brig();
        }
        if(type.equals(Frigate.class.toString())){
            return  new Frigate();
        }
        if(type.equals(Galleon.class.toString())){
            return  new Galleon();
        }
        if(type.equals(Schooner.class.toString())){
            return  new Schooner();
        }
        else throw new IllegalArgumentException("Illegal argument: this is not a known ship type: " + type);
    }
}

