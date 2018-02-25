package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.controls.EditableText;
import com.vztekoverflow.lospiratos.view.controls.ResourceEdit;
import com.vztekoverflow.lospiratos.view.controls.figures.ShipFigure;
import com.vztekoverflow.lospiratos.viewmodel.*;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.transitions.*;
import com.vztekoverflow.lospiratos.viewmodel.transitions.Transition;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PiratosHexTileContentsFactory implements HexTileContentsFactory {

    private static Font bigFont = new Font("Consolas", 30);

    private ObjectProperty<AxialCoordinateActionParameter> axialSelector;
    private HashMap<AxialCoordinate, PiratosHexTileContents> current = new HashMap<>();
    private double tileWidth, tileHeight;
    private OnClickEventHandler onMouseClick;
    private ObservableList<AxialCoordinate> highlightedCoordinates[];

    private ChangeListener<? super AxialCoordinate> highlightedMoveListener = (observable, oldValue, newValue) -> {
        if (oldValue != null && current.get(oldValue) != null) {
            current.get(oldValue).setSelected(false);
        }
        if (newValue != null && current.get(newValue) != null) {
            current.get(newValue).setSelected(true);
        }
    };

    public PiratosHexTileContentsFactory(Board board, double edgeLength, boolean pointy, OnClickEventHandler onMouseClick, ObjectProperty<AxialCoordinateActionParameter> axialSelector, ObservableList<AxialCoordinate>... highlightedCoordinates) {
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
        for (Figure mf : board.getFigures()) {
            addFigure(mf);
        }
        board.figuresProperty().addListener((ListChangeListener<Figure>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Figure addedItem : c.getAddedSubList()) {
                        addFigure(addedItem);
                    }
                } else if (c.wasRemoved()) {
                    for (Figure removedItem : c.getRemoved()) {
                        removeFigure(removedItem);
                    }
                }
            }
        });

        board.getGame().addOnMovementsEvaluatedListener(transitions -> {
            for (PiratosHexTileContents tileContents : current.values())
            {
                tileContents.animate(transitions);
            }
        });


    }

    public PiratosHexTileContentsFactory(Board board, double edgeLength, boolean pointy) {
        this(board, edgeLength, pointy, null, null, null);
    }

    private boolean pointy;
    private double edgeLength;
    private HashMap<Figure, ShipFigure> shipFigures = new HashMap<>();


    private void addFigure(Figure f) {
        if (!current.containsKey(f.getCoordinate())) {
            throw new UnsupportedOperationException("Figure out of bounds or not on a tile!");
        }
        current.get(f.getCoordinate()).addFigure(f);
        if (f instanceof MovableFigure) {
            ((MovableFigure) f).getPosition().coordinateProperty().addListener((observable, oldValue, newValue) ->
            {
                if (!current.containsKey(oldValue) || !current.containsKey(newValue)) {
                    throw new UnsupportedOperationException("Figure out of bounds or not on a tile!");
                }

                current.get(newValue).addFigure(f, current.get(oldValue).removeFigure(f));
            });
        }

    }

    private void removeFigure(Figure f) {
        if (!current.containsKey(f.getCoordinate())) {
            throw new UnsupportedOperationException("Figure out of bounds or not on a tile!");
        }

        current.get(f.getCoordinate()).removeFigure(f);
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
                ht.availableProperty.bind(Bindings.createBooleanBinding(() -> axialSelector.get() == null || axialSelector.get().isValidValue(coords), axialSelector));
            }

            if (highlightedCoordinates != null) {
                ht.highlightedProperty.bind(Bindings.createBooleanBinding(() -> Arrays.stream(highlightedCoordinates).anyMatch(x -> x.contains(coords)), highlightedCoordinates));
            }

            return ht;
        }
        return null;
    }

    public interface OnClickEventHandler {
        void OnClick(Iterable<Figure> figures, AxialCoordinate ac, MouseEvent e);
    }

    private class PiratosHexTileContents implements HexTileContents {
        StackPane s = new StackPane();
        ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(s);
        HashMap<Figure, Node> internalNodes = new HashMap<>();
        BoardTile bt;
        ListProperty<MovableFigure> figures = new SimpleListProperty<>();
        DoubleProperty tileWidth = new SimpleDoubleProperty();
        DoubleProperty tileHeight = new SimpleDoubleProperty();
        ArrayList<String> classes = new ArrayList<>();
        StringProperty classProperty = new SimpleStringProperty();
        BooleanProperty availableProperty = new SimpleBooleanProperty(true);
        BooleanProperty highlightedProperty = new SimpleBooleanProperty(false);

        Label tondaHack = null;

        private void tonikuvHack(AxialCoordinate coords) {
            tondaHack = new Label(coords.toString());
            tondaHack.setFont(bigFont);
            s.getChildren().add(tondaHack);
        }

        private PiratosHexTileContents(BoardTile bt) {
            s.prefWidthProperty().bind(tileWidth);
            s.prefHeightProperty().bind(tileHeight);
            classes.add(bt.getClass().getSimpleName());
            updateClasses();
            this.bt = bt;
            if (bt instanceof com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port) {
                s.getChildren().add(new ImageView("/com/vztekoverflow/lospiratos/view/port.png"));
            }
            if (bt instanceof com.vztekoverflow.lospiratos.viewmodel.boardTiles.Plantation) {
                s.getChildren().add(new ImageView("/com/vztekoverflow/lospiratos/view/plantation.png"));
            }
            if (bt instanceof Plunderable) {
                ResourceEdit re = getResourceEdit();
                re.setResource(((Plunderable) bt).getResource());
                s.getChildren().add(re);
            }
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
        Node removeFigure(Figure f) {
            if (!internalNodes.containsKey(f)) {
                throw new UnsupportedOperationException("Cannot remove figure that's not in this tile!");
            }
            Node n = internalNodes.get(f);
            s.getChildren().remove(n);
            internalNodes.remove(f);
            return n;
        }

        private void animate(Map<Ship, List<Transition>> transitions)
        {
            for(Ship s : transitions.keySet())
            {
                if(internalNodes.containsKey(s))
                {
                    SequentialTransition trans = new SequentialTransition(internalNodes.get(s));
                    Position position = s.getPosition().createCopy();
                    Node figureNode = internalNodes.get(s);
                    for(int i = transitions.get(s).size()-1; i>=0;i--) {
                        Transition t = transitions.get(s).get(i);
                        if (t instanceof Forward) {
                            TranslateTransition tt = new TranslateTransition();
                            AxialCoordinate prevPosition = position.getCoordinate().minus(position.getRotationAsDirection());
                            tt.setByX(AxialCoordinate.hexToPixel(position.getCoordinate(), pointy, edgeLength).getX() - AxialCoordinate.hexToPixel(prevPosition, pointy, edgeLength).getX());
                            tt.setByY(AxialCoordinate.hexToPixel(position.getCoordinate(), pointy, edgeLength).getY() - AxialCoordinate.hexToPixel(prevPosition, pointy, edgeLength).getY());
                            position.setCoordinate(prevPosition);
                            tt.setDuration(Duration.millis(1000));
                            tt.setNode(figureNode);
                            trans.getChildren().add(0, tt);
                        } else if (t instanceof Rotate) {
                            RotateTransition rt = new RotateTransition();
                            rt.setByAngle(((Rotate) t).getRotation());
                            rt.setDuration(Duration.millis(1000));
                            rt.setNode(figureNode);
                            position.setRotation(position.getRotation() - ((Rotate) t).getRotation());
                            trans.getChildren().add(0, rt);
                        } else if (t instanceof Teleport)
                        {
                            ParallelTransition pt = new ParallelTransition();
                            TranslateTransition tt = new TranslateTransition();
                            Point2D diff = AxialCoordinate.hexToPixel(((Teleport) t).getOriginPositionRelative(), pointy, edgeLength);
                            tt.setByX(diff.getX());
                            tt.setByY(diff.getY());
                            position.setCoordinate(((Teleport) t).getOriginPositionAbsolute());
                            tt.setDuration(Duration.millis(10));
                            if(t instanceof Bump) {
                                tt.setDuration(Duration.millis(1000));
                            }
                            tt.setNode(figureNode);

                            RotateTransition rt = new RotateTransition();
                            rt.setByAngle(((Teleport) t).getNewRotationAbsolute() - ((Teleport) t).getOriginalRotationAbsolute());
                            rt.setDuration(Duration.millis(10));
                            if(t instanceof Bump) {
                                rt.setDuration(Duration.millis(1000));
                            }
                            rt.setNode(figureNode);
                            position.setRotation(((Teleport) t).getOriginalRotationAbsolute());

                            pt.getChildren().addAll(tt, rt);

                            if(t instanceof Death)
                            {
                                SequentialTransition sqrt = new SequentialTransition();
                                FadeTransition ft1 = new FadeTransition(Duration.millis(500), figureNode);
                                ft1.setFromValue(1);
                                ft1.setToValue(0);
                                FadeTransition ft2 = new FadeTransition(Duration.millis(500), figureNode);
                                ft2.setFromValue(0);
                                ft2.setToValue(1);
                                sqrt.getChildren().addAll(ft1, pt, ft2);
                                trans.getChildren().add(0, sqrt);
                            } else {
                                trans.getChildren().add(0, pt);
                            }
                        }
                    }
                    if(figureNode instanceof ShipFigure)
                    {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), ((ShipFigure) figureNode).hpBar);
                        ft.setFromValue(1);
                        ft.setToValue(0);
                        trans.getChildren().add(0, ft);
                        FadeTransition ft2 = new FadeTransition(Duration.millis(200), ((ShipFigure) figureNode).hpBar);
                        ft2.setFromValue(0);
                        ft2.setToValue(1);
                        trans.getChildren().add(ft2);
                    }
                    internalNodes.get(s).setTranslateX(AxialCoordinate.hexToPixel(position.getCoordinate(), pointy, edgeLength).getX() - AxialCoordinate.hexToPixel(s.getPosition().getCoordinate(), pointy, edgeLength).getX());
                    internalNodes.get(s).setTranslateY(AxialCoordinate.hexToPixel(position.getCoordinate(), pointy, edgeLength).getY() - AxialCoordinate.hexToPixel(s.getPosition().getCoordinate(), pointy, edgeLength).getY());
                    internalNodes.get(s).setRotate(position.getRotation() - s.getPosition().getRotation());
                    trans.play();
                }
            }

        }


        void addFigure(Figure f) {

            Node n;
            if (f.getClass().equals(Ship.class)) {
                ShipFigure s = new ShipFigure((Ship) f, pointy);
                s.maxWidthProperty().bind(tileWidth);
                s.maxHeightProperty().bind(tileHeight);
                shipFigures.put(f, s);
                n = s;
            } else if (f.getClass().equals(Shipwreck.class)) {
                StackPane sp = new StackPane();
                ImageView iv = new ImageView("/com/vztekoverflow/lospiratos/view/wreck.png");
                iv.fitWidthProperty().bind(tileWidth.multiply(0.7));
                iv.fitHeightProperty().bind(tileHeight.multiply(0.7));
                iv.setPreserveRatio(true);
                sp.getChildren().add(iv);
                ResourceEdit re = getResourceEdit();
                re.setResource(((Shipwreck) f).getResource());
                sp.getChildren().add(re);
                n = sp;
            } else {
                Label l = new Label(f.getClass().getSimpleName());
                l.setFont(bigFont);
                n = l;
            }

            FadeTransition ft = new FadeTransition(Duration.millis(1000), n);
            ft.setToValue(1);
            ft.setFromValue(0);
            ft.play();

            addFigure(f, n);


        }

        private ResourceEdit getResourceEdit() {
            ResourceEdit re = new ResourceEdit();
            StackPane.setAlignment(re, Pos.CENTER);
            re.setAlignment(Pos.CENTER);
            re.setMode(EditableText.Mode.READONLY);
            re.getStyleClass().add("inmap-resources");
            re.setScaleY(2.2);
            re.setScaleX(2.2);
            re.maxWidthProperty().bind(tileWidth.multiply(0.6).divide(2.2));
            return re;
        }

        void addFigure(Figure f, Node n) {
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
