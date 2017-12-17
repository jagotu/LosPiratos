package com.vztekoverflow.lospiratos.model;

public enum ShipMechanics {
    empty,
    chained, //cannot move in the next round because it has been chained
    rooted //the ship is rooted and cannot move for next two rounds
}
