package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Brig extends ShipType {

    @Override
    public int getBonusMaxHP() {return 45;}
    @Override
    public int getBonusCannonsNr() {return 12;}
    @Override
    public int getBonusCargoSpace() {return 480;}
    @Override
    public int getBonusGarrison() {return 60;}
    @Override
    public int getBonusSpeed() {return 3;}

    public static ResourceImmutable getCost(){
        return new ResourceImmutable();
    }

    @Override
    public String getČeskéJméno() {
        return "Briga";
    }
}
