package com.vztekoverflow.lospiratos.sample;

import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class SampleController {

    public VirtualizingHexGridPane hexPane;
    public Slider XSlider;
    public Slider YSlider;
    public Label topleftCoords;
    public Slider ScaleSlider;
    public Label sizeLabel;

    @FXML
    public void initialize() {
        XSlider.valueProperty().bindBidirectional(hexPane.XOffsetProperty());
        YSlider.valueProperty().bindBidirectional(hexPane.YOffsetProperty());
        ScaleSlider.valueProperty().bindBidirectional(hexPane.scaleProperty());
    }

}
