package com.vztekoverflow.lospiratos.view.stages;

import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class OrgStageController
{
    public VirtualizingHexGridPane hexPane;
    public BorderPane root;
    private Point2D lastMouse;


    private HexTileContentsFactory hexTileContentsFactory;
    private static final int minMove = -3000;
    private static final int maxMove = 500;

    public OrgStageController(HexTileContentsFactory hexTileContentsFactory) {
        this.hexTileContentsFactory = hexTileContentsFactory;
    }

    @FXML
    public void initialize() {

        hexPane = new VirtualizingHexGridPane(40, true, hexTileContentsFactory);


        Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        hexPane.setClip(clipRect);
        root.setCenter(hexPane);

        hexPane.setOnMousePressed(MouseEvent -> lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY()));

        hexPane.setOnMouseDragged(MouseEvent -> {
            hexPane.setXOffset(Math.min(Math.max(hexPane.getXOffset() + (lastMouse.getX() - MouseEvent.getX()) * hexPane.getScale(), minMove), maxMove));
            hexPane.setYOffset(Math.min(Math.max(hexPane.getYOffset() + (lastMouse.getY() - MouseEvent.getY()) * hexPane.getScale(), minMove), maxMove));
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnScroll(ScrollEvent -> {
            double scale = Math.pow(1.005, -ScrollEvent.getDeltaY());
            double newScale = scale * hexPane.getScale();
            if (newScale > 2) {
                newScale = 2;
                scale = 2 / hexPane.getScale();
            }
            if (newScale < 0.1) {
                newScale = 0.1;
                scale = 0.1 / hexPane.getScale();
            }

            Point2D mouse = new Point2D(hexPane.getXOffset() + (ScrollEvent.getX() * hexPane.getScale()), hexPane.getYOffset() + (ScrollEvent.getY() * hexPane.getScale()));

            hexPane.setXOffset(Math.min(Math.max(mouse.getX() - (mouse.getX() - hexPane.getXOffset()) * scale, minMove), maxMove));
            hexPane.setYOffset(Math.min(Math.max(mouse.getY() - (mouse.getY() - hexPane.getYOffset()) * scale, minMove), maxMove));
            hexPane.setScale(newScale);
        });

        root.setCenter(hexPane);

    }
}
