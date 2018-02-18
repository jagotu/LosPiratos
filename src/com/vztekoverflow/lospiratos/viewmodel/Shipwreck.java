package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;

public class Shipwreck implements Plunderable, MovableFigure, OnNextRoundStartedListener {

    private final Resource resource = new Resource();
    private final Position position = new Position();
    private final Game owner;

    public Game getOwner() {
        return owner;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    Shipwreck(AxialCoordinate position, Game owner) {
        this.position.setCoordinate(position);
        this.owner = owner;
        owner.addOnNextRoundStartedListener(this);
    }

    /**
     * @return final object representing the resource hold by this board. The returned value is always the same.
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public void onNextRoundStarted(int roundNo) {
        if (getResource().isLesserThanOrEqual(ResourceReadOnly.ZERO)) {
            owner.remove(this);
            owner.removeOnNextRoundStartedListener(this);
        }
    }
}
