package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.util.BijectiveMap;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

import java.util.ArrayList;
import java.util.List;

/**
 * static class providing API for accesing available ship enhancements
 */
public class EnhancementsCatalog {
    public final static Iterable<Class<? extends ShipEnhancement>> allPossibleEnhancements;
    private final static BijectiveMap<Class<? extends ShipEnhancement>, EnhancementIcon> icons = new BijectiveMap<>();

    static {
        List<Class<? extends ShipEnhancement>> l = new ArrayList<>();
        l.add(CannonUpgrade.class);
        l.add(ChainShot.class);
        l.add(HeavyShot.class);
        l.add(HullUpgrade.class);
        l.add(Mortar.class);
        l.add(Ram.class);
        allPossibleEnhancements = l;

        icons.put(CannonUpgrade.class, new CannonUpgrade().getIcon());
        icons.put(ChainShot.class, new ChainShot().getIcon());
        icons.put(HeavyShot.class, new HeavyShot().getIcon());
        icons.put(HullUpgrade.class, new HullUpgrade().getIcon());
        icons.put(Mortar.class, new Mortar().getIcon());
        icons.put(Ram.class, new Ram().getIcon());
    }

    public static EnhancementIcon getIcon(Class<? extends ShipEnhancement> enhancementType) {
        if (icons.containsKey(enhancementType)) {
            return icons.getValue(enhancementType);
        }
        //else
        Warnings.panic("ShipEnhancement.getIcon()", "Unknown enhancement class: " + enhancementType.getCanonicalName());
        return null;
    }

    public static Class<? extends ShipEnhancement> getEnhancementFromIcon(EnhancementIcon icon) {
        if (icons.containsValue(icon)) {
            return icons.getKey(icon);
        }
        //else
        Warnings.panic("ShipEnhancement.getEnhancementFromIcon()", "Unknown enhancement icon: " + icon.toString());
        return null;
    }

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

    /**
     * @return null if the @enhancementName is unknown
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

    public static boolean isAcquirableBy(Class<? extends ShipEnhancement> enh, Class<? extends ShipType> type){
        return true; //todo
    }

    public static boolean isAcquirableBy(Class<? extends ShipEnhancement> enh, ShipType type){
        return isAcquirableBy(enh, type.getClass());
    }
}
