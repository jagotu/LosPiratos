package com.vztekoverflow.lospiratos.view.stages;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.CubeCoordinateMutable;
import com.vztekoverflow.lospiratos.view.controls.TeamView;
import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.application.Platform;
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
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class OrgStageController
{
    private static final int SIZE = 8;
    public VirtualizingHexGridPane hexPane;
    public SplitPane root;
    private Point2D lastMouse;
    private Game game;
    @FXML
    private FlowPane teamsBox;


    private static final int minMove = -3000;
    private static final int maxMove = 500;

    public OrgStageController(Game game) {
        this.game = game;
    }

    private HexTileContentsFactory fact = (coords, tileWidth, tileHeight) -> {

        CubeCoordinateMutable cube = coords.toCubeCoordinate();
        if(cube.getQ() < -SIZE || cube.getQ() > SIZE ||
                cube.getR() < -SIZE || cube.getR() > SIZE ||
                cube.getS() < -SIZE || cube.getS() > SIZE)
        {
            return null;
        }



        final Label l = new Label();
        l.setText(String.format("[%s,%s]", coords.getQ(), coords.getR()));

        final String cssClassName = coords.getR() % 2 == 0 ? "even" : "odd";

        return new HexTileContents() {

            ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(l);
            StringProperty cssClass = new ReadOnlyStringWrapper(cssClassName);

            @Override
            public ObjectProperty<Node> contentsProperty() {
                return contents;
            }

            @Override
            public StringProperty cssClassProperty() {
                return cssClass;
            }


        };

    };

    @FXML
    public void initialize() {

        hexPane = new VirtualizingHexGridPane(40, true, fact);


        Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        hexPane.setClip(clipRect);
        root.getItems().add(0, hexPane);



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


        Platform.runLater(() -> hexPane.centerInParent(new AxialCoordinate(0, 0)));

        for(Team t : game.teamsProperty())//Todo: bind
        {
            teamsBox.getChildren().add(new TeamView(t));
        }

        root.setDividerPosition(0, 0.75);



    }

    public void loremIpsum()
    {
        int a = 0;
    }
}

