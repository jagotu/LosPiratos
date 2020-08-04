package com.vztekoverflow.lospiratos.viewmodel.shipEntitites;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;

import java.util.Arrays;
import java.util.List;


public abstract class ShipType extends ShipEntity {

    /**
     * Returns cost of buying new ship with this type. The value should be same for all instances.
     */
    abstract public ResourceReadOnly getBuyingCost();

    /**
     * Returns cost of repairing fully damaged ship with this type. The value should be same for all instances.
     */
    public ResourceReadOnly getBasicRepairCost(){
        return getBuyingCost().times(1/5f);
    }

    /**
     * Returns cost of upgrading to this ship type. The value should be same for all instances.
     */
    public ResourceReadOnly getUpgradeCost(){
        Class<? extends ShipType> shipClass = increment(getClass());
        if(shipClass == null) return  ResourceReadOnly.MAX;
        ShipType instance = createInstance(increment(getClass()));
        if(instance == null) return  ResourceReadOnly.MAX;
        return instance.getBuyingCost().times(1/2f);
    }

    public static List<Class<? extends ShipType>> allShipTypes = Arrays.asList(Schooner.class, Brig.class, Frigate.class, Galleon.class);

    abstract public int getWeight();

    //static:

    private static String persistentNameSchooner = "Schooner";
    private static String persistentNameBrig = "Brig";
    private static String persistentNameFrigate = "Frigate";
    private static String persistentNameGalleon = "Galleon";


    public static String getPersistentName(Class<? extends ShipType> shipType) {
        if(shipType == null) return null;
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

    /**
     * @return null if the @shipTypeName is unknown
     */
    public static ShipType createInstanceFromPersistentName(String shipTypeName) {
        if (shipTypeName == null || shipTypeName.isEmpty()) {
            Warnings.makeWarning("ShipType.createInstanceFromPersistentName()", "Empty or null type name: " + shipTypeName);
            return null;
        }
        if(shipTypeName.equals("null")) return null;
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

    public static ShipType createInstance(Class<? extends ShipType> type) {
        return createInstanceFromPersistentName(ShipType.getPersistentName(type));
    }

    /**
     * @return a ship type that is one order better than {@code shipTypeToIncrement}
     * null if {@code shipTypeToIncrement} cannot be incremented
     */
    public static Class<? extends ShipType> increment(Class<? extends ShipType> shipTypeToIncrement) {
        if (shipTypeToIncrement.equals(Schooner.class))
            return Brig.class;
        if (shipTypeToIncrement.equals(Brig.class))
            return Frigate.class;
        if (shipTypeToIncrement.equals(Frigate.class))
            return Galleon.class;
        //else
        return null;
    }

    /**
     * @return a ship type that is one order worse than {@code shipTypeToDecrement}
     * null if {@code shipTypeToDecrement} cannot be decremented
     */
    public static Class<? extends ShipType> decrement(Class<? extends ShipType> shipTypeToDecrement) {
        if (shipTypeToDecrement.equals(Brig.class))
            return Schooner.class;
        if (shipTypeToDecrement.equals(Frigate.class))
            return Brig.class;
        if (shipTypeToDecrement.equals(Galleon.class))
            return Frigate.class;
        //else
        return null;
    }

}

