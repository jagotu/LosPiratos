package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Frigate extends ShipType {
    @Override
    public int getBonusMaxHP() {return 70;}
    @Override
    public int getBonusCannonsNr() {return 20;}
    @Override
    public int getBonusCargoSpace() {return 800;}
    @Override
    public int getBonusGarrison() {return 100;}
    @Override
    public int getBonusSpeed() {return 3;}

    public static ResourceImmutable getCost(){
        return new ResourceImmutable();
    }

    @Override
    public String getČeskéJméno() {
        return "Frigata";
    }
}

