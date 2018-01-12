package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public abstract class ShipEnhancement extends ShipEntity {

    private BooleanProperty destroyed = new SimpleBooleanProperty(false);

    public boolean isDestroyed() {
        return destroyed.getValue();
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed.set(destroyed);
    }

    public BooleanProperty destroyedProperty() {
        return destroyed;
    }

    //static:

    private static String persistentNameCannonUpgrade = "CannonUpgrade";
    private static String persistentNameHullUpgrade = "HullUpgrade";
    private static String persistentNameMortar = "Mortar";
    private static String persistentNameRam = "Ram";
    private static String persistentNameChainShot = "ChainShot";
    private static String persistentNameHeavyShot = "HeavyShot";

    public static String getPersistentName(Class<? extends ShipEnhancement> enhancementType) {
        if (enhancementType.equals(CannonUpgrade.class)) {
            return persistentNameCannonUpgrade;
        }
        if (enhancementType.equals(HullUpgrade.class)) {
            return persistentNameHullUpgrade;
        }
        if (enhancementType.equals(Mortar.class)) {
            return persistentNameMortar;
        }
        if (enhancementType.equals(Ram.class)) {
            return persistentNameRam;
        }
        if (enhancementType.equals(ChainShot.class)) {
            return persistentNameChainShot;
        }
        if (enhancementType.equals(HeavyShot.class)) {
            return persistentNameHeavyShot;
        }
        //else
        Warnings.panic("ShipEnhancement.getPersistentName()", "Unknown enhancement class: " + enhancementType.getCanonicalName());
        return "UnknownShipEnhancement";
    }

    /*
     * @returns null if the @enhancementName is unknown
     */
    public static ShipEnhancement createInstanceFromPersistentName(String enhancementName) {
        if (enhancementName == null || enhancementName.isEmpty()) {
            Warnings.makeWarning("ShipEnhancement.createInstanceFromPersistentName()", "Empty or null enhancement name: " + enhancementName);
            return null;
        }
        if (enhancementName.equalsIgnoreCase(persistentNameCannonUpgrade)) {
            return new CannonUpgrade();
        }
        if (enhancementName.equalsIgnoreCase(persistentNameHullUpgrade)) {
            return new HullUpgrade();
        }
        if (enhancementName.equalsIgnoreCase(persistentNameMortar)) {
            return new Mortar();
        }
        if (enhancementName.equalsIgnoreCase(persistentNameRam)) {
            return new Ram();
        }
        if (enhancementName.equalsIgnoreCase(persistentNameChainShot)) {
            return new ChainShot();
        }
        if (enhancementName.equalsIgnoreCase(persistentNameHeavyShot)) {
            return new HeavyShot();
        }
        //else
        Warnings.makeWarning("ShipEnhancement.createInstanceFromPersistentName()", "Unknown enhancement: " + enhancementName);
        return null;

    }
}

