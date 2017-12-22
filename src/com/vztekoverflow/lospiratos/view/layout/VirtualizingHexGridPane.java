package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Constants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

import java.lang.ref.SoftReference;
import java.util.*;


public class VirtualizingHexGridPane extends Pane {

    //Public properties
    private final DoubleProperty XOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty YOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty Scale = new SimpleDoubleProperty(1);

    //Private values calculated by constructor
    private double edgeLength;
    private boolean pointy;
    private double tileWidth;
    private double tileHeight;
    private ArrayList<Double> hexagonPoints;

    //Virtualization helpers
    private HashMap<AxialCoordinate, HexTile> usedTiles = new HashMap<>();
    private HashSet<SoftReference<HexTile>> freeTiles = new HashSet<>();

    //Internal dynamic calculations
    private SimpleObjectProperty<AxialCoordinate> topLeft = new SimpleObjectProperty<>();
    private ReadOnlyDoubleWrapper internalWidth = new ReadOnlyDoubleWrapper();
    private ReadOnlyDoubleWrapper internalHeight = new ReadOnlyDoubleWrapper();

    //Contents factory
    private HexTileContentsFactory factory;

    public VirtualizingHexGridPane(double edgeLength, boolean pointy, HexTileContentsFactory factory) {
        this.edgeLength = edgeLength;
        this.pointy = pointy;
        this.factory = factory;

        if (pointy) {
            tileWidth = Constants.SQRT_3 * edgeLength;
            tileHeight = edgeLength * 2;
        } else {
            tileHeight = Constants.SQRT_3 * edgeLength;
            tileWidth = edgeLength * 2;
        }

        Point2D center;
        if (pointy) {
            center = new Point2D(Constants.SQRT_3 * edgeLength / 2, edgeLength);
        } else {
            center = new Point2D(edgeLength, Constants.SQRT_3 * edgeLength / 2);
        }

        hexagonPoints = new ArrayList<>(12);
        double a = pointy ? (30 * Math.PI / 180) : 0;
        for (int i = 0; i < 6; i++) {
            hexagonPoints.add(center.getX() + edgeLength * Math.cos(a));
            hexagonPoints.add(center.getY() + edgeLength * Math.sin(a));
            a += 60 * Math.PI / 180;
        }

        topLeft.bind(Bindings.createObjectBinding(() -> {
            if (pointy) {
                return AxialCoordinate.pixelToHex(new Point2D(XOffset.get() - Constants.SQRT_3 * edgeLength / 2, YOffset.get() - edgeLength), pointy, edgeLength);
            } else {
                return AxialCoordinate.pixelToHex(new Point2D(XOffset.get() - edgeLength, -YOffset.get() + Constants.SQRT_3 * edgeLength / 2), pointy, edgeLength);
            }
        }, XOffset, YOffset));

        Scale.addListener(observable -> requestLayout());
        XOffset.addListener(observable -> requestLayout());
        YOffset.addListener(observable -> requestLayout());

        internalHeight.bind(heightProperty().multiply(Scale));
        internalWidth.bind(widthProperty().multiply(Scale));
    }

    private Shape getHexagon() {
        Polygon croppingPolygon = new Polygon();
        croppingPolygon.getPoints().addAll(hexagonPoints);
        return croppingPolygon;
    }


    public double getXOffset() {
        return XOffset.get();
    }

    public void setXOffset(double XOffset) {
        this.XOffset.set(XOffset);
    }

    public DoubleProperty XOffsetProperty() {
        return XOffset;
    }

    public double getYOffset() {
        return YOffset.get();
    }

    public void setYOffset(double YOffset) {
        this.YOffset.set(YOffset);
    }

    public DoubleProperty YOffsetProperty() {
        return YOffset;
    }

    public double getScale() {
        return Scale.get();
    }

    public void setScale(double scale) {
        this.Scale.set(scale);
    }

    public DoubleProperty scaleProperty() {
        return Scale;
    }

    //TODO: Better naming?
    private void innerLoop(AxialCoordinate origin, AxialCoordinate vector) {
        for (AxialCoordinate x = origin; ; x = x.plus(vector)) {
            Point2D pos = AxialCoordinate.hexToPixel(x, pointy, edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get()) {
                break;
            }
            HexTileContents contents = factory.getContentsFor(x, tileWidth, tileHeight);
            if (contents == null) {
                continue;
            }
            HexTile t = null;
            if (!usedTiles.containsKey(x)) {
                while (freeTiles.size() > 0) //Try to get a nonGCed already instantianated tile
                {
                    SoftReference<HexTile> ref = freeTiles.iterator().next();
                    freeTiles.remove(ref);
                    t = ref.get();
                    if (t != null) {
                        break;
                    }
                }

                if (t == null) {
                    t = new HexTile(tileWidth, tileHeight);
                }

                t.setContent(contents);
                getChildren().add(t);
                usedTiles.put(x, t);

            } else {
                t = usedTiles.get(x);
            }
            t.setScale(1 / Scale.get());
            layoutInArea(t, (pos.getX() - XOffset.get()) / scaleProperty().get(), (pos.getY() - YOffset.get()) / scaleProperty().get(), t.getWidth(), t.getHeight(), 0, HPos.LEFT, VPos.TOP);
        }
    }

    @Override
    protected void layoutChildren() {

        //Get coords of the hexagon we start drawing from. We move one tile northwest from the tile being in the topleft corner.
        AxialCoordinate origin;
        if (pointy) {
            origin = topLeft.get().plus(new AxialCoordinate(0, -1));
        } else {
            origin = topLeft.get().plus(new AxialCoordinate(-1, 0));
        }
        Point2D originpos = AxialCoordinate.hexToPixel(origin, pointy, edgeLength);

        //Recycle tiles that were visible but now aren't
        for (Iterator<Map.Entry<AxialCoordinate, HexTile>> it = usedTiles.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<AxialCoordinate, HexTile> entry = it.next();
            Point2D pos = AxialCoordinate.hexToPixel(entry.getKey(), pointy, edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get() || pos.getX() < originpos.getX() || pos.getY() < originpos.getY()) {
                entry.getValue().setContent(null);
                freeTiles.add(new SoftReference<HexTile>(entry.getValue()));
                getChildren().remove(entry.getValue());
                it.remove();
            }
        }

        //Create all visible children
        AxialCoordinate inner = pointy ? new AxialCoordinate(0, 1) : new AxialCoordinate(1, 0);
        AxialCoordinate outer1 = pointy ? new AxialCoordinate(1, 0) : new AxialCoordinate(0, 1);
        AxialCoordinate outer2 = pointy ? new AxialCoordinate(-1, 2) : new AxialCoordinate(2, -1);
        for (AxialCoordinate x = origin; ; x = x.plus(outer1)) {
            Point2D pos = AxialCoordinate.hexToPixel(x, pointy, edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get()) {
                break;
            }
            innerLoop(x, inner);
        }

        for (AxialCoordinate x = origin; ; x = x.plus(outer2)) {
            Point2D pos = AxialCoordinate.hexToPixel(x,pointy,edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get()) {
                break;
            }
            innerLoop(x, inner);
        }

    }

    public class HexTile extends Region {
        double width;
        double height;


        javafx.scene.transform.Scale st = new Scale();
        HexTileContents content;
        ObjectProperty<Node> contentNode = new SimpleObjectProperty<>(null);
        StringProperty cssClassName = new SimpleStringProperty("");

        Shape tileShape;

        HexTile(double width, double height) {


            this.getTransforms().add(st);
            st.setPivotX(0);
            st.setPivotY(0);

            this.width = width;
            this.height = height;

            tileShape = VirtualizingHexGridPane.this.getHexagon();
            getChildren().add(tileShape);
            layoutInArea(tileShape, 0, 0, width, height, 0, HPos.LEFT, VPos.TOP);

            tileShape.getStyleClass().add("hexTile");
            this.getStyleClass().add("hexParent");

            contentNode.addListener((observable, oldValue, newValue) ->
            {
                if (oldValue != null) {
                    this.getChildren().remove(oldValue);
                }
                if (newValue != null) {
                    this.getChildren().add(newValue);
                }
            });

            cssClassName.addListener((observable, oldValue, newValue) -> {
                if (oldValue != null && !oldValue.equals("")) {
                    this.getStyleClass().remove(oldValue);
                }
                if (newValue != null && !newValue.equals("")) {
                    this.getStyleClass().add(newValue);
                }
            });

            /*clip.addListener((observable ->
            {
                if (clip.get()) {
                    this.setClip(VirtualizingHexGridPane.this.getHexagon());
                } else {
                    this.setClip(null);
                }
            }));*/


        }

        public HexTileContents getContent() {
            return content;
        }

        void setContent(HexTileContents content) {
            contentNode.unbind();
            cssClassName.unbind();
            if (content != null) {
                if (content.contentsProperty() != null) {
                    contentNode.bind(content.contentsProperty());
                } else {
                    contentNode.set(null);
                }

                if (content.cssClassProperty() != null) {
                    cssClassName.bind(content.cssClassProperty());
                } else {
                    cssClassName.set("");
                }
            }
            this.content = content;
        }

        final void setScale(double scale) {
            st.setX(scale);
            st.setY(scale);
        }

        @Override
        protected double computeMaxWidth(double height) {
            return width;
        }

        @Override
        protected double computePrefWidth(double height) {
            return width;
        }

        @Override
        protected double computeMinWidth(double height) {
            return width;
        }

        @Override
        protected double computePrefHeight(double width) {
            return height;
        }

        @Override
        protected double computeMaxHeight(double width) {
            return height;
        }

        @Override
        protected double computeMinHeight(double width) {
            return height;
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();
            for (Node child : getChildren()) {
                if (contentNode.get() != null) {
                    layoutInArea(contentNode.get(), 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
                }
            }

        }
    }
}