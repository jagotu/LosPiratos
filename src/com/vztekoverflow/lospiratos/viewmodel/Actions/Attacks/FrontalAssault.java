package com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.Ram;

public class FrontalAssault extends Attack {

    public static final int FrontalAssaultBasicDamage = 10;
    public static final int FrontalAssaultSpeedBonusDamage = 5;
    public static final int FrontalAssaultBasicSelfDamage = 5;

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    protected boolean recomputePlannable() {
        return shipHasPlannedLessThan(1, FrontalAssault.class);
    }

    @Override
    public void performOnTargetInternal() {
        int damage = FrontalAssaultBasicDamage;
        int selfDamage = FrontalAssaultBasicSelfDamage;
        Position p = getRelatedShip().getPosition().createCopy();
        p.moveForward();

        Ship target = getRelatedShip().getTeam().getGame().getBoard().getShip(p.getCoordinate());
        if(target == null) return;
        //if target is facing me:
        if(target.getPosition().getRotation() == (getRelatedShip().getPosition().getRotation() + 180) % 360){
            return; //the rules state that if target is facing me, nothing happens
            //todo maybe log this event?
        }

        //if action played just before was move forward, add dmg:
        Action lastAction = null;
        for(Action a : getRelatedShip().getPlannedActions()){
            if(a.equals(this)) break;
            lastAction = a;
        }
        if(lastAction instanceof MoveForward){
            damage+=FrontalAssaultSpeedBonusDamage;
        }

        if(getRelatedShip().hasActiveEnhancement(Ram.class)){
            selfDamage = 0;
            damage += getRelatedShip().getEnhancement(Ram.class).getFrontalAssaultBonusDamage();
        }

        applyDamageTo(damage, target.getPosition().getCoordinate());
        getRelatedShip().takeDamage(selfDamage);
    }

    @Override
    public String getČeskéJméno() {
        return "čelní útok";
    }

    @Override
    protected Action createCopy(){
        return new FrontalAssault();
    }

}
