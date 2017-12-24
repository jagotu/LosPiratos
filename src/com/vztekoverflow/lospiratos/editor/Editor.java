package com.vztekoverflow.lospiratos.editor;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Editor extends Application {

    @FXML
    public VBox rightPanel;
    private VirtualizingHexGridPane hexPane;
    @FXML
    public BorderPane root;

    private Point2D lastMouse;

    @FXML
    public void initialize() {
        hexPane = new VirtualizingHexGridPane(40, true, new HexTileContentsFactory() {

            public HexTileContents getContentsFor(AxialCoordinate coords, double tileWidth, double tileHeight) {

                final String css = (coords.getR() == 0 && coords.getQ() == 0) ? "origin" : "";





                return new HexTileContents() {

                    ObjectProperty<Image> img = new SimpleObjectProperty<>();
                    StringProperty cssClass = new ReadOnlyStringWrapper(css);
                    ImageView iv = new ImageView();
                    ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(iv);

                    {
                        iv.setFitWidth(tileWidth);
                        iv.setFitHeight(tileHeight);
                        iv.setPreserveRatio(true);
                        iv.imageProperty().bind(img);
                        iv.setOnMouseClicked(MouseEvent -> {
                            if(MouseEvent.isStillSincePress())
                                img.set(new Image("lod.png"));
                        });
                    }



                    @Override
                    public ObjectProperty<Node> contentsProperty() {
                        return contents;
                    }

                    @Override
                    public StringProperty cssClassProperty() {
                        return cssClass;
                    }
                };
            }
        });

        Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        hexPane.setClip(clipRect);
        root.setCenter(hexPane);

        hexPane.setOnMousePressed(MouseEvent -> {
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnMouseDragged(MouseEvent -> {
            hexPane.setXOffset(hexPane.getXOffset() + (lastMouse.getX() - MouseEvent.getX())*hexPane.getScale());
            hexPane.setYOffset(hexPane.getYOffset() + (lastMouse.getY() - MouseEvent.getY())*hexPane.getScale());
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnScroll(ScrollEvent -> {
            double scale = Math.pow(1.005, -ScrollEvent.getDeltaY());
            double newScale = scale*hexPane.getScale();
            if(newScale > 2)
            {
                newScale = 2;
                scale = 2 / hexPane.getScale();
            }
            if(newScale < 0.1)
            {
                newScale = 0.1;
                scale = 0.1 / hexPane.getScale();
            }

            Point2D mouse = new Point2D(hexPane.getXOffset() + (ScrollEvent.getX() * hexPane.getScale()), hexPane.getYOffset() + (ScrollEvent.getY() * hexPane.getScale()));

            hexPane.setXOffset(mouse.getX() - (mouse.getX() - hexPane.getXOffset()) * scale);
            hexPane.setYOffset(mouse.getY() - (mouse.getY() - hexPane.getYOffset()) * scale);
            //hexPane.setYOffset(hexPane.getYOffset() + (hexPane.getHeight() * hexPane.getScale()) * (1.0 - 1/scale) / 2);
            hexPane.setScale(newScale);
        });

    }

    @FXML
    public void returnToOrigin()
    {
        hexPane.setXOffset(0);
        hexPane.setYOffset(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Editor.class.getResource("editor.fxml"));
        primaryStage.setTitle("Editor");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}