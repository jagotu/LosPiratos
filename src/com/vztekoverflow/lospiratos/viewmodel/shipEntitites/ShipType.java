package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;


public abstract class ShipType extends ShipEntity {

    //static:

    private static String persistentNameSchooner = "Schooner";
    private static String persistentNameBrig = "Brig";
    private static String persistentNameFrigate = "Frigate";
    private static String persistentNameGalleon = "Galleon";


    public static String getPersistentName(Class<? extends ShipType> shipType) {
        if (shipType.equals(Schooner.class)) {
            return persistentNameSchooner;
        }
        if (shipType.equals(Brig.class)) {
            return persistentNameBrig;
        }
        if (shipType.equals(Frigate.class)) {
            return persistentNameFrigate;
        }
        if (shipType.equals(Galleon.class)) {
            return persistentNameGalleon;
        }
        //else
        Warnings.panic("ShipEntity.getPersistentName()", "Unknown ship type class: " + shipType.getCanonicalName());
        return "UnknownShipType";
    }

    /*
     * @returns null if the @shipTypeName is unknown
     */
    public static ShipType createInstanceFromPersistentName(String shipTypeName) {
        if (shipTypeName == null || shipTypeName.isEmpty()) {
            Warnings.makeWarning("ShipType.createInstanceFromPersistentName()", "Empty or null type name: " + shipTypeName);
            return null;
        }
        if (shipTypeName.equalsIgnoreCase(persistentNameSchooner)) {
            return new Schooner();
        }
        if (shipTypeName.equalsIgnoreCase(persistentNameBrig) || shipTypeName.equalsIgnoreCase("Brig")) {
            return new Brig();
        }
        if (shipTypeName.equalsIgnoreCase(persistentNameFrigate)) {
            return new Frigate();
        }
        if (shipTypeName.equalsIgnoreCase(persistentNameGalleon)) {
            return new Galleon();
        }
        //else
        Warnings.makeWarning("ShipType.createInstanceFromPersistentName()", "Unknown ship type: " + shipTypeName);
        return null;

    }
}

