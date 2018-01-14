package com.vztekoverflow.lospiratos.view.controls;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;

public class TeamView extends StackPane {

    @FXML
    private Rectangle backRect;
    @FXML
    private EditableText teamName;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private VBox vbox;

    private Team t;

    public interface RequestDeleteListener {
        void RequestDelete(Team t);
    }

    public RequestDeleteListener getRequestDeleteListener() {
        return requestDeleteListener;
    }

    public void setRequestDeleteListener(RequestDeleteListener requestDeleteListener) {
        this.requestDeleteListener = requestDeleteListener;
    }

    RequestDeleteListener requestDeleteListener = null;


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


        t.colorProperty().addListener(a -> updateColor());

        updateColor();

        teamName.textProperty().bindBidirectional(t.nameProperty());
        colorPicker.valueProperty().bindBidirectional(t.colorProperty());

        colorPicker.setSkin(new ColorPickerSkin(colorPicker) {
            @Override
            public Node getDisplayNode() {
                Label l = new Label();
                l.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PAINT_BRUSH));
                l.setPadding(new Insets(0));
                return l;
            }
        });

        ResourceView rw = new ResourceView(t.getOwnedResource());
        VBox.setMargin(rw, new Insets(4));
        vbox.getChildren().add(rw);


    }

    private void updateColor() {
        Color color = t.getColor();
        String style = String.format("-team-color: rgba(%d, %d, %d, %f);",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
        setStyle(style);
    }

    @FXML
    private void delete() {
        if (requestDeleteListener != null) {
            requestDeleteListener.RequestDelete(t);
        }
    }
}
