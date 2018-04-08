package com.vztekoverflow.lospiratos.viewmodel.transitions;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.transitions.Transition;

import java.util.List;
import java.util.Map;

public interface OnMovementsEvaluatedListener {
    void OnMovementsEvaluated(Map<Ship, List<Transition>> transitions);
}
