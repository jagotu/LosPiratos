package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.Plunderable;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.Plunder;

public interface OnPlunderRequestedListener {
    void onPlunderRequested(Plunderable target, Plunder sender);
}
