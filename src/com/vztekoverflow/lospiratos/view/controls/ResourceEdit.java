package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

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
    @FXML
    private BorderPane moneyPane;
    @FXML
    private BorderPane metalPane;
    @FXML
    private BorderPane woodPane;
    @FXML
    private BorderPane clothPane;
    @FXML
    private BorderPane rumPane;

    private ObjectProperty<EditableText.Mode> mode = new SimpleObjectProperty<>(EditableText.Mode.EDITABLE);
    private ObjectProperty<Resource> resource = new SimpleObjectProperty<>();

    public Resource getResource() {
        return resource.get();
    }

    public ObjectProperty<Resource> resourceProperty() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource.set(resource);
    }

    public EditableText.Mode getMode() {
        return mode.get();
    }

    public ObjectProperty<EditableText.Mode> modeProperty() {
        return mode;
    }

    public void setMode(EditableText.Mode mode) {
        this.mode.set(mode);
    }

    static FXMLLoader fxmlLoader = new FXMLLoader(ResourceEdit.class.getResource(
            "ResourceEdit.fxml"));

    public ResourceEdit() {

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("resource-edit");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        resource.addListener((observable, oldValue, newValue) -> {
            bindTo(newValue);
        });

        bindTo(resource.get());


    }

    private void bindTo(Resource r) {
        if (r == null) {
            moneyPane.managedProperty().unbind();
            moneyPane.visibleProperty().unbind();
            moneyPane.setManaged(false);
            moneyPane.setVisible(false);
            metalPane.managedProperty().unbind();
            metalPane.visibleProperty().unbind();
            metalPane.setManaged(false);
            metalPane.setVisible(false);
            woodPane.managedProperty().unbind();
            woodPane.visibleProperty().unbind();
            woodPane.setManaged(false);
            woodPane.setVisible(false);
            clothPane.managedProperty().unbind();
            clothPane.visibleProperty().unbind();
            clothPane.setManaged(false);
            clothPane.setVisible(false);
            rumPane.managedProperty().unbind();
            rumPane.visibleProperty().unbind();
            rumPane.setManaged(false);
            rumPane.setVisible(false);
            return;
        }
        money.valueProperty().bindBidirectional(r.moneyPropProperty());
        moneyPane.visibleProperty().bind(mode.isNotEqualTo(EditableText.Mode.READONLY).or(r.moneyPropProperty().isNotEqualTo(0)));
        moneyPane.managedProperty().bind(moneyPane.visibleProperty());
        money.modeProperty().bind(mode);
        metal.valueProperty().bindBidirectional(r.metalProperty());
        metalPane.visibleProperty().bind(mode.isNotEqualTo(EditableText.Mode.READONLY).or(r.metalProperty().isNotEqualTo(0)));
        metalPane.managedProperty().bind(metalPane.visibleProperty());
        metal.modeProperty().bind(mode);
        wood.valueProperty().bindBidirectional(r.woodProperty());
        woodPane.visibleProperty().bind(mode.isNotEqualTo(EditableText.Mode.READONLY).or(r.woodProperty().isNotEqualTo(0)));
        woodPane.managedProperty().bind(woodPane.visibleProperty());
        wood.modeProperty().bind(mode);
        cloth.valueProperty().bindBidirectional(r.clothProperty());
        clothPane.visibleProperty().bind(mode.isNotEqualTo(EditableText.Mode.READONLY).or(r.clothProperty().isNotEqualTo(0)));
        clothPane.managedProperty().bind(clothPane.visibleProperty());
        cloth.modeProperty().bind(mode);
        rum.valueProperty().bindBidirectional(r.rumProperty());


        rumPane.visibleProperty().bind(mode.isNotEqualTo(EditableText.Mode.READONLY).or(r.rumProperty().isNotEqualTo(0)));
        rumPane.managedProperty().bind(rumPane.visibleProperty());
        rum.modeProperty().bind(mode);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return super.computePrefHeight(width);
    }

    @Override
    public void requestFocus() {
        money.requestFocus();
    }
}
