package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.PopOverSkin;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

import java.io.IOException;

public class CreateShipPopOver extends PopOver {

    @FXML
    private BorderPane root;
    @FXML
    private ComboBox<Team> teamSelect;
    @FXML
    private ComboBox<Class<? extends ShipType>> shipTypeSelect;
    @FXML
    private TextField captainName;
    @FXML
    private ResourceEdit cost;
    @FXML
    private Label targetPortLabel;
    @FXML
    private Button targetPortButton;
    @FXML
    private TextField shipName;


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

    private AxialCoordinateActionParameter targetPortParameter = new AxialCoordinateActionParameter() {
        @Override
        public BooleanExpression validValueProperty(ObservableValue<AxialCoordinate> value) {
            return Bindings.createBooleanBinding(() -> {
                if (value.getValue() == null) return false;
                return game.getBoard().getTiles().get(value.getValue()) instanceof Port;
            }, value, game.getBoard().tilesProperty());
        }

        @Override
        public String getČeskéJméno() {
            return "přístav";
        }
    };

    private Button okButton;

    static FXMLLoader fxmlLoader = new FXMLLoader(TeamView.class.getResource(
            "CreateShipPopOver.fxml"));

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        if (game != null) {
            bindTo(game);
        }
    }

    private void bindTo(Game g) {
        teamSelect.setItems(g.getTeams());
        shipTypeSelect.getItems().clear();
        shipTypeSelect.getItems().addAll(Game.getShipTypes());
        cost.setMode(EditableText.Mode.EDITOR);
        cost.resourceProperty().bind(Bindings.createObjectBinding(() -> {
            if (shipTypeSelect.getSelectionModel().getSelectedItem() == null) return null;
            return ShipType.createInstance(shipTypeSelect.getSelectionModel().getSelectedItem()).getBuyingCost().createMutableCopy();
        }, shipTypeSelect.getSelectionModel().selectedItemProperty()));
    }

    private Game game;

    public CreateShipPopOver() {
        setSkin(new PopOverSkin(this));
        fxmlLoader.setController(this);

        getStyleClass().add("create-ship");

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

        okButton = new Button("Šup sem s ní!");
        ButtonBar.setButtonData(okButton, ButtonBar.ButtonData.OK_DONE);
        okButton.setOnAction(e -> {
            teamSelect.getValue().buyNewShip(shipTypeSelect.getValue(), shipName.getText(), captainName.getText(), targetPortParameter.get());
            reset();
            hide();
        });
        okButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                    if (teamSelect.getValue() == null || shipTypeSelect.getValue() == null || shipName.getText() == null || shipName.getText().isEmpty() || captainName.getText() == null || captainName.getText().isEmpty() || targetPortParameter.get() == null)
                        return true;
                    if (!game.mayCreateShipWithName(shipName.getText())) return true;
                    if (!cost.resourceProperty().get().isLesserThanOrEqual(teamSelect.getValue().getOwnedResource()))
                        return true;
                    return false;
                }, teamSelect.getSelectionModel().selectedItemProperty(),
                shipTypeSelect.getSelectionModel().selectedItemProperty(),
                shipName.textProperty(),
                captainName.textProperty(),
                targetPortParameter.property()));
        buttons.getButtons().add(okButton);

        root.setBottom(buttons);

        Callback<ListView<Class<? extends ShipType>>, ListCell<Class<? extends ShipType>>> shipTypeCellFactory = new Callback<ListView<Class<? extends ShipType>>, ListCell<Class<? extends ShipType>>>() {
            @Override
            public ListCell<Class<? extends ShipType>> call(ListView<Class<? extends ShipType>> l) {
                return new ListCell<Class<? extends ShipType>>() {
                    @Override
                    protected void updateItem(Class<? extends ShipType> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            setText(ShipType.createInstance(item).getČeskéJméno());
                        }
                    }
                };
            }
        };
        shipTypeSelect.setButtonCell(shipTypeCellFactory.call(null));
        shipTypeSelect.setCellFactory(shipTypeCellFactory);

        Callback<ListView<Team>, ListCell<Team>> teamCellFactory = new Callback<ListView<Team>, ListCell<Team>>() {
            @Override
            public ListCell<Team> call(ListView<Team> l) {
                return new ListCell<Team>() {
                    @Override
                    protected void updateItem(Team item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        };
        teamSelect.setButtonCell(teamCellFactory.call(null));
        teamSelect.setCellFactory(teamCellFactory);

        targetPortLabel.textProperty().bind(targetPortParameter.property().asString());
        targetPortButton.setOnAction(e -> {
            if (onRequestAxialCoordinateListener != null) {
                onRequestAxialCoordinateListener.onRequestAxialCoordinate(targetPortParameter);
                hide();
            }
        });
        targetPortParameter.property().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                highlightedTiles.remove(oldValue);
            }
            if (newValue != null) {
                highlightedTiles.add(newValue);
            }
        });
        if (targetPortParameter.property().get() != null) {
            highlightedTiles.add(targetPortParameter.get());
        }


        setDetachable(false);
        setCloseButtonEnabled(false);
        setAutoFix(true);
        setAutoHide(false);

    }

    private void reset() {
        teamSelect.getSelectionModel().clearSelection();
        shipTypeSelect.getSelectionModel().clearSelection();
        captainName.setText("");
        shipName.setText("");
        targetPortParameter.set(null);
    }
}
