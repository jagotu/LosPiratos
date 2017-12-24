package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Brig extends ShipType {

    @Override
    public int getBonusMaxHP() {return 45;}
    @Override
    public int getBonusCannons() {return 12;}
    @Override
    public int getBonusCargo() {return 480;}
    @Override
    public int getBonusGarrison() {return 60;}
    @Override
    public int getBonusSpeed() {return 3;}
}
