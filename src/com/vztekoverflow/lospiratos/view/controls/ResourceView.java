package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ResourceView extends FlowPane {

    @FXML
    private Label money;
    @FXML
    private Label metal;
    @FXML
    private Label wood;
    @FXML
    private Label cloth;
    @FXML
    private Label rum;

    @FXML
    private Pane moneyPane;
    @FXML
    private Pane metalPane;
    @FXML
    private Pane woodPane;
    @FXML
    private Pane clothPane;
    @FXML
    private Pane rumPane;

    static FXMLLoader fxmlLoader = new FXMLLoader(ResourceView.class.getResource(
            "ResourceView.fxml"));

    public ResourceReadOnly getResource() {
        return resource.get();
    }

    public ObjectProperty<ResourceReadOnly> resourceProperty() {
        return resource;
    }

    ObjectProperty<ResourceReadOnly> resource = new SimpleObjectProperty<>();

    public ResourceView() {

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("resource-view");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        resource.addListener(i -> {
            setResource(resource.get());
        });
        visibleProperty().bind(resource.isNotNull());
    }

    private void setResource(ResourceReadOnly r) {
        if (r == null) {
            return;
        }
        getChildren().clear();
        money.setText(Integer.toString(r.getMoney()));
        metal.setText(Integer.toString(r.getMetal()));
        wood.setText(Integer.toString(r.getWood()));
        cloth.setText(Integer.toString(r.getCloth()));
        rum.setText(Integer.toString(r.getRum()));

        if (r.getMoney() != 0) {
            getChildren().add(moneyPane);
        }
        if (r.getMetal() != 0) {
            getChildren().add(metalPane);
        }
        if (r.getWood() != 0) {
            getChildren().add(woodPane);
        }
        if (r.getCloth() != 0) {
            getChildren().add(clothPane);
        }
        if (r.getRum() != 0) {
            getChildren().add(rumPane);
        }
    }
}
