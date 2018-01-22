package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.util.Translatable;
import com.vztekoverflow.lospiratos.viewmodel.Ship;

public interface PerformableAction extends Translatable {
    void performOnTarget();
    void setRelatedShip(Ship s);
}
