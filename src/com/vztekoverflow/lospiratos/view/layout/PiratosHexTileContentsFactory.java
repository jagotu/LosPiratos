package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;
import com.vztekoverflow.lospiratos.viewmodel.MovableFigure;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

public class PiratosHexTileContentsFactory implements HexTileContentsFactory {

    private HashMap<AxialCoordinate, PiratosHexTileContents> current = new HashMap<>();
    private double tileWidth, tileHeight;
    private OnClickEventHandler onMouseClick;

    private ChangeListener<? super AxialCoordinate> highlightedMoveListener = (observable, oldValue, newValue) -> {
        if (oldValue != null && current.get(oldValue) != null) {
            current.get(oldValue).setSelected(false);
        }
        if (newValue != null && current.get(newValue) != null) {
            current.get(newValue).setSelected(true);
        }
    };

    public PiratosHexTileContentsFactory(Board board, OnClickEventHandler onMouseClick) {
        this.onMouseClick = onMouseClick;

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

    public PiratosHexTileContentsFactory(Board board) {
        this(board, null);
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

    public interface OnClickEventHandler {
        void OnClick(Iterable<MovableFigure> figures, MouseEvent e);
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
            this.bt = bt;
            if (onMouseClick != null) {
                s.setOnMouseClicked(e -> {
                    if (e.isStillSincePress() && e.getButton().equals(MouseButton.PRIMARY)) {
                        onMouseClick.OnClick(internalNodes.keySet(), e);
                    }
                });
            }

        }

        private void setSelected(boolean selected) {
            if (selected) {
                classProperty.setValue(bt.getClass().getSimpleName() + " selected");
            } else {
                classProperty.setValue(bt.getClass().getSimpleName());
            }
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

            n.rotateProperty().bind(f.getPosition().rotationProperty().subtract(120));
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
