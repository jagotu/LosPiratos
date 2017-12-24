package com.vztekoverflow.lospiratos.sample;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

public class SampleController {

    public VirtualizingHexGridPane hexPane;
    public Slider XSlider;
    public Slider YSlider;
    public Slider ScaleSlider;
    public StackPane root;

    @FXML
    public void initialize() {

        hexPane = new VirtualizingHexGridPane(40, true, new HexTileContentsFactory() {

            public HexTileContents getContentsFor(AxialCoordinate coords, double tileWidth, double tileHeight) {

                final Label l = new Label();
                l.setText(String.format("[%s,%s]", coords.getQ(), coords.getR()));
                l.setOnMouseClicked((MouseEvent) -> {
                    new Alert(Alert.AlertType.INFORMATION, "Clicked on " + coords.toString()).showAndWait();
                });

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
            }
        });

        XSlider.valueProperty().bindBidirectional(hexPane.XOffsetProperty());
        YSlider.valueProperty().bindBidirectional(hexPane.YOffsetProperty());
        ScaleSlider.valueProperty().bindBidirectional(hexPane.scaleProperty());

        root.getChildren().add(hexPane);
        hexPane.toBack();

    }

}
