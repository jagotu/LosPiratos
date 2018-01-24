package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements;

import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;

import java.util.ArrayList;
import java.util.List;

public class EnhancementsCatalog {
    public final static Iterable<Class<? extends ShipEnhancement>> allPossibleEnhancements;

    static
    {
        List<Class<? extends ShipEnhancement>> l = new ArrayList<>();
        l.add(CannonUpgrade.class);
        l.add(ChainShot.class);
        l.add(HeavyShot.class);
        l.add(HullUpgrade.class);
        l.add(Mortar.class);
        l.add(Ram.class);
        allPossibleEnhancements = l;
    }

}
