package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.viewmodel.Ship;

public class ShipDied extends LoggedEvent {

    @Override
    public String getTextualDescription(LogFormatter f) {
        String s = f.space();
        StringBuilder attackers  = new StringBuilder();
        boolean firstGoThrough = true;
        for(Ship a: this.attackers){
            if(!firstGoThrough)
                attackers.append(f.const_And());
            attackers.append(f.format(a));
            firstGoThrough = false;
        }

        return f.format(deadShip) + s + f.const_Dies() +s
                + f.const_ThanksTo() + s + "{" + attackers.toString() + "}";
    }

    Ship deadShip;
    Iterable<Ship> attackers;

    public ShipDied(Ship deadShip, Iterable<Ship> attackers) {
        this.deadShip = deadShip;
        this.attackers = attackers;
    }

    public Ship getDeadShip() {
        return deadShip;
    }

    public Iterable<Ship> getAttackers() {
        return attackers;
    }
}
