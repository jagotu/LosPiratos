package com.vztekoverflow.lospiratos.evaluator;

import com.vztekoverflow.lospiratos.viewmodel.DamageSufferedResponse;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;

public interface OnDamageDoneListener {
    void onDamageDone(Attack sender, Ship target, int damageAmount, DamageSufferedResponse response);
}
