package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;
import com.vztekoverflow.lospiratos.viewmodel.MovableFigure;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

public class PiratosHexTileContentsFactory implements HexTileContentsFactory {

    private HashMap<AxialCoordinate, PiratosHexTileContents> current = new HashMap<>();
    private double tileWidth, tileHeight;

    public PiratosHexTileContentsFactory(Board board) {
        for (AxialCoordinate coords : board.getTiles().keySet()) {
            if (current.containsKey(coords)) {
                throw new UnsupportedOperationException("Cannot have the same tile defined twice!");
            }
            PiratosHexTileContents sc = new PiratosHexTileContents(board.getTiles().get(coords));
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
        if (current.containsKey(coords)) {
            PiratosHexTileContents ht = current.get(coords);
            ht.tileWidth.setValue(tileWidth);
            ht.tileHeight.setValue(tileHeight);
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

    private class PiratosHexTileContents implements HexTileContents {
        StackPane s = new StackPane();
        ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(s);
        HashMap<MovableFigure, Node> internalNodes = new HashMap<>();
        BoardTile bt;
        ListProperty<MovableFigure> figures = new SimpleListProperty<>();
        DoubleProperty tileWidth = new SimpleDoubleProperty();
        DoubleProperty tileHeight = new SimpleDoubleProperty();
        StringProperty classProperty = new SimpleStringProperty();


        private PiratosHexTileContents(BoardTile bt) {
            classProperty.setValue(bt.getClass().getSimpleName());
        }

        /**
         * Removes a MovableFigure from this node and returns the internal node represenation, so it doesn't have to be recreated
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
            return n;
        }


        void addFigure(MovableFigure f) {

            Node n;
            if (f.getClass().equals(Ship.class)) {
                ImageView iv = new ImageView();
                iv.setImage(new Image("simpleship.png"));
                iv.fitWidthProperty().bind(tileWidth.subtract(5));
                iv.fitHeightProperty().bind(tileHeight.subtract(5));
                iv.setSmooth(true);
                iv.setPreserveRatio(true);
                n = iv;
            } else {
                n = new Label(f.getClass().getSimpleName());
            }

            n.rotateProperty().bind(f.getPosition().rotationProperty());
            addFigure(f, n);


        }

        void addFigure(MovableFigure f, Node n) {
            internalNodes.put(f, n);
            s.getChildren().add(n);
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
