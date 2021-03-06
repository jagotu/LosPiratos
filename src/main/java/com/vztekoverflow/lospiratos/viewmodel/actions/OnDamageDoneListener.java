package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.DamageSufferedResponse;
import com.vztekoverflow.lospiratos.viewmodel.Ship;

public interface OnDamageDoneListener {
    void onDamageDone(Attack sender, Ship target, int damageAmount, DamageSufferedResponse response);
}
