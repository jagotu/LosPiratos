package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class Galleon extends ShipType {

    @Override
    public int getBonusMaxHP() {return 100;}
    @Override
    public int getBonusCannonsNr() {return 30;}
    @Override
    public int getBonusCargoSpace() {return 1200;}
    @Override
    public int getBonusGarrison() {return 200;}
    @Override
    public int getBonusSpeed() {return 2;}
}
