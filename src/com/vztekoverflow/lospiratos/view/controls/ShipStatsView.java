package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;

public class ShipStatsView extends VBox {

    @FXML
    public FlowPane flow;

    @FXML
    private Label cannonsCount;
    @FXML
    private Label maxCargo;
    //@FXML
    //private Label garrison;
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
    @FXML
    private HBox shipEnhancements;
    //@FXML
    //private EditableIntegerText xp;

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
        maxCargo.textProperty().bind(s.getStorage().capacityMaximumProperty().asString());
        //garrison.textProperty().bind(s.garrisonSizeProperty().asString());
        speed.textProperty().bind(s.speedProperty().asString());
        currentHP.valueProperty().bindBidirectional(s.currentHPProperty());
        HPBar.progressProperty().bind(s.currentHPProperty().add(0.0).divide(s.maxHPProperty()));
        maxHP.textProperty().bind(s.maxHPProperty().asString());
        captain.textProperty().bindBidirectional(s.captainNameProperty());
        //xp.valueProperty().bindBidirectional(s.XPProperty());
        s.shipTypeProperty().addListener((observable, oldValue, newValue) ->
                shipType.setText(newValue.getČeskéJméno()));
        shipType.setText(s.getShipType().getČeskéJméno());

        for (Class<? extends ShipEnhancement> enh : EnhancementsCatalog.allPossibleEnhancements) {
            Node n = getNodeFor(EnhancementsCatalog.getIcon(enh));
            s.enhancementStatusProperty(enh).addListener((observable, oldValue, newValue) -> {
                n.getStyleClass().remove(oldValue.toString());
                n.getStyleClass().add(newValue.toString());
            });
            n.getStyleClass().add(s.enhancementStatusProperty(enh).get().toString());
            shipEnhancements.getChildren().add(n);
        }
    }

    public static Node getNodeFor(EnhancementIcon enhancementIcon) {
        if (enhancementIcon == null) {
            return new Label("NULL");
        }
        Node n;
        switch (enhancementIcon) {
            case hull:
                n = new Glyph("piratos", 'J');
                break;
            case chain:
                n = new Glyph("piratos", 'H');
                break;
            case cannon:
                n = new Glyph("piratos", 'G');
                break;
            case mortar:
                n = new Glyph("piratos", 'K');
                break;
            case heavyBall:
                n = new Glyph("piratos", 'I');
                break;
            case ram:
                n = new Glyph("piratoslastminute", 'A');
                break;
            default:
                n = new Label(enhancementIcon.toString());
        }
        HBox.setMargin(n, new Insets(0, 2, 0, 2));
        if (n instanceof Glyph) {
            ((Glyph) n).setFontSize(18);
        }
        return n;
    }
}
