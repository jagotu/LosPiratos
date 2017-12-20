package com.vztekoverflow.lospiratos.sample;

import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SampleController {

    public VirtualizingHexGridPane hexPane;
    public Slider XSlider;
    public Slider YSlider;
    public Slider ScaleSlider;
    public StackPane root;

    @FXML
    public void initialize() {

        hexPane = new VirtualizingHexGridPane(40, true, new HexTileContentsFactory() {

            public HexTileContents getContentsFor(Point2D coords, double tileWidth, double tileHeight) {

                final Label l = new Label();
                l.setText(String.format("[%s,%s]", coords.getX(), coords.getY()));


                /*
                if (coords.getY() < -5 || coords.getY() > 5 || coords.getX() < -5 || coords.getX() > 5) {
                    return null;
                }*/

                return new HexTileContents() {

                    ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(l);
                    ObjectProperty<Paint> background = new SimpleObjectProperty<>(Color.TRANSPARENT);
                    BooleanProperty clip = new ReadOnlyBooleanWrapper(true);

                    @Override
                    public ObjectProperty<Node> contentsProperty() {
                        return contents;
                    }

                    @Override
                    public ObjectProperty<Paint> backgroundProperty() {
                        return background;
                    }

                    @Override
                    public BooleanProperty clipProperty() {
                        return clip;
                    }
                };
            }
        });

        XSlider.valueProperty().bindBidirectional(hexPane.XOffsetProperty());
        YSlider.valueProperty().bindBidirectional(hexPane.YOffsetProperty());
        ScaleSlider.valueProperty().bindBidirectional(hexPane.scaleProperty());

        root.getChildren().add(hexPane);
        hexPane.toBack();

    }

}
