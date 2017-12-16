package com.vztekoverflow.lospiratos.view.layout;

import com.sun.xml.internal.bind.v2.util.QNameMap;
import javafx.beans.NamedArg;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.awt.*;
import java.util.*;


public class VirtualizingHexGridPane extends Pane {

    double edgeLength;
    boolean pointy;




    public Point2D getTopLeft() {
        return topLeft.get();
    }

    public SimpleObjectProperty<Point2D> topLeftProperty() {
        return topLeft;
    }

    public void setTopLeft(Point2D topLeft) {
        this.topLeft.set(topLeft);
    }

    SimpleObjectProperty<Point2D> topLeft = new SimpleObjectProperty<>();

    public double getInternalWidth() {
        return internalWidth.get();
    }

    public ReadOnlyDoubleWrapper internalWidthProperty() {
        return internalWidth;
    }

    public double getInternalHeight() {
        return internalHeight.get();
    }

    public ReadOnlyDoubleWrapper internalHeightProperty() {
        return internalHeight;
    }

    ReadOnlyDoubleWrapper internalWidth = new ReadOnlyDoubleWrapper();
    ReadOnlyDoubleWrapper internalHeight = new ReadOnlyDoubleWrapper();

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

    Rectangle rec;
    ArrayList<Double> croppingPoints;

    public VirtualizingHexGridPane(@NamedArg(value = "edgeLength",defaultValue = "20") double edgeLength, @NamedArg("pointy") boolean pointy) {
        this.edgeLength = edgeLength;
        this.pointy = pointy;

        rec = new Rectangle(10, 10, Color.RED);


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

        Point2D center;
        if(pointy)
        {
            center = new Point2D(Utils.SQRT_3*edgeLength/2, edgeLength);
        } else {
            center = new Point2D(edgeLength, Utils.SQRT_3*edgeLength/2);
        }



        croppingPoints = new ArrayList<>(12);
        double a =  pointy ? (30 * Math.PI / 180) : 0;
        for(int i = 0; i<6;i++)
        {
            croppingPoints.add(center.getX() + edgeLength * Math.cos(a));
            croppingPoints.add(center.getY() + edgeLength * Math.sin(a));
            a += 60 *  Math.PI / 180;
        }



    }

    public double getXOffset() {
        return XOffset.get();
    }

    public DoubleProperty XOffsetProperty() {
        return XOffset;
    }

    public void setXOffset(double XOffset) {
        this.XOffset.set(XOffset);
    }

    public double getYOffset() {
        return YOffset.get();
    }

    public DoubleProperty YOffsetProperty() {
        return YOffset;
    }

    public void setYOffset(double YOffset) {
        this.YOffset.set(YOffset);
    }

    public double getScale() {
        return Scale.get();
    }

    public DoubleProperty scaleProperty() {
        return Scale;
    }

    public void setScale(double scale) {
        this.Scale.set(scale);
    }

    private final DoubleProperty XOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty YOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty Scale = new SimpleDoubleProperty(1);

    HashMap<Point2D, HexTile> usedTiles = new HashMap<>();
    HashSet<HexTile> freeTiles = new HashSet<>();


    private void innerLoop(Point2D origin, Point2D vector)
    {
        for(Point2D x = origin; ;x = x.add(vector))
        {
            Point2D pos = hexToPixel(x);
            if(pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get())
            {
                break;
            }
            HexTile t;
            if(!usedTiles.containsKey(x))
            {

                if(freeTiles.size() > 0)
                {
                    t = freeTiles.iterator().next();
                    freeTiles.remove(t);
                } else {
                    Polygon croppingPolygon = new Polygon();
                    croppingPolygon.getPoints().addAll(croppingPoints);
                    t = new HexTile(croppingPolygon);
                }

                t.setContent(getNodeFor(x));
                t.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
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

        layoutInArea(rec, 0, 0, 10, 10, 0, HPos.LEFT, VPos.TOP);
        Point2D origin;
        if(pointy)
        {
            origin = topLeft.get().add(0, -1);
        } else {
            origin = topLeft.get().add(-1, 0);
        }
        Point2D originpos = hexToPixel(origin);

        //recycle newly hidden tiles
        for(Iterator<Map.Entry<Point2D, HexTile>> it = usedTiles.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Point2D, HexTile> entry = it.next();
            Point2D pos = hexToPixel(entry.getKey());
            if(pos.getX() - XOffset.get() > internalWidth.get() || pos.getY() - YOffset.get() > internalHeight.get() || pos.getX() < originpos.getX() || pos.getY() < originpos.getY())
            {
                entry.getValue().clearContent();
                freeTiles.add(entry.getValue());
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

        this.getChildren().remove(rec);
        this.getChildren().add(rec);


    }

    private Node getNodeFor(Point2D coords)
    {
        Label l = new Label();
        l.setText(String.format("[%s,%s]", coords.getX(), coords.getY()));
        return l;
    }

    public class HexTile extends Region {

        Polygon croppingPolygon;
        javafx.scene.transform.Scale st = new Scale();
        public HexTile(Polygon croppingPolygon) {

            this.croppingPolygon = croppingPolygon;
            this.setClip(croppingPolygon);
            this.getTransforms().add(st);
            st.setPivotX(0);
            st.setPivotY(0);
        }

        public final void setScale(double scale)
        {
            st.setX(scale);
            st.setY(scale);
        }

        public final void setContent(Node value) {
            getChildren().clear();
            getChildren().add(value);
        }

        public final void clearContent()
        {
            getChildren().clear();
        }

        public final Node getContent() {
            return getChildren().size() == 0 ? null : getChildren().get(0);
        }

        @Override
        protected double computeMaxWidth(double height) {
            return croppingPolygon.getLayoutBounds().getWidth();
        }

        @Override
        protected double computePrefWidth(double height) {
            return croppingPolygon.getLayoutBounds().getWidth();
        }

        @Override
        protected double computeMinWidth(double height) {
            return croppingPolygon.getLayoutBounds().getWidth();
        }

        @Override
        protected double computePrefHeight(double width) {
            return croppingPolygon.getLayoutBounds().getHeight();
        }

        @Override
        protected double computeMaxHeight(double width) {
            return croppingPolygon.getLayoutBounds().getHeight();
        }

        @Override
        protected double computeMinHeight(double width) {
            return croppingPolygon.getLayoutBounds().getHeight();
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();
            for(Node child : getChildren())
            {
                child.setLayoutX((croppingPolygon.getLayoutBounds().getWidth() - child.getLayoutBounds().getWidth())/2);
                child.setLayoutY((croppingPolygon.getLayoutBounds().getHeight() - child.getLayoutBounds().getHeight())/2);
            }

        }
    }
}
