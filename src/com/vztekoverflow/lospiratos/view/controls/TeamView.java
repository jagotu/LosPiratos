package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class TeamView extends StackPane {

    @FXML
    private Rectangle backRect;
    @FXML
    private EditableText teamName;
    @FXML
    private ColorPicker colorPicker;
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
    @FXML
    private FlowPane flow;

    private Team t;


    public TeamView(Team t) {
        this.t = t;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "TeamView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("team-view");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        t.colorProperty().addListener(a -> {
            updateColor();
        });

        updateColor();

        teamName.textProperty().bindBidirectional(t.nameProperty());
        colorPicker.valueProperty().bindBidirectional(t.colorProperty());

        Resource r = t.getOwnedResource();
        NumberStringConverter nsc = new NumberStringConverter();
        money.textProperty().bindBidirectional(r.moneyProperty(), nsc);
        metal.textProperty().bindBidirectional(r.metalProperty(), nsc);
        wood.textProperty().bindBidirectional(r.woodProperty(), nsc);
        cloth.textProperty().bindBidirectional(r.clothProperty(), nsc);
        rum.textProperty().bindBidirectional(r.rumProperty(), nsc);




    }

    private void updateColor()
    {
        Color color = t.getColor();
        String style = String.format("-team-color: rgba(%d, %d, %d, %f);",
                (int)(color.getRed()*255),
                (int)(color.getGreen()*255),
                (int)(color.getBlue()*255),
                color.getOpacity());
        setStyle(style);
    }
}
