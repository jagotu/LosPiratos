package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.util.Translatable;
import com.vztekoverflow.lospiratos.viewmodel.Ship;

public interface PerformableAction extends Translatable {
    void performOnShip();

    void setRelatedShip(Ship s);
}
