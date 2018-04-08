package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.PopOverSkin;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PopOver;

import java.io.IOException;

public class CreateWreckPopOver extends PopOver {

    @FXML
    private BorderPane root;
    @FXML
    private Label targetLabel;
    @FXML
    private Button targetButton;
    @FXML
    private GridPane gp;


    public interface OnRequestAxialCoordinateListener {
        void onRequestAxialCoordinate(AxialCoordinateActionParameter par);
    }

    private ActionParametersPopOver.OnRequestAxialCoordinateListener onRequestAxialCoordinateListener = null;

    public ActionParametersPopOver.OnRequestAxialCoordinateListener getOnRequestAxialCoordinateListener() {
        return onRequestAxialCoordinateListener;
    }

    public void setOnRequestAxialCoordinateListener(ActionParametersPopOver.OnRequestAxialCoordinateListener onRequestAxialCoordinateListener) {
        this.onRequestAxialCoordinateListener = onRequestAxialCoordinateListener;
    }

    public ObservableList<AxialCoordinate> getHighlightedTiles() {
        return highlightedTiles;
    }

    private ObservableList<AxialCoordinate> highlightedTiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    private AxialCoordinateActionParameter targetParameter = new AxialCoordinateActionParameter() {
        @Override
        public BooleanExpression validValueProperty(ObservableValue<AxialCoordinate> value) {
            return Bindings.createBooleanBinding(() -> true);
        }

        @Override
        public String getČeskéJméno() {
            return "pozice šipvreku";
        }
    };

    private Button okButton;

    private final Resource resources = new Resource();

    static FXMLLoader fxmlLoader = new FXMLLoader(TeamView.class.getResource(
            "CreateWreckPopOver.fxml"));

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private Game game;

    public CreateWreckPopOver() {
        setSkin(new PopOverSkin(this));
        fxmlLoader.setController(this);

        getStyleClass().add("create-wreck");

        try {
            this.setContentNode(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        ButtonBar buttons = new ButtonBar();

        Button cancelButton = new Button(ButtonType.CANCEL.getText());
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        cancelButton.setOnAction(e -> {
            reset();
            hide();
        });

        buttons.getButtons().add(cancelButton);

        okButton = new Button("Zavrekuj!");
        ButtonBar.setButtonData(okButton, ButtonBar.ButtonData.OK_DONE);
        okButton.setOnAction(e -> {
            game.createAndAddNewShipwreck(targetParameter.get(), resources);
            reset();
            hide();
        });
        okButton.disableProperty().bind(targetParameter.property().isNull());
        buttons.getButtons().add(okButton);

        root.setBottom(buttons);

        targetLabel.textProperty().bind(targetParameter.property().asString());
        targetButton.setOnAction(e -> {
            if (onRequestAxialCoordinateListener != null) {
                onRequestAxialCoordinateListener.onRequestAxialCoordinate(targetParameter);
                hide();
            }
        });
        targetParameter.property().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                highlightedTiles.remove(oldValue);
            }
            if (newValue != null) {
                highlightedTiles.add(newValue);
            }
        });
        if (targetParameter.property().get() != null) {
            highlightedTiles.add(targetParameter.get());
        }

        ResourceEdit re = new ResourceEdit();
        re.setMode(EditableText.Mode.EDITOR);
        re.setResource(resources);
        GridPane.setColumnIndex(re, 1);
        GridPane.setRowIndex(re, 1);
        gp.getChildren().add(re);


        setDetachable(false);
        setCloseButtonEnabled(false);
        setAutoFix(true);
        setAutoHide(false);

    }

    private void reset() {
        targetParameter.set(null);
        resources.multiply(0);
    }
}
