package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.AxialDirection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Position {

    public static final boolean pointy = true;

    /*
     * Creates new Position with coordinate and rotation 2-way bound to given properties
     */
    public Position(ObjectProperty<AxialCoordinate> coordinate, IntegerProperty rotation) {
        this.coordinate.bindBidirectional(coordinate);
        this.rotation.bindBidirectional(rotation);
    }

    public Position() {
    }

    private ObjectProperty<AxialCoordinate> coordinate = new SimpleObjectProperty<>();
    private IntegerProperty rotation = new SimpleIntegerProperty() {
        @Override
        public void set(int newValue) {
            super.set(((newValue % 360) + 360) % 360); //i.e. always set value \in [0,360)
        }

        @Override
        public void setValue(Number v) {
            super.set((int) v); //not sure whether this is correct, but hope so
        }
    };

    public AxialCoordinate getCoordinate() {
        return coordinate.get();
    }

    public ObjectProperty<AxialCoordinate> coordinateProperty() {
        return coordinate;
    }

    public void setCoordinate(AxialCoordinate coordinate) {
        this.coordinate.set(coordinate);
    }

    public void setCoordinate(int Q, int R) {
        setCoordinate(new AxialCoordinate(Q, R));
    }

    public int getRotation() {
        return rotation.get();
    }

    public AxialDirection getRotationAsDirection() {
        if (pointy) {
            return AxialDirection.DirectionFromDegree_Pointy(getRotation());
        } else {
            return AxialDirection.DirectionFromDegree_Flat(getRotation());
        }
    }

    public IntegerProperty rotationProperty() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation.set(rotation);
    }

    public void setRotation(AxialDirection direction) {
        rotation.set(direction.toDegrees());
    }

    public void rotateRight() {
        rotation.set((rotation.get() + 60));
    }

    public void rotateLeft() {
        setRotation((getRotation() - 60));
    }

    public void moveForwards() { //btw, do we use British or American english in function names? this one is British
        coordinate.set(coordinate.get().plus(getRotationAsDirection()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (Position.class.isAssignableFrom(obj.getClass())) {
            Position p = (Position) obj;
            return p.coordinate.equals(this.coordinate) &&
                    p.getRotationAsDirection().equals(this.getRotationAsDirection());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return coordinate.hashCode() * getRotationAsDirection().hashCode();
    }
}
