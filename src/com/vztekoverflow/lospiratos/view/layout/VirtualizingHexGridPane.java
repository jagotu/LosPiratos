package com.vztekoverflow.lospiratos.view.layout;

import javafx.beans.NamedArg;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;


//TODO: Use AxialCoords everywhere
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
    private HashMap<Point2D, HexTile> usedTiles = new HashMap<>();
    private HashSet<SoftReference<HexTile>> freeTiles = new HashSet<>();

    //Internal dynamic calculations
    private SimpleObjectProperty<Point2D> topLeft = new SimpleObjectProperty<>();
    private ReadOnlyDoubleWrapper internalWidth = new ReadOnlyDoubleWrapper();
    private ReadOnlyDoubleWrapper internalHeight = new ReadOnlyDoubleWrapper();

    public VirtualizingHexGridPane(@NamedArg(value = "edgeLength",defaultValue = "20") double edgeLength, @NamedArg("pointy") boolean pointy) {
        this.edgeLength = edgeLength;
        this.pointy = pointy;

        if(pointy)
        {
            tileWidth = Utils.SQRT_3 * edgeLength;
            tileHeight = edgeLength * 2;
        } else {
            tileHeight = Utils.SQRT_3 * edgeLength;
            tileWidth = edgeLength * 2;
        }

        Point2D center;
        if(pointy)
        {
            center = new Point2D(Utils.SQRT_3*edgeLength/2, edgeLength);
        } else {
            center = new Point2D(edgeLength, Utils.SQRT_3*edgeLength/2);
        }

        hexagonPoints = new ArrayList<>(12);
        double a =  pointy ? (30 * Math.PI / 180) : 0;
        for(int i = 0; i<6;i++)
        {
            hexagonPoints.add(center.getX() + edgeLength * Math.cos(a));
            hexagonPoints.add(center.getY() + edgeLength * Math.sin(a));
            a += 60 *  Math.PI / 180;
        }

        //TODO: Move calculation to a different class after merge
        topLeft.bind(Bindings.createObjectBinding(() -> {

            double q, r;
            if(pointy)
            {
                double x = XOffset.get() - Utils.SQRT_3 * edgeLength/2;
                double y = -YOffset.get() + edgeLength;
                q = (x * Utils.SQRT_3/3 - y / 3) / edgeLength;
                r = y * 2/3 / edgeLength;
            } else {
                double x = XOffset.get() - edgeLength;
                double y = -YOffset.get() + Utils.SQRT_3 * edgeLength/2;
                q = x * 2/3 / edgeLength;
                r = (-x / 3 + Utils.SQRT_3/3 * y) / edgeLength;
            }
            return hexRound(q, r);
        }, XOffset, YOffset));

        Scale.addListener(observable -> requestLayout());
        XOffset.addListener(observable -> requestLayout());
        YOffset.addListener(observable -> requestLayout());

        internalHeight.bind(heightProperty().multiply(Scale));
        internalWidth.bind(widthProperty().multiply(Scale));
    }

    private Shape getHexagon()
    {
        Polygon croppingPolygon = new Polygon();
        croppingPolygon.getPoints().addAll(hexagonPoints);
        return croppingPolygon;
    }

    //TODO: Move calculation to a different class after merge
    private Point2D hexRound(double x, double z)
    {
        double y = -x-z;

        int rx = (int) Math.round(x);
        int rz = (int) Math.round(y);
        int ry = (int) Math.round(y);

        double x_diff = Math.abs(rx - x);
        double y_diff = Math.abs(ry - y);
        double z_diff = Math.abs(rz - z);

        if(x_diff > y_diff && x_diff > z_diff)
        {
            rx = -ry-rz;
        }
        else if (y_diff > z_diff)
        {
            ry = -rx-rz;
        }
        return new Point2D(rx, rz);
    }

    //TODO: Move calculation to a different class after merge
    private Point2D hexToPixel(Point2D hexCoords)
    {
        double x,y;
        if(pointy)
        {
            x = edgeLength * Utils.SQRT_3 * (hexCoords.getX() + hexCoords.getY()/2);
            y = edgeLength * 3/2 * hexCoords.getY();
        } else {
            x = edgeLength * 3/2 * hexCoords.getX();
            y = edgeLength * Utils.SQRT_3 * (hexCoords.getY() + hexCoords.getX()/2);
        }
        return new Point2D(x, y);
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
    private void innerLoop(Point2D origin, Point2D vector)
    {
        for(Point2D x = origin; ;x = x.add(vector))
        {
            Point2D pos = hexToPixel(x);
            if(pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get())
            {
                break;
            }
            HexTile t = null;
            if(!usedTiles.containsKey(x))
            {
                while(freeTiles.size() > 0) //Try to get a nonGCed already instantianated tile
                {
                    SoftReference<HexTile> ref = freeTiles.iterator().next();
                    freeTiles.remove(ref);
                    t = ref.get();
                    if(t != null) {
                        break;
                    }
                }

                if(t == null) {
                    t = new HexTile(tileWidth, tileHeight);
                }

                t.setContent(getNodeFor(x));
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
        Point2D origin;
        if(pointy)
        {
            origin = topLeft.get().add(0, -1);
        } else {
            origin = topLeft.get().add(-1, 0);
        }
        Point2D originpos = hexToPixel(origin);

        //Recycle tiles that were visible but now aren't
        for(Iterator<Map.Entry<Point2D, HexTile>> it = usedTiles.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Point2D, HexTile> entry = it.next();
            Point2D pos = hexToPixel(entry.getKey());
            if(pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get() || pos.getX() < originpos.getX() || pos.getY() < originpos.getY())
            {
                entry.getValue().clearContent();
                freeTiles.add(new SoftReference<HexTile>(entry.getValue()));
                getChildren().remove(entry.getValue());
                it.remove();
            }
        }

        //Create all visible children
        Point2D inner = pointy ? new Point2D(0,1) : new Point2D(1, 0);
        Point2D outer1 = pointy ? new Point2D(1,0) : new Point2D(0, 1);
        Point2D outer2 = pointy ? new Point2D(-1,2) : new Point2D(2, -1);
        for(Point2D x = origin; ;x = x.add(outer1))
        {
            Point2D pos = hexToPixel(x);
            if(pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get())
            {
                break;
            }
            innerLoop(x, inner);
        }

        for(Point2D x = origin.add(outer2); ;x = x.add(outer2))
        {
            Point2D pos = hexToPixel(x);
            if(pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get())
            {
                break;
            }
            innerLoop(x, inner);
        }

    }

    //TODO: move to factory
    private Node getNodeFor(Point2D coords)
    {
        Label l = new Label();
        l.setText(String.format("[%s,%s]", coords.getX(), coords.getY()));
        return l;
    }

    public class HexTile extends Region {
        //TODO: Optional clip
        //TODO: Bindable content
        double width;
        double height;


        javafx.scene.transform.Scale st = new Scale();
        Node content = null;
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

            tileShape.setFill(Color.WHITE);
            tileShape.setStroke(Color.BLACK);

        }

        final void setScale(double scale)
        {
            st.setX(scale);
            st.setY(scale);
        }

        final void clearContent()
        {
            if(content != null)
            {
                this.getChildren().remove(content);
            }
        }

        public final Node getContent() {
            return content;
        }

        final void setContent(Node value) {
            if(content != null)
            {
                this.getChildren().remove(content);
            }
            content = value;
            if(content != null)
            {
                this.getChildren().add(content);
            }
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
        protected double computePrefHeight(double width)
        {
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
            for(Node child : getChildren())
            {
                if(content != null)
                {
                    layoutInArea(content, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
                }
            }

        }
    }
}