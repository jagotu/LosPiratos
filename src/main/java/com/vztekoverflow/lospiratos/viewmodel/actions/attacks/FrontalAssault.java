package com.vztekoverflow.lospiratos.viewmodel.actions.attacks;

import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.Ram;

import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.FRONTAL_ASSAULT_BASIC_DAMAGE;
import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.FRONTAL_ASSAULT_BASIC_SELF_DAMAGE;
import static com.vztekoverflow.lospiratos.viewmodel.GameConstants.FRONTAL_ASSAULT_SPEED_BONUS_DAMAGE;

public class FrontalAssault extends Attack {

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    protected boolean recomputePlannable() {
        return shipHasPlannedLessThan(1, FrontalAssault.class);
    }

    @Override
    public void performOnShipInternal() {
        int damage = FRONTAL_ASSAULT_BASIC_DAMAGE;
        int selfDamage = FRONTAL_ASSAULT_BASIC_SELF_DAMAGE;
        Position p = getRelatedShip().getPosition().createCopy();
        p.moveForward();

        Ship target = getRelatedShip().getTeam().getGame().getBoard().getShip(p.getCoordinate());
        if (target == null) return;
        //if target is facing me:
        if (target.getPosition().getRotation() == (getRelatedShip().getPosition().getRotation() + 180) % 360) {
            return; //the rules state that if target is facing me, nothing happens
            //todo maybe log this event?
        }

        //if action played just before was move forward, add dmg:
        Action lastAction = null;
        for (Action a : getRelatedShip().getPlannedActions()) {
            if (a.equals(this)) break;
            lastAction = a;
        }
        if (lastAction instanceof MoveForward) {
            damage += FRONTAL_ASSAULT_SPEED_BONUS_DAMAGE;
        }

        if (getRelatedShip().hasActiveEnhancement(Ram.class)) {
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
    protected Action createCopyAndResetThis() {
        return new FrontalAssault();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.ram;
    }

}
