package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.AxialDirection;
import com.vztekoverflow.lospiratos.util.Constants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.css.*;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
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
    private final double edgeLength;
    private final boolean pointy;
    private final double tileWidth;
    private final double tileHeight;
    private final ArrayList<Double> hexagonPoints;

    //Virtualization helpers
    private final HashMap<AxialCoordinate, HexTile> usedTiles = new HashMap<>();
    private final HashSet<SoftReference<HexTile>> freeTiles = new HashSet<>();

    //Internal dynamic calculations
    private final SimpleObjectProperty<AxialCoordinate> topLeft = new SimpleObjectProperty<>();
    private final ReadOnlyDoubleWrapper internalWidth = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper internalHeight = new ReadOnlyDoubleWrapper();

    //Contents factory
    private final HexTileContentsFactory factory;

    //Two planes for proper zIndex
    private final Pane tilesPlane = new Pane();
    private final Pane contentPlane = new Pane();

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


        //Generate points of tileShape
        hexagonPoints = new ArrayList<>(12);
        double a = pointy ? (30 * Math.PI / 180) : 0;
        for (int i = 0; i < 6; i++) {
            hexagonPoints.add(center.getX() + edgeLength * Math.cos(a));
            hexagonPoints.add(center.getY() + edgeLength * Math.sin(a));
            a += 60 * Math.PI / 180;
        }

        //Dynamically recalculates coordinates of top-left hexagon
        topLeft.bind(Bindings.createObjectBinding(() -> {
            if (pointy) {
                return AxialCoordinate.pixelToHex(new Point2D(XOffset.get() - Constants.SQRT_3 * edgeLength / 2, YOffset.get() - edgeLength), true, edgeLength).plus(AxialDirection.PointyUpLeft);
            } else {
                return AxialCoordinate.pixelToHex(new Point2D(XOffset.get() - edgeLength, YOffset.get() - Constants.SQRT_3 * edgeLength / 2), false, edgeLength).plus(AxialDirection.FlatUp);
            }
        }, XOffset, YOffset));

        Scale.addListener(o -> requestLayout());
        XOffset.addListener(o -> requestLayout());
        YOffset.addListener(o -> requestLayout());

        internalHeight.bind(heightProperty().multiply(Scale));
        internalWidth.bind(widthProperty().multiply(Scale));

        contentPlane.setMouseTransparent(true);

        this.getChildren().add(tilesPlane);
        this.getChildren().add(contentPlane);

    }

    public void centerInParent(AxialCoordinate coord) {
        Point2D location = AxialCoordinate.hexToPixel(coord, pointy, edgeLength);
        XOffset.setValue(location.getX() - (internalWidth.get() - Scale.get() * tileWidth)/2);
        YOffset.setValue(location.getY() - (internalHeight.get() - Scale.get() * tileHeight)/2);
    }


    private Shape getHexagon() {
        Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(hexagonPoints);
        hexagon.setFill(Color.TRANSPARENT);
        return hexagon;
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

    private void innerLoop(AxialCoordinate origin, AxialCoordinate vector, HashMap<String, List<HexTile>> localFreeTiles) {
        for (AxialCoordinate x = origin; ; x = x.plus(vector)) {
            Point2D pos = AxialCoordinate.hexToPixel(x, pointy, edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get()) {
                break;
            }
            HexTileContents contents = factory.getContentsFor(x, tileWidth, tileHeight);
            if (contents == null) {
                continue;
            }
            String cssClass = (contents.cssClassProperty() == null) ? "" : contents.cssClassProperty().get();
            HexTile t = null;
            boolean shouldAdd = true;
            if (!usedTiles.containsKey(x))
            {
                if(localFreeTiles.containsKey(cssClass)) //Try to get a tile that's still a child and has the same CSS style
                {                                        //This recyclation is the fastest of all, as the CSS doesn't have to be reapplied at all
                    List<HexTile> l = localFreeTiles.get(cssClass);
                    if(l.size() > 0) {
                        t = l.iterator().next();
                        l.remove(t);
                        shouldAdd = false;
                    }

                }
                if(t == null)
                {
                    while (freeTiles.size() > 0) //Try to get a nonGCed already instantiated tile
                    {
                        SoftReference<HexTile> ref = freeTiles.iterator().next();
                        freeTiles.remove(ref);
                        t = ref.get();
                        if (t != null) {
                            break;
                        }
                    }
                }


                if (t == null) {
                    Shape tileShape = getHexagon();
                    tileShape.getStyleClass().add("hexTile");
                    t = new HexTile(tileWidth, tileHeight, this, tileShape);
                }

                t.setContent(contents);
                if(shouldAdd)
                {
                    contentPlane.getChildren().add(t);
                    tilesPlane.getChildren().add(t.tileShape);
                }
                usedTiles.put(x, t);

            } else {
                t = usedTiles.get(x);
            }
            t.setScale(1 / Scale.get());
            layoutInArea(t, (pos.getX() - XOffset.get()) / scaleProperty().get(), (pos.getY() - YOffset.get()) / scaleProperty().get(), t.getWidth(), t.getHeight(), 0, HPos.LEFT, VPos.TOP);
            layoutInArea(t.tileShape, (pos.getX() - XOffset.get()) / scaleProperty().get(), (pos.getY() - YOffset.get()) / scaleProperty().get(), t.getWidth(), t.getHeight(), 0, HPos.LEFT, VPos.TOP);
        }
    }

    @Override
    protected void layoutChildren() {

        layoutInArea(contentPlane, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(tilesPlane, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);

        AxialCoordinate origin = topLeft.get();
        Point2D originpos = AxialCoordinate.hexToPixel(origin, pointy, edgeLength);

        HashMap<String, List<HexTile>> localFreeTiles = new HashMap<>();

        //Mark tiles that were visible but now aren't as free
        for (Iterator<Map.Entry<AxialCoordinate, HexTile>> it = usedTiles.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<AxialCoordinate, HexTile> entry = it.next();
            Point2D pos = AxialCoordinate.hexToPixel(entry.getKey(), pointy, edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get() || pos.getX() < originpos.getX() || pos.getY() < originpos.getY()) {
                entry.getValue().setContent(null);
                String cssClass = entry.getValue().cssClassName.get();
                if(!localFreeTiles.containsKey(cssClass))
                {
                    localFreeTiles.put(cssClass, new LinkedList<>());
                }
                localFreeTiles.get(cssClass).add(entry.getValue());
                it.remove();
            }
        }

        //Create all visible children
        AxialCoordinate inner = pointy ? AxialDirection.PointyDownRight : AxialDirection.FlatRightDown;
        AxialCoordinate outer1 = pointy ? AxialDirection.PointyRight : AxialDirection.FlatDown;
        AxialCoordinate outer2 = pointy ? new AxialCoordinate(-1, 2) : new AxialCoordinate(2, -1);
        for (AxialCoordinate x = origin; ; x = x.plus(outer1)) {
            Point2D pos = AxialCoordinate.hexToPixel(x, pointy, edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get()) {
                break;
            }
            innerLoop(x, inner, localFreeTiles);
        }

        for (AxialCoordinate x = origin; ; x = x.plus(outer2)) {
            Point2D pos = AxialCoordinate.hexToPixel(x,pointy,edgeLength);
            if (pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get()) {
                break;
            }
            innerLoop(x, inner, localFreeTiles);
        }

        //Remove all extra children
        for(List<HexTile> k : localFreeTiles.values())
        {
            for(HexTile h : k)
            {
                freeTiles.add(new SoftReference<>(h));
                h.setContent(null);
                contentPlane.getChildren().remove(h);
                tilesPlane.getChildren().remove(h.tileShape);
            }
        }
        localFreeTiles.clear();
    }

    static private class HexTile extends Region {
        private double width;
        private double height;


        private final javafx.scene.transform.Scale st = new Scale();
        private HexTileContents content;
        private final ObjectProperty<Node> contentNode = new SimpleObjectProperty<>(null);
        private final StringProperty cssClassName = new SimpleStringProperty("");

        private Shape tileShape;
        private VirtualizingHexGridPane parent;

        private static final CssMetaData<HexTile, Boolean> CLIP = new CssMetaData<HexTile, Boolean>("-hex-clip", StyleConverter.getBooleanConverter(), false) {
            @Override
            public boolean isSettable(HexTile styleable) {
                return styleable.clip == null || !styleable.clip.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(HexTile styleable) {
                return styleable.clip;
            }
        };

        private final StyleableBooleanProperty clip = new SimpleStyleableBooleanProperty(CLIP);

        private static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA;
        static {
            final List<CssMetaData<? extends Styleable, ?>> metaData = new ArrayList<>(Region.getClassCssMetaData());
            metaData.add(CLIP);
            CSS_META_DATA = Collections.unmodifiableList(metaData);

        }

        public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
            return CSS_META_DATA;
        }

        @Override public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
            return getClassCssMetaData();
        }

        private HexTile(double width, double height, VirtualizingHexGridPane parent, Shape tileShapeInstance) {
            setPickOnBounds(false);

            this.width = width;
            this.height = height;

            this.tileShape = tileShapeInstance;
            this.parent = parent;

            this.getStyleClass().add("hexParent");

            this.getTransforms().add(st);
            tileShape.getTransforms().add(st);
            st.setPivotX(0);
            st.setPivotY(0);

            tileShape.setOnMouseEntered(e -> toFront());

            contentNode.addListener((observable, oldValue, newValue) ->
            {
                if (oldValue != null) {
                    this.getChildren().remove(oldValue);
                    tileShape.onMouseClickedProperty().unbind();
                }
                if (newValue != null) {
                    newValue.setMouseTransparent(true);
                    this.getChildren().add(newValue);
                    tileShape.onMouseClickedProperty().bind(contentNode.get().onMouseClickedProperty());
                }
            });

            cssClassName.addListener((observable, oldValue, newValue) -> {
                if (oldValue != null && !oldValue.equals("")) {
                    this.getStyleClass().removeAll(oldValue.split(" "));
                    tileShape.getStyleClass().removeAll(oldValue.split(" "));
                }
                if (newValue != null && !newValue.equals("")) {
                    this.getStyleClass().addAll(newValue.split(" "));
                    tileShape.getStyleClass().addAll(newValue.split(" "));
                }
            });



            clip.addListener((observable ->
            {
                if (clip.get()) {
                    this.setClip(parent.getHexagon());
                } else {
                    this.setClip(null);
                }
            }));


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
            if (contentNode.get() != null) {
                layoutInArea(contentNode.get(), 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
            }
        }
    }
}