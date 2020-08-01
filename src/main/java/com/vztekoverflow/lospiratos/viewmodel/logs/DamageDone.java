package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;

public class DamageDone extends LoggedEvent {

    @Override
    public String getTextualDescription(LogFormatter f) {
        String s = f.space();
        String targetName = sender.equals(target) ? f.const_Self() : f.format(target);
        return f.format(sender) + s + f.const_AttacksVia() + s + f.format(cause) + s + f.const_AttacksOn() + s + targetName
                + f.const_And() + f.const_AttackCauses() + f.formatDamage(damageCaused);
    }

    private Ship sender;
    private Attack cause;
    private Ship target;
    private int damageCaused;

    DamageDone(Ship sender, Attack cause, Ship target, int damageCaused) {
        this.sender = sender;
        this.cause = cause;
        this.target = target;
        this.damageCaused = damageCaused;
    }

    public Ship getSender() {
        return sender;
    }

    public Attack getCause() {
        return cause;
    }

    public Ship getTarget() {
        return target;
    }

    public int getDamageCaused() {
        return damageCaused;
    }
}
