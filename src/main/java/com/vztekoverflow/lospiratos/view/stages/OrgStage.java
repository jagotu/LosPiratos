package com.vztekoverflow.lospiratos.view.stages;

import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.controls.*;
import com.vztekoverflow.lospiratos.view.layout.PiratosHexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.ShipsBox;
import com.vztekoverflow.lospiratos.view.layout.TeamsBox;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import com.vztekoverflow.lospiratos.viewmodel.Figure;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActivatePrivilegedMode;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.CannonsSimpleVolley;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.TurnLeft;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.TurnRight;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.ModifyShipTransaction;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.Plunder;
import com.vztekoverflow.lospiratos.viewmodel.logs.LogFormatter;
import com.vztekoverflow.lospiratos.viewmodel.logs.LoggedEvent;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class OrgStage {

    private ObjectProperty<Game> game;

    //Controls
    @FXML
    private TeamsBox teamsBox;
    @FXML
    private ShipsBox shipsBox;
    @FXML
    private SplitPane root;
    private VirtualizingHexGridPane hexPane = null;
    @FXML
    private ScrollPane shipsScroll;
    @FXML
    private Tab shipsTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Pane mainPane;
    @FXML
    private ActionSelector actionSelector;
    @FXML
    private PlannedActionsBar actionsBar;
    @FXML
    private Region mouseBlocker;
    @FXML
    private ListView<LoggedEvent> logListView;
    @FXML
    private Button evaluateRoundButton;
    @FXML
    private Button createShipButton;
    @FXML
    private Button createWreckButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button cancelAxialSelection;
    @FXML
    private HBox axialSelectorMessage;
    @FXML
    private Button evaluateRelatedShip;
    @FXML
    private Button commitTransactions;
    @FXML
    private Button autopilotButton;


    private ActionParametersPopOver parametersPopOver = new ActionParametersPopOver();

    //Internal working helpers
    private Point2D lastMouse;
    private static final int minMove = -4000;
    private static final int maxMove = 1000;


    public OrgStage() {
        this(Game.CreateNewDODGame());
    }

    public OrgStage(Game game) {
        this.game = new SimpleObjectProperty<>(game);
        this.game.addListener(c -> connectToGame());
    }

    private BooleanProperty hasModifyingTransaction = new SimpleBooleanProperty(false);
    private BooleanBinding hasModifyingTransactionBinding;


    @FXML
    private void initialize() {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON files", "*.json"), new FileChooser.ExtensionFilter("All files", "*.*"));
        root.setDividerPosition(0, 0.75);
        actionSelector.setOnActionSelectedListener(this::parametrizeAndPlan);
        mouseBlocker.prefWidthProperty().bind(mainPane.widthProperty());
        mouseBlocker.prefHeightProperty().bind(mainPane.heightProperty());
        mouseBlocker.visibleProperty().bind(parametersPopOver.showingProperty().or(createShipPopOver.showingProperty()).or(createWreckPopOver.showingProperty()));
        parametersPopOver.setOnRequestAxialCoordinateListener(((par) -> {
            axialCoordinateActionParameterPending.set(par);
            final Node parametersPopOverOwnerCache = parametersPopOver.getOwnerNode();
            restoreStateAfterAxialSelection = () -> {
                actionSelector.setVisible(true);
                actionSelector.setMouseTransparent(false);
                parametersPopOver.show(parametersPopOverOwnerCache);
            };
            parametersPopOver.hide();
            actionSelector.setVisible(false);
            actionSelector.setMouseTransparent(true);
        }));
        createShipPopOver.setOnRequestAxialCoordinateListener(((par) -> {
            axialCoordinateActionParameterPending.set(par);
            final Node parametersPopOverOwnerCache = createShipPopOver.getOwnerNode();
            restoreStateAfterAxialSelection = () -> {
                createShipPopOver.show(parametersPopOverOwnerCache);
            };
            createShipPopOver.hide();
        }));
        createWreckPopOver.setOnRequestAxialCoordinateListener(((par) -> {
            axialCoordinateActionParameterPending.set(par);
            final Node parametersPopOverOwnerCache = createWreckPopOver.getOwnerNode();
            restoreStateAfterAxialSelection = () -> {
                createWreckPopOver.show(parametersPopOverOwnerCache);
            };
            createWreckPopOver.hide();
        }));
        actionsBar.setOnActionDetailsListener((action, sender) -> {
            parametersPopOver.setAction(action);
            parametersPopOver.setReadOnly(true);
            parametersPopOver.show(sender);
        });
        messageLabel.textProperty().bind(Bindings.createStringBinding(() -> String.format("Vyberte %s!", axialCoordinateActionParameterPending.get() == null ? "null" : axialCoordinateActionParameterPending.get().getČeskéJméno()), axialCoordinateActionParameterPending));
        cancelAxialSelection.setText(ButtonType.CANCEL.getText());
        cancelAxialSelection.setOnAction(e -> {
            axialCoordinateActionParameterPending.set(null);
            if (restoreStateAfterAxialSelection != null) {
                restoreStateAfterAxialSelection.run();
            }
        });
        axialSelectorMessage.layoutXProperty().bind(mainPane.widthProperty().subtract(axialSelectorMessage.widthProperty()).divide(2));
        axialSelectorMessage.visibleProperty().bind(axialCoordinateActionParameterPending.isNotNull());

        OnCenterShipListener onCenterShipListener = s -> {
            hexPane.centerInParent(s.getPosition().getCoordinate());
            hexPane.highlightTile(s.getPosition().getCoordinate());
            publicHexPane.highlightTile(s.getPosition().getCoordinate());
        };
        shipsBox.setOnCenterShipListener(onCenterShipListener);
        teamsBox.setOnCenterShipListener(onCenterShipListener);

        shipsBox.setOnShipDetailsListener(this::showShipDetails);
        teamsBox.setOnShipDetailsListener(this::showShipDetails);

        evaluateRelatedShip.disableProperty().bind(ActionsCatalog.relatedShip.isNull());
        commitTransactions.disableProperty().bind(ActionsCatalog.relatedShip.isNull());

        ActionsCatalog.relatedShip.addListener(i -> {
            if(ActionsCatalog.relatedShip.get() == null)
            {
                hasModifyingTransaction.unbind();
                hasModifyingTransaction.set(false);
            } else {
                hasModifyingTransactionBinding = Bindings.createBooleanBinding(() ->
                        ActionsCatalog.relatedShip.get().getPlannedActions().stream().anyMatch(x -> x instanceof ModifyShipTransaction),
                        ActionsCatalog.relatedShip.get().plannedActionsProperty());
                hasModifyingTransaction.bind(hasModifyingTransactionBinding);
            }

        });

        hasModifyingTransaction.addListener((observable, oldValue, newValue) -> {
            if(oldValue)
            {
                commitTransactions.getStyleClass().remove("highlighted");
            }
            if(newValue)
            {
                commitTransactions.getStyleClass().add("highlighted");
            }
        });



        connectToGame();
    }

    private ObjectProperty<AxialCoordinateActionParameter> axialCoordinateActionParameterPending = new SimpleObjectProperty<>(null);
    private Runnable restoreStateAfterAxialSelection = null;


    private void parametrizeAndPlan(PlannableAction act, Node n) {
        if (act instanceof ParameterizedAction) {
            parametersPopOver.setReadOnly(false);
            parametersPopOver.setAction(act);
            parametersPopOver.show(n);
            Platform.runLater(parametersPopOver::requestFocus);
            return;
        }
        ActionsCatalog.relatedShip.get().planAction(act);
    }

    private boolean relocateActionSelector = false;
    private double edgeLength = 107.312;
    private boolean pointy = true; //DON'T SET TO FALSE
    private Stage publicMapStage = null;
    private Stage publicStatStage = null;

    private void setCADBackground(VirtualizingHexGridPane hexPane) {
        hexPane.setBackgroundGraphic(new ImageView("/cad_extended.png"));
        hexPane.setBackgroundGraphicOffset(new Point2D(-1160, -995));
    }

    private VirtualizingHexGridPane publicHexPane;

    private VirtualizingHexGridPane createMainHexPane() {
        VirtualizingHexGridPane hexPane = new VirtualizingHexGridPane(edgeLength, pointy, new PiratosHexTileContentsFactory(game.get().getBoard(), edgeLength, pointy, (figures, ac, e) -> {
            if (axialCoordinateActionParameterPending.get() != null && axialCoordinateActionParameterPending.get().isValidValue(ac)) {
                axialCoordinateActionParameterPending.get().set(ac);
                axialCoordinateActionParameterPending.set(null);
                if (restoreStateAfterAxialSelection != null) {
                    restoreStateAfterAxialSelection.run();
                }
                return;
            }
            for (Figure f : figures) {
                if (f instanceof Ship) {
                    Ship s = (Ship) f;
                    ActionsCatalog.relatedShip.set(s);
                    actionSelector.setCurrentNode(ActionsCatalog.allPossiblePlannableActions);
                    final ShipView sv = shipsBox.getShipViewFor(s);
                    ensureVisible(shipsScroll, sv);

                    relocateActionSelector = true;
                    return;
                }
            }
        }, axialCoordinateActionParameterPending, parametersPopOver.getHighlightedTiles(), createShipPopOver.getHighlightedTiles(), createWreckPopOver.getHighlightedTiles()));
        hexPane.setId("main-map");
        hexPane.setOnMouseClicked(e -> {
            if (relocateActionSelector) {
                double hexPaneX = hexPane.getXOffset() + e.getX() * hexPane.getScale();
                double hexPaneY = hexPane.getYOffset() + e.getY() * hexPane.getScale();

                actionSelector.layoutXProperty().unbind();
                actionSelector.layoutYProperty().unbind();
                actionSelector.layoutXProperty().bind(hexPane.XOffsetProperty().negate().add(hexPaneX).divide(hexPane.scaleProperty()));
                actionSelector.layoutYProperty().bind(hexPane.YOffsetProperty().negate().add(hexPaneY).divide(hexPane.scaleProperty()));
                relocateActionSelector = false;
            }
        });
        /*Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        mainPane.setClip(clipRect);*/
        hexPane.maxWidthProperty().bind(mainPane.widthProperty());
        hexPane.maxHeightProperty().bind(mainPane.heightProperty());
        hexPane.relocate(0, 0);
        setCADBackground(hexPane);
        setHexPanePanAndZoom(hexPane, 1.005);
        return hexPane;
    }

    private void updatePublicMapStage() {
        if (publicMapStage == null) {
            publicMapStage = new Stage();
            publicMapStage.setTitle("Public map");
            publicMapStage.setOnCloseRequest(Event::consume);
            publicMapStage.show();
        }

        publicHexPane = new VirtualizingHexGridPane(edgeLength, pointy, new PiratosHexTileContentsFactory(game.get().getBoard(), edgeLength, pointy));
        publicHexPane.getStylesheets().add("/common.css");
        publicHexPane.setId("public-map");
        publicMapStage.setScene(new Scene(publicHexPane, 800, 600));
        setCADBackground(publicHexPane);
        setHexPanePanAndZoom(publicHexPane, 1.001);
    }

    private ShipsBox publicShipsBox;
    private TeamsBox publicTeamsBox;

    private void updatePublicStatStage() {
        //Create public stat stage
        if (publicStatStage == null) {
            publicStatStage = new Stage();
            publicStatStage.setTitle("Public stats");
            publicStatStage.setOnCloseRequest(Event::consume);
            publicShipsBox = new ShipsBox();
            publicTeamsBox = new TeamsBox();
            SplitPane sp = new SplitPane();
            sp.setOrientation(Orientation.VERTICAL);
            sp.getItems().addAll(publicTeamsBox, publicShipsBox);
            sp.getStylesheets().add("/common.css");
            publicStatStage.setScene(new Scene(sp, 800, 600));
            publicStatStage.show();
        }

        publicTeamsBox.bindToGame(game.get());
        publicShipsBox.bindToGame(game.get());

    }

    private void connectToGame() {
        //Update hexPane
        mainPane.getChildren().remove(hexPane);
        if (hexPane != null) {
            root.getItems().remove(hexPane);
        }
        hexPane = createMainHexPane();
        mainPane.getChildren().add(0, hexPane);

        updatePublicMapStage();
        updatePublicStatStage();

        teamsBox.bindToGame(game.get());
        shipsBox.bindToGame(game.get());

        logListView.setItems(game.get().getLogger().getLoggedEvents());
        logListView.setCellFactory(new Callback<ListView<LoggedEvent>, ListCell<LoggedEvent>>() {
            @Override
            public ListCell<LoggedEvent> call(ListView<LoggedEvent> param) {
                return new ListCell<LoggedEvent>() {
                    @Override
                    protected void updateItem(LoggedEvent item, boolean empty) {

                        //Co je tohle? Proč setGraphic(text) a ne setText()???
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(new Text(""));
                            return;
                        }
                        Text text = new Text();
                        text.wrappingWidthProperty().bind(logListView.widthProperty().subtract(15));
                        text.setText(item.getTextualDescription(LogFormatter.hezkyČesky()));
                        setGraphic(text);
                    }
                };
            }
        });


        updateEvaluateButton();

        game.get().getAllShips().addListener((InvalidationListener) i -> {
            updateEvaluateButton();
        });

        createShipPopOver.setGame(game.get());
        createWreckPopOver.setGame(game.get());

        Platform.runLater(() -> hexPane.centerInParent(new AxialCoordinate(0, 0)));
    }

    private void updateEvaluateButton() {
        Observable[] shipaction = game.get().getAllShips().stream().map(Ship::plannedActionsProperty).toArray(Observable[]::new);
        LongBinding unplannedCountBinding = Bindings.createLongBinding(() -> game.get().getAllShips().stream().filter(x -> x.getPlannedActions().size() == 0).count(), shipaction);
        evaluateRoundButton.textProperty().bind(Bindings.format("Vyhodnotit kolo\n(%d lodí nemá nastavenou akci)", unplannedCountBinding));
    }


    private void showShipDetails(Ship s) {
        ActionsCatalog.relatedShip.set(s);
        Point2D shippos = AxialCoordinate.hexToPixel(s.getPosition().getCoordinate(), pointy, edgeLength);
        actionSelector.layoutXProperty().unbind();
        actionSelector.layoutYProperty().unbind();
        actionSelector.layoutXProperty().bind(hexPane.XOffsetProperty().negate().add(shippos.getX() + hexPane.getTileWidth() / 2).divide(hexPane.scaleProperty()));
        actionSelector.layoutYProperty().bind(hexPane.YOffsetProperty().negate().add(shippos.getY() + hexPane.getTileHeight() / 2).divide(hexPane.scaleProperty()));
        actionSelector.setCurrentNode(ActionsCatalog.allPossiblePlannableActions);
        final ShipView sv = shipsBox.getShipViewFor(s);
        ensureVisible(shipsScroll, sv);
    }


    @FXML
    private void loremIpsum() {
        Game g = ActionsCatalog.relatedShip.get().getTeam().getGame();
        int a = 0;
    }

    @FXML
    private void togglePrivilegedMode() {
        ActivatePrivilegedMode.available.set(!ActivatePrivilegedMode.available.get());
    }

    @FXML
    private void evaluateRound() {
        game.get().closeRoundAndEvaluate();
    }

    CreateShipPopOver createShipPopOver = new CreateShipPopOver();
    CreateWreckPopOver createWreckPopOver = new CreateWreckPopOver();

    @FXML
    private void createShip() {
        createShipPopOver.show(createShipButton);
    }

    @FXML
    private void createWreck() {
        createWreckPopOver.show(createWreckButton);
    }


    @FXML
    private void addTeam() {
        game.get().createAndAddNewDefaultTeam();
    }

    private void setHexPanePanAndZoom(VirtualizingHexGridPane hexPane, double zoomRatio) {
        hexPane.setOnMousePressed(MouseEvent -> {
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnMouseDragged(MouseEvent -> {
            hexPane.setXOffset(Math.min(Math.max(hexPane.getXOffset() + (lastMouse.getX() - MouseEvent.getX()) * hexPane.getScale(), minMove), maxMove));
            hexPane.setYOffset(Math.min(Math.max(hexPane.getYOffset() + (lastMouse.getY() - MouseEvent.getY()) * hexPane.getScale(), minMove), maxMove));
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnScroll(ScrollEvent -> {
            double scale = Math.pow(zoomRatio, -ScrollEvent.getDeltaY());
            double newScale = scale * hexPane.getScale();
            if (newScale > 4) {
                newScale = 4;
                scale = 4 / hexPane.getScale();
            }
            if (newScale < 0.3) {
                newScale = 0.3;
                scale = 0.3 / hexPane.getScale();
            }

            Point2D mouse = new Point2D(hexPane.getXOffset() + (ScrollEvent.getX() * hexPane.getScale()), hexPane.getYOffset() + (ScrollEvent.getY() * hexPane.getScale()));

            hexPane.setXOffset(Math.min(Math.max(mouse.getX() - (mouse.getX() - hexPane.getXOffset()) * scale, minMove), maxMove));
            hexPane.setYOffset(Math.min(Math.max(mouse.getY() - (mouse.getY() - hexPane.getYOffset()) * scale, minMove), maxMove));
            hexPane.setScale(newScale);
        });
    }

    private static void ensureVisible(ScrollPane pane, Node node) {
        double height = pane.getContent().getBoundsInLocal().getHeight();
        double result = node.getBoundsInParent().getMinY() / (height - pane.getHeight());

        pane.setVvalue(Math.min(result, 1));
    }

    FileChooser fileChooser = new FileChooser();

    private Stage parentStage;

    public Stage getParentStage() {
        return parentStage;
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    @FXML
    private void load() {
        fileChooser.setTitle("Load game...");
        File file = fileChooser.showOpenDialog(parentStage);
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
            game.set(Game.LoadFromModel(GameSerializer.LoadGameFromFile(file)));
        }
    }

    @FXML
    private void save() {
        fileChooser.setTitle("Save game...");
        File file = fileChooser.showSaveDialog(parentStage);
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
            GameSerializer.SaveGameToFile(file, game.get().getGameModel(), false);
        }
    }

    public void evaluateShip(ActionEvent actionEvent) {
        ActionsCatalog.relatedShip.get().evaluateAllActions();
    }

    public void commitTransactions(ActionEvent actionEvent) {
        ActionsCatalog.relatedShip.get().commitModifyingTransactions();
    }

    private double savedX = 0, savedY = 0, savedScale = 1;

    @FXML
    private void saveView(ActionEvent actionEvent) {
        savedX = hexPane.getXOffset();
        savedY = hexPane.getYOffset();
        savedScale = hexPane.getScale();
    }

    @FXML
    private void restoreView(ActionEvent actionEvent) {
        hexPane.setXOffset(savedX);
        hexPane.setYOffset(savedY);
        hexPane.setScale(savedScale);
    }

    public void epicEnterPressed(KeyEvent keyEvent) {
        ((TextField) keyEvent.getSource()).setText("⏎");
        if(keyEvent.getCode().equals(KeyCode.ENTER))
        {
            evaluateRound();
        }
    }

    public void autopilot(ActionEvent actionEvent) {
        if(timerIsActive){
            timer.cancel();
            timerIsActive = false;
            autopilotButton.setText("DOD autopilot zapnout");
        }else{
            timer.scheduleAtFixedRate(task, 0, 5*1000L);
            timerIsActive = true;
            autopilotButton.setText("DOD autopilot vypnout");
        }
    }

    private boolean timerIsActive = false;

    int autopilotCycleIterations = 0;
    int autopilotState = 0;
    private void autopilotNextStep(){
        Game g = game.get();
        g.getAllShips().forEach(s -> s.getPlannedActions().clear());
        try{
            //blue
            Ship s1 = g.getTeams().get(0).getShips().values().stream().filter(s -> s.getShipType() instanceof Brig).findFirst().get();
            if(autopilotState == 4){
                s1.planAction(new CannonsSimpleVolley(false));
            }
            s1.planAction(new MoveForward());
            s1.planAction(new TurnLeft());
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            //red
            Ship s1 = g.getTeams().get(1).getShips().values().stream().filter(s -> s.getShipType() instanceof Brig).findFirst().get();
            switch (autopilotState) {
                case 0:
                    if(autopilotCycleIterations == 0) break;
                    s1.planAction(new TurnRight());
                    s1.planAction(new TurnRight());
                    s1.planAction(new TurnRight());
                    s1.setCurrentHP(s1.getMaxHP());
                    break;
                case 1:
                    if(autopilotCycleIterations == 0) break;
                    s1.planAction(new MoveForward());
                    s1.planAction(new MoveForward());
                    break;
                case 2:
                    if(autopilotCycleIterations == 0) break;
                    s1.planAction(new TurnRight());
                    s1.planAction(new TurnRight());
                    s1.planAction(new TurnRight());
                    break;
                case 3:
                    s1.planAction(new TurnRight());
                    break;
                case 4:
                    s1.planAction(new TurnLeft());
                    break;
                case 5:
                    s1.planAction(new MoveForward());
                    s1.planAction(new MoveForward());
                    Plunder p = new Plunder();
                    p.getCommodities().setRum(50);
                    p.getCommodities().setMetal(20);
                    s1.planAction(p);
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            //green
            Ship s1 = g.getTeams().get(2).getShips().values().stream().filter(s -> s.getShipType() instanceof Brig).findFirst().get();
            switch (autopilotState){
                case 0:
                    s1.planAction(new MoveForward());
                    s1.planAction(new TurnLeft());
                    break;
                case 1:
                    s1.planAction(new MoveForward());
                    s1.planAction(new MoveForward());
                    s1.planAction(new TurnLeft());
                    break;
                case 2:
                    s1.planAction(new MoveForward());
                    s1.planAction(new MoveForward());
                    s1.planAction(new TurnLeft());
                    break;
                case 3:
                    s1.planAction(new ActivatePrivilegedMode());
                    s1.planAction(new MoveForward());
                    s1.planAction(new TurnLeft());
                    s1.planAction(new MoveForward());
                    s1.planAction(new MoveForward());
                    break;
                case 4:
                    s1.planAction(new TurnLeft());
                    s1.planAction(new MoveForward());
                    break;
                case 5:
                    s1.planAction(new MoveForward());
                    s1.planAction(new TurnLeft());
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            //red schooner
            Ship s1 = g.getTeams().get(1).getShips().values().stream().filter(s -> s.getShipType() instanceof Schooner).findFirst().get();
            switch (autopilotState) {
                case 2:
                    s1.planAction(new TurnRight());
                    s1.planAction(new TurnRight());
                    break;
                case 4:
                    s1.planAction(new TurnLeft());
                    s1.planAction(new TurnLeft());
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        secondAutopilotNextStepLolProtcSiToNezkomplikvoatZejo();

        g.closeRoundAndEvaluate();

        ++autopilotState;
        if(autopilotState == 6)
            ++autopilotCycleIterations;
        autopilotState %= 6;


    }

    private int secondAutopilotState = 0;
    private void secondAutopilotNextStepLolProtcSiToNezkomplikvoatZejo(){
        Game g = game.get();

        try {
            // green schooner
            Ship s = g.getTeams().get(2).getShips().values().stream().filter(sp -> sp.getShipType() instanceof Schooner).findFirst().get();
            switch (secondAutopilotState) {
                case 0:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    break;
                case 1:
                    s.planAction(new TurnRight());
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());
                    break;
                case 2:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());
                    s.planAction(new MoveForward());
                    break;
                case 3:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());
                    break;
                case 4:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());

                    break;
                case 5:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());
                    break;
                case 6:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnRight());
                    s.planAction(new MoveForward());
                    break;
                case 7:
                    s.planAction(new MoveForward());
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());
                    s.planAction(new MoveForward());
                    break;
                case 8:
                    s.planAction(new MoveForward());
                    s.planAction(new TurnLeft());
                    s.planAction(new MoveForward());
                    break;
                case 9:
                    s.planAction(new TurnLeft());
                    s.planAction(new MoveForward());
                    break;
                case 10:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ++secondAutopilotState;
        secondAutopilotState %= 11;
    }

    TimerTask task = new TimerTask()
    {
        public void run()
        {
            Platform.runLater(OrgStage.this::autopilotNextStep);
        }

    };

    Timer timer = new Timer();

}

