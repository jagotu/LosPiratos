package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ShipStatsView extends VBox {

    @FXML
    public FlowPane flow;

    @FXML
    private Label cannonsCount;
    @FXML
    private Label maxCargo;
    @FXML
    private Label garrison;
    @FXML
    private Label speed;
    @FXML
    private ProgressBar HPBar;
    @FXML
    private EditableIntegerText currentHP;
    @FXML
    private Label maxHP;
    @FXML
    private EditableStringText captain;
    @FXML
    private Label shipType;

    static FXMLLoader fxmlLoader = new FXMLLoader(ShipStatsView.class.getResource(
            "ShipStatsView.fxml"));


    public ShipStatsView(Ship s) {

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("ship-stats-view");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        cannonsCount.textProperty().bind(s.cannonsCountProperty().asString());
        maxCargo.textProperty().bind(s.maxCargoProperty().asString());
        garrison.textProperty().bind(s.garrisonCountProperty().asString());
        speed.textProperty().bind(s.speedProperty().asString());
        currentHP.valueProperty().bindBidirectional(s.currentHPProperty());
        HPBar.progressProperty().bind(s.currentHPProperty().add(0.0).divide(s.maxHPProperty()));
        maxHP.textProperty().bind(s.maxHPProperty().asString());
        captain.textProperty().bindBidirectional(s.captainNameProperty());
        shipType.setText(s.getShipType().getČeskéJméno());
    }
}
