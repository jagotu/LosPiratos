package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.model.ShipwreckM;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Translatable;

public class Shipwreck implements Plunderable, Figure, OnNextRoundStartedListener, Translatable {

    private final Resource resource = new Resource();
    private final AxialCoordinate coordinate;
    private final Game owner;
    private final ShipwreckM model;

    public Game getOwner() {
        return owner;
    }

    @Override
    public AxialCoordinate getCoordinate() {
        return coordinate;
    }

    Shipwreck(AxialCoordinate coordinate, Game owner, ShipwreckM model) {
        this.coordinate = coordinate;
        this.owner = owner;
        owner.addListener(this);
        this.model = model;
        getResource().bindBidirectional(model.resource);
    }

    static Shipwreck loadFrom(ShipwreckM model, Game owner){
        return new Shipwreck(model.position, owner, model);
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
            owner.removeListener(this);
        }
    }

    @Override
    public String getČeskéJméno() {
        return "vrak";
    }
}
