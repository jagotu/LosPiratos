package com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;


public class Schooner extends ShipType {
    @Override
    public int getBonusMaxHP() {return 30;}
    @Override
    public int getBonusCannons() {return 8;}
    @Override
    public int getBonusCargo() {return 250;}
    @Override
    public int getBonusGarrison() {return 30;}
    @Override
    public int getBonusSpeed() {return 4;}
}
