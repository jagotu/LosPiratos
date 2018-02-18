package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class ResourceEdit extends FlowPane {

    @FXML
    private EditableIntegerText money;
    @FXML
    private EditableIntegerText metal;
    @FXML
    private EditableIntegerText wood;
    @FXML
    private EditableIntegerText cloth;
    @FXML
    private EditableIntegerText rum;

    static FXMLLoader fxmlLoader = new FXMLLoader(ResourceEdit.class.getResource(
            "ResourceEdit.fxml"));

    public ResourceEdit(Resource r) {

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("resource-edit");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        money.valueProperty().bindBidirectional(r.moneyProperty());
        metal.valueProperty().bindBidirectional(r.metalProperty());
        wood.valueProperty().bindBidirectional(r.woodProperty());
        cloth.valueProperty().bindBidirectional(r.clothProperty());
        rum.valueProperty().bindBidirectional(r.rumProperty());
    }
}
