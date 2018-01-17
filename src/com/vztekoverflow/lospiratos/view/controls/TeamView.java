package com.vztekoverflow.lospiratos.view.controls;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.util.HashMap;

public class TeamView extends StackPane {

    @FXML
    private Rectangle backRect;
    @FXML
    private EditableStringText teamName;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private VBox vbox;
    @FXML
    private VBox overflow;

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

    private BooleanProperty expanded = new SimpleBooleanProperty(false);

    public interface CenterShipListener {
        void CenterShip(Ship s);
    }

    public CenterShipListener getOnCenterShip() {
        return onCenterShip;
    }

    public void setOnCenterShip(CenterShipListener onCenterShip) {
        this.onCenterShip = onCenterShip;
    }

    private CenterShipListener onCenterShip;

    public interface ShipDetailsListener {
        void ShipDetails(Ship s);
    }

    public ShipDetailsListener getOnShipDetails() {
        return onShipDetails;
    }

    public void setOnShipDetails(ShipDetailsListener onShipDetails) {
        this.onShipDetails = onShipDetails;
    }

    private ShipDetailsListener onShipDetails;

    static FXMLLoader fxmlLoader = new FXMLLoader(TeamView.class.getResource(
            "TeamView.fxml"));


    public TeamView(Team t) {
        this.t = t;

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("team-view");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        vbox.getChildren().remove(overflow);

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

        BorderPane shipsCount = new BorderPane();
        shipsCount.setMinWidth(40);
        Label shipLabel = new Label();
        shipLabel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.SHIP));
        BorderPane.setMargin(shipLabel, new Insets(0, 2, 0, 0));
        shipsCount.setLeft(shipLabel);
        Label shipCount = new Label();
        shipCount.textProperty().bind(t.shipsProperty().sizeProperty().asString());
        shipsCount.setCenter(shipCount);
        shipCount.setMaxWidth(Double.MAX_VALUE);
        rw.getChildren().add(shipsCount);

        Button showOverflow = new Button();
        showOverflow.getStyleClass().add("mini-button");
        final Glyph expandGlyph = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_CIRCLE_DOWN);
        final Glyph hideGlyph = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_CIRCLE_UP);
        showOverflow.graphicProperty().bind(Bindings.when(expanded).then(hideGlyph).otherwise(expandGlyph));
        rw.getChildren().add(showOverflow);
        showOverflow.setOnAction(e -> expanded.set(!expanded.get()));

        expanded.addListener(c -> {
            if (expanded.get()) {
                vbox.getChildren().add(overflow);
            } else {
                vbox.getChildren().remove(overflow);
            }


        });

        VBox.setMargin(rw, new Insets(4));
        vbox.getChildren().add(rw);


        for (Ship s : t.getShips().values()) {
            addShip(s);
        }


        t.shipsProperty().addListener((MapChangeListener<? super String, ? super Ship>) change -> {
            if (change.wasRemoved()) {
                removeShip(change.getValueRemoved());
            }
            if (change.wasAdded()) {
                addShip(change.getValueAdded());
            }


        });


    }

    HashMap<Ship, Node> ships = new HashMap<>();


    private void addShip(Ship s) {
        BorderPane bp = new BorderPane();
        Label l = new Label(s.getName());
        bp.setCenter(l);
        l.setMaxWidth(Double.MAX_VALUE);

        final Glyph centerGlyph = new Glyph("FontAwesome", FontAwesome.Glyph.BULLSEYE);
        final Glyph detailsGlyph = new Glyph("FontAwesome", FontAwesome.Glyph.INFO);


        HBox hb = new HBox();
        Button center = new Button();
        center.setGraphic(centerGlyph);
        center.getStyleClass().add("mini-button");
        center.setOnAction(e -> {
            if (onCenterShip != null) {
                onCenterShip.CenterShip(s);
            }
        });
        hb.getChildren().add(center);
        Button details = new Button();
        details.setGraphic(detailsGlyph);
        details.getStyleClass().add("mini-button");
        details.setOnAction(e -> {
            if (onShipDetails != null) {
                onShipDetails.ShipDetails(s);
            }
        });
        details.setMinWidth(16);
        hb.getChildren().add(details);
        BorderPane.setMargin(hb, new Insets(0, 4, 0, 0));
        bp.setLeft(hb);


        ships.put(s, bp);
        overflow.getChildren().add(bp);
    }

    private void removeShip(Ship s) {
        overflow.getChildren().remove(ships.get(s));
        ships.remove(s);
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
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Opravdu chcete smazat " + t.getName() + "?", ButtonType.YES, ButtonType.NO);
        a.showAndWait().filter(response -> response == ButtonType.YES).ifPresent(x ->
        {
            if (requestDeleteListener != null) {
                requestDeleteListener.RequestDelete(t);
            }
        });
    }
}
