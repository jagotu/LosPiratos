package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Ship;

import java.util.Map;

public class CollisionSolved extends LoggedEvent {
    private AxialCoordinate collisionPosition;
    private Map<Ship, AxialCoordinate> newPositions;
    private int iteration;
    @Override
    public String getTextualDescription(LogFormatter f) {
        String s = f.space();
        String rightArrow = " \uD83E\uDC72 ";
        StringBuilder changes  = new StringBuilder();
        boolean firstGoThrough = true;
        for(Map.Entry<Ship, AxialCoordinate> e: this.newPositions.entrySet()){
            if(!firstGoThrough)
                changes.append(f.const_And()).append(f.newLine());
            changes.append(f.format(e.getKey()));
            changes.append(rightArrow);
            changes.append(f.format(e.getValue()));
            firstGoThrough = false;
        }

        return f.const_CollisionOn() + s + f.format(collisionPosition) +
                "("+ f.formatIteration(iteration+1) + "):" + f.newLine() +
                "{" + changes.toString() + "}";

    }

    public CollisionSolved(AxialCoordinate collisionPosition, Map<Ship, AxialCoordinate> newPositions, int iteration) {
        this.collisionPosition = collisionPosition;
        this.newPositions = newPositions;
        this.iteration = iteration;
    }

    public AxialCoordinate getCollisionPosition() {
        return collisionPosition;
    }

    public Map<Ship, AxialCoordinate> getNewPositions() {
        return newPositions;
    }

    public int getIteration() {
        return iteration;
    }
}
