package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.Plunderable;

public interface OnPlunderRequestedListener {
    void onPlunderRequested(Plunderable target, Plunder sender);
}
