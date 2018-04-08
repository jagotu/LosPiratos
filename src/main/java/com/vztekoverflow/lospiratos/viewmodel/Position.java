package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.AxialDirection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A modifiable object, representing a position of a figure on a hexagonal board with axial coordinates
 */
public class Position {

    public static final boolean pointy = true;

    /**
     * Creates new Position with coordinate and rotation 2-way bound to given properties
     */
    public Position(ObjectProperty<AxialCoordinate> coordinate, IntegerProperty rotation) {
        this.coordinate.bindBidirectional(coordinate);
        this.rotation.bindBidirectional(rotation);
    }

    public Position() {
    }

    private ObjectProperty<AxialCoordinate> coordinate = new SimpleObjectProperty<>(AxialDirection.ZERO);
    private IntegerProperty rotation = new SimpleIntegerProperty(0) {
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

    public int getQ() {
        return getCoordinate().getQ();
    }

    public int getR() {
        return getCoordinate().getR();
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
            return AxialDirection.directionFromDegree_Pointy(getRotation());
        } else {
            return AxialDirection.directionFromDegree_Flat(getRotation());
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

    public void setFrom(Position anotherPosition) {
        this.setCoordinate(anotherPosition.getCoordinate());
        this.setRotation(anotherPosition.getRotation());
    }

    public void rotateRight() {
        rotation.set((rotation.get() + 60));
    }

    public void rotateLeft() {
        setRotation((getRotation() - 60));
    }

    public void moveForward() {
        coordinate.set(coordinate.get().plus(getRotationAsDirection()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (AxialCoordinate.class.isAssignableFrom(obj.getClass())) {
            AxialCoordinate c = (AxialCoordinate) obj;
            return this.coordinate.get().equals(c);
        } else if (Position.class.isAssignableFrom(obj.getClass())) {
            Position p = (Position) obj;
            return p.coordinate.get().equals(this.coordinate.get()) &&
                    p.getRotationAsDirection().equals(this.getRotationAsDirection());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return coordinate.hashCode() * getRotationAsDirection().hashCode();
    }

    public Position createCopy() {
        Position result = new Position();
        result.rotation.set(this.rotation.get());
        result.setCoordinate(this.getQ(), this.getR());
        return result;
    }
}
