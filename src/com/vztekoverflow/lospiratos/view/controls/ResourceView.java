package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.util.Locale;

public class ResourceView extends FlowPane {

    @FXML
    private EditableText money;
    @FXML
    private EditableText metal;
    @FXML
    private EditableText wood;
    @FXML
    private EditableText cloth;
    @FXML
    private EditableText rum;

    public ResourceView(Resource r) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "ResourceView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("resource-view");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        NumberStringConverter nsc = new NumberStringConverter(new Locale("cs"));
        money.textProperty().bindBidirectional(r.moneyProperty(), nsc);
        metal.textProperty().bindBidirectional(r.metalProperty(), nsc);
        wood.textProperty().bindBidirectional(r.woodProperty(), nsc);
        cloth.textProperty().bindBidirectional(r.clothProperty(), nsc);
        rum.textProperty().bindBidirectional(r.rumProperty(), nsc);
    }
}
