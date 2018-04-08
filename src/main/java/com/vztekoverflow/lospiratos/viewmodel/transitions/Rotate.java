package com.vztekoverflow.lospiratos.viewmodel.transitions;

public class Rotate extends Transition {
    private int rotation;

    public Rotate(int rotation) {
        this.rotation = rotation;
    }

    public int getRotation() {
        return rotation;
    }
}
