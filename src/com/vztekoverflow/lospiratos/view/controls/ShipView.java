package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class ShipView extends StackPane {

    @FXML
    private Rectangle backRect;
    @FXML
    private EditableStringText shipName;
    @FXML
    private VBox vbox;
    @FXML
    private Label noActionsPlanned;

    private Ship s;

    public Ship getShip() {
        return s;
    }

    public interface RequestDeleteListener {
        void RequestDelete(Ship s);
    }

    public RequestDeleteListener getRequestDeleteListener() {
        return requestDeleteListener;
    }

    public void setRequestDeleteListener(RequestDeleteListener requestDeleteListener) {
        this.requestDeleteListener = requestDeleteListener;
    }

    private RequestDeleteListener requestDeleteListener = null;

    private BooleanProperty expanded = new SimpleBooleanProperty(false);

    public OnCenterShipListener getOnCenterShipListener() {
        return onCenterShipListener;
    }

    public void setOnCenterShipListener(OnCenterShipListener onCenterShipListener) {
        this.onCenterShipListener = onCenterShipListener;
    }

    public OnShipDetailsListener getOnShipDetailsListener() {
        return onShipDetailsListener;
    }

    public void setOnShipDetailsListener(OnShipDetailsListener onShipDetailsListener) {
        this.onShipDetailsListener = onShipDetailsListener;
    }

    private OnCenterShipListener onCenterShipListener;
    private OnShipDetailsListener onShipDetailsListener;

    static FXMLLoader fxmlLoader = new FXMLLoader(ShipView.class.getResource(
            "ShipView.fxml"));

    private BooleanExpression isSelected;

    public ShipView(Ship s) {
        this.s = s;

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("ship-view");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        s.getTeam().colorProperty().addListener(a -> updateColor());

        updateColor();

        shipName.textProperty().bindBidirectional(s.nameProperty());

        ShipStatsView ssv = new ShipStatsView(s);
        VBox.setMargin(ssv, new Insets(0, 4, 0, 4));
        vbox.getChildren().add(ssv);


        ResourceEdit rw = new ResourceEdit();
        rw.setResource(s.getStorage());
        VBox.setMargin(rw, new Insets(0, 4, 4, 4));
        vbox.getChildren().add(rw);

        noActionsPlanned.visibleProperty().bind(s.plannedActionsProperty().sizeProperty().isEqualTo(0));


        isSelected = ActionsCatalog.relatedShip.isEqualTo(s);
        isSelected.addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                getStyleClass().remove("related");
            }
            if (newValue) {
                getStyleClass().add("related");
            }
        });


    }


    private void updateColor() {
        Color color = s.getTeam().getColor();
        String style = String.format("-team-color: rgba(%d, %d, %d, %f);",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
        setStyle(style);
    }

    @FXML
    private void delete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Opravdu chcete smazat " + s.getName() + "?", ButtonType.YES, ButtonType.NO);
        a.showAndWait().filter(response -> response == ButtonType.YES).ifPresent(x ->
        {
            if (requestDeleteListener != null) {
                requestDeleteListener.RequestDelete(s);
            }
        });

    }

    @FXML
    private void center() {
        if (onCenterShipListener != null) {
            onCenterShipListener.onCenterShip(s);
        }
    }

    @FXML
    private void info() {
        if (onShipDetailsListener != null) {
            onShipDetailsListener.onShipDetails(s);
        }
    }

}
