package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.controls.figures.ShipFigure;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;
import com.vztekoverflow.lospiratos.viewmodel.MovableFigure;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;

public class PiratosHexTileContentsFactory implements HexTileContentsFactory {

    private ObjectProperty<AxialCoordinateActionParameter> axialSelector;
    private HashMap<AxialCoordinate, PiratosHexTileContents> current = new HashMap<>();
    private double tileWidth, tileHeight;
    private OnClickEventHandler onMouseClick;
    private ObservableList<AxialCoordinate> highlightedCoordinates;

    private ChangeListener<? super AxialCoordinate> highlightedMoveListener = (observable, oldValue, newValue) -> {
        if (oldValue != null && current.get(oldValue) != null) {
            current.get(oldValue).setSelected(false);
        }
        if (newValue != null && current.get(newValue) != null) {
            current.get(newValue).setSelected(true);
        }
    };

    public PiratosHexTileContentsFactory(Board board, double edgeLength, boolean pointy, OnClickEventHandler onMouseClick, ObjectProperty<AxialCoordinateActionParameter> axialSelector, ObservableList<AxialCoordinate> highlightedCoordinates) {
        this.onMouseClick = onMouseClick;
        this.edgeLength = edgeLength;
        this.pointy = pointy;
        this.axialSelector = axialSelector;
        this.highlightedCoordinates = highlightedCoordinates;

        ActionsCatalog.relatedShip.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getPosition().coordinateProperty().removeListener(highlightedMoveListener);
                if (current.get(oldValue.getPosition().coordinateProperty().get()) != null) {
                    current.get(oldValue.getPosition().coordinateProperty().get()).setSelected(false);
                }
            }
            if (newValue != null) {
                newValue.getPosition().coordinateProperty().addListener(highlightedMoveListener);
                if (current.get(newValue.getPosition().coordinateProperty().get()) != null) {
                    current.get(newValue.getPosition().coordinateProperty().get()).setSelected(true);
                }
            }
        });

        for (AxialCoordinate coords : board.getTiles().keySet()) {
            if (current.containsKey(coords)) {
                throw new UnsupportedOperationException("Cannot have the same tile defined twice!");
            }
            PiratosHexTileContents sc = new PiratosHexTileContents(board.getTiles().get(coords));
            //sc.tonikuvHack(coords);
            current.put(coords, sc);
        }
        for (MovableFigure mf : board.getFigures()) {
            addFigure(mf);
        }
        board.figuresProperty().addListener((ListChangeListener<MovableFigure>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (MovableFigure addedItem : c.getAddedSubList()) {
                        addFigure(addedItem);
                    }
                } else if (c.wasRemoved()) {
                    for (MovableFigure removedItem : c.getRemoved()) {
                        removeFigure(removedItem);
                    }
                }
            }
        });

    }

    public PiratosHexTileContentsFactory(Board board, double edgeLength, boolean pointy) {
        this(board, edgeLength, pointy, null, null, null);
    }

    private boolean pointy;
    private double edgeLength;


    private void addFigure(MovableFigure f) {
        if (!current.containsKey(f.getPosition().getCoordinate())) {
            throw new UnsupportedOperationException("Figure out of bounds or not on a tile!");
        }
        current.get(f.getPosition().getCoordinate()).addFigure(f);
        f.getPosition().coordinateProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!current.containsKey(oldValue) || !current.containsKey(newValue)) {
                throw new UnsupportedOperationException("Figure out of bounds or not on a tile!");
            }

            current.get(newValue).addFigure(f, current.get(oldValue).removeFigure(f));
        });
    }

    private void removeFigure(MovableFigure f) {
        if (!current.containsKey(f.getPosition().getCoordinate())) {
            throw new UnsupportedOperationException("Figure out of bounds or not on a tile!");
        }

        current.get(f.getPosition().getCoordinate()).removeFigure(f);
    }


    @Override
    public HexTileContents getContentsFor(AxialCoordinate coords, double tileWidth, double tileHeight) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        if (current.containsKey(coords)) {
            PiratosHexTileContents ht = current.get(coords);
            ht.tileWidth.setValue(tileWidth);
            ht.tileHeight.setValue(tileHeight);
            if (axialSelector != null) {
                ht.availableProperty.bind(Bindings.createBooleanBinding(() -> axialSelector.get() == null || axialSelector.get().isAvailable(coords), axialSelector));
            }

            if (highlightedCoordinates != null) {
                ht.highlightedProperty.bind(Bindings.createBooleanBinding(() -> highlightedCoordinates.contains(coords), highlightedCoordinates));
            }

            return ht;
        }
        return new HexTileContents() {
            private StringProperty css = new ReadOnlyStringWrapper("Sea");

            @Override
            public ObjectProperty<Node> contentsProperty() {
                return null;
            }

            @Override
            public StringProperty cssClassProperty() {
                return css;
            }
        };
    }

    public interface OnClickEventHandler {
        void OnClick(Iterable<MovableFigure> figures, AxialCoordinate ac, MouseEvent e);
    }

    private class PiratosHexTileContents implements HexTileContents {
        StackPane s = new StackPane();
        ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(s);
        HashMap<MovableFigure, Node> internalNodes = new HashMap<>();
        BoardTile bt;
        ListProperty<MovableFigure> figures = new SimpleListProperty<>();
        DoubleProperty tileWidth = new SimpleDoubleProperty();
        DoubleProperty tileHeight = new SimpleDoubleProperty();
        ArrayList<String> classes = new ArrayList<>();
        StringProperty classProperty = new SimpleStringProperty();
        BooleanProperty availableProperty = new SimpleBooleanProperty(true);
        BooleanProperty highlightedProperty = new SimpleBooleanProperty(false);

        Node tondaHack = null;

        private void tonikuvHack(AxialCoordinate coords) {
            tondaHack = new Label(coords.toString());
            s.getChildren().add(tondaHack);
        }

        private PiratosHexTileContents(BoardTile bt) {
            classes.add(bt.getClass().getSimpleName());
            updateClasses();
            this.bt = bt;
            if (onMouseClick != null) {
                s.setOnMouseClicked(e -> {
                    if (e.isStillSincePress() && e.getButton().equals(MouseButton.PRIMARY)) {
                        onMouseClick.OnClick(internalNodes.keySet(), bt.getLocation(), e);
                    }
                });
            }
            availableProperty.addListener(i -> {
                if (classes.contains("disabled")) {
                    classes.remove("disabled");
                }
                if (!availableProperty.get()) {
                    classes.add("disabled");
                }
                updateClasses();
            });


            highlightedProperty.addListener(i -> {
                if (classes.contains("highlighted")) {
                    classes.remove("highlighted");
                }
                if (highlightedProperty.get()) {
                    classes.add("highlighted");
                }
                updateClasses();
            });

        }

        private void updateClasses() {
            classProperty.setValue(String.join(" ", classes));
        }

        private void setSelected(boolean selected) {
            if (selected) {
                classes.add("selected");
            } else {
                classes.remove("selected");
            }
            updateClasses();
        }


        /**
         * Removes a MovableFigure from this node and returns the internal node representation, so it doesn't have to be recreated
         *
         * @param f
         * @return The internal node representing the figure,
         */
        Node removeFigure(MovableFigure f) {
            if (!internalNodes.containsKey(f)) {
                throw new UnsupportedOperationException("Cannot remove figure that's not in this tile!");
            }
            Node n = internalNodes.get(f);
            s.getChildren().remove(n);
            internalNodes.remove(f);
            return n;
        }


        void addFigure(MovableFigure f) {

            Node n;
            if (f.getClass().equals(Ship.class)) {
                ShipFigure s = new ShipFigure((Ship) f);
                s.maxWidthProperty().bind(tileWidth);
                s.maxHeightProperty().bind(tileHeight);
                n = s;
            } else {
                n = new Label(f.getClass().getSimpleName());
            }

            addFigure(f, n);


        }

        void addFigure(MovableFigure f, Node n) {
            internalNodes.put(f, n);
            s.getChildren().add(n);
            if (tondaHack != null) {
                tondaHack.toFront();
            }
        }


        @Override
        public ObjectProperty<Node> contentsProperty() {
            return contents;
        }

        @Override
        public StringProperty cssClassProperty() {
            return classProperty;
        }
    }

}
