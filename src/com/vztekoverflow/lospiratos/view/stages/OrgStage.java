package com.vztekoverflow.lospiratos.view.stages;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.controls.*;
import com.vztekoverflow.lospiratos.view.layout.PiratosHexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.MovableFigure;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrgStage {


    private ObjectProperty<Game> game;

    //Controls
    @FXML
    private FlowPane teamsBox;
    @FXML
    private FlowPane shipsBox;
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


    private ActionParametersPopOver parametersPopOver = new ActionParametersPopOver();

    private final ExecutorService viewCreator = Executors.newSingleThreadExecutor(r -> {
        Thread thread = Executors.defaultThreadFactory().newThread(r);
        thread.setDaemon(true);
        return thread;
    });

    //Internal working helpers
    private Point2D lastMouse;
    private HashMap<Team, TeamView> teamViews = new HashMap<>();
    private HashMap<Ship, ShipView> shipViews = new HashMap<>();
    private static final int minMove = -3000;
    private static final int maxMove = 500;


    public OrgStage() {
        this(Game.CreateNewMockGame());
    }

    public OrgStage(Game game) {
        this.game = new SimpleObjectProperty<>(game);
        this.game.addListener(c -> connectToGame());
    }


    @FXML
    private void initialize() {
        root.setDividerPosition(0, 0.75);
        actionSelector.setOnActionSelectedListener(this::parametrizeAndPlan);
        mouseBlocker.prefWidthProperty().bind(mainPane.widthProperty());
        mouseBlocker.prefHeightProperty().bind(mainPane.heightProperty());
        mouseBlocker.visibleProperty().bind(parametersPopOver.showingProperty());
        parametersPopOver.setOnRequestAxialCoordinateListener(((action, par) -> {
            axialCoordinateActionParameterPending.set(par);
            parametersPopOverOwnerCache = parametersPopOver.getOwnerNode();
            parametersPopOver.hide();
            actionSelector.setVisible(false);
            actionSelector.setMouseTransparent(true);
        }));
        connectToGame();
    }

    private ObjectProperty<AxialCoordinateActionParameter> axialCoordinateActionParameterPending = new SimpleObjectProperty<>(null);
    private Node parametersPopOverOwnerCache = null;


    private void parametrizeAndPlan(PlannableAction act, Node n) {
        if (act instanceof ParameterizedAction) {
            parametersPopOver.setAction(act);
            parametersPopOver.show(n);
            return;
        }
        ActionsCatalog.relatedShip.get().planAction(act);
    }

    private boolean relocateActionSelector = false;
    private double edgeLength = 40;
    private boolean pointy = true;

    private void connectToGame() {
        if (hexPane != null) {
            root.getItems().remove(hexPane);
        }

        hexPane = new VirtualizingHexGridPane(edgeLength, pointy, new PiratosHexTileContentsFactory(game.get().getBoard(), edgeLength, pointy, (figures, ac, e) -> {
            if (axialCoordinateActionParameterPending.get() != null && axialCoordinateActionParameterPending.get().isAvailable(ac)) {
                axialCoordinateActionParameterPending.get().set(ac);
                axialCoordinateActionParameterPending.set(null);
                actionSelector.setVisible(true);
                actionSelector.setMouseTransparent(false);
                parametersPopOver.show(parametersPopOverOwnerCache);
                return;
            }
            for (MovableFigure f : figures) {
                if (f instanceof Ship) {
                    Ship s = (Ship) f;
                    ActionsCatalog.relatedShip.set(s);
                    actionSelector.setCurrentNode(ActionsCatalog.allPossiblePlannableActions);
                    final ShipView sv = shipViews.get(s);
                    ensureVisible(shipsScroll, sv);

                    relocateActionSelector = true;
                    return;
                }
            }
        }, axialCoordinateActionParameterPending, parametersPopOver.getHighlightedTiles()));
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
        Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        mainPane.setClip(clipRect);
        hexPane.maxWidthProperty().bind(mainPane.widthProperty());
        hexPane.maxHeightProperty().bind(mainPane.heightProperty());
        hexPane.relocate(0, 0);
        mainPane.getChildren().add(0, hexPane);

        setHexPanePanAndZoom();

        teamsBox.getChildren().clear();
        teamViews.clear();

        for (final Team t : game.get().getTeams()) {
            viewCreator.submit(() -> addTeamView(t));
        }

        game.get().getTeams().addListener((ListChangeListener.Change<? extends Team> c) -> {
            while (c.next()) {
                if (!(c.wasPermutated() || c.wasUpdated())) {
                    for (Team t : c.getAddedSubList()) {
                        viewCreator.submit(() -> addTeamView(t));
                    }
                    for (Team t : c.getRemoved()) {
                        removeTeamView(t);
                    }
                }
            }
        });

        shipsBox.getChildren().clear();
        shipViews.clear();

        for (final Ship s : game.get().getAllShips().values()) {
            viewCreator.submit(() -> addShipView(s));
        }

        game.get().allShipsProperty().addListener((MapChangeListener<? super String, ? super Ship>) change -> {
            if (change.wasRemoved()) {
                removeShipView(change.getValueRemoved());
            }
            if (change.wasAdded()) {
                viewCreator.submit(() -> addShipView(change.getValueAdded()));
            }

        });

        Platform.runLater(() -> hexPane.centerInParent(new AxialCoordinate(0, 0)));
    }


    private void addTeamView(Team t) {
        TeamView tv = new TeamView(t);
        tv.setRequestDeleteListener(team -> game.get().getTeams().remove(team));
        tv.setOnCenterShip(s -> {
            hexPane.centerInParent(s.getPosition().getCoordinate());
            hexPane.highlightTile(s.getPosition().getCoordinate());

        });
        tv.setOnShipDetails(this::showShipDetails);
        Platform.runLater(() -> teamsBox.getChildren().add(tv));

        teamViews.put(t, tv);
    }

    private void removeTeamView(Team t) {
        teamsBox.getChildren().remove(teamViews.get(t));
        teamViews.remove(t);

    }

    private void showShipDetails(Ship s) {
        ActionsCatalog.relatedShip.set(s);
        Point2D shippos = AxialCoordinate.hexToPixel(s.getPosition().getCoordinate(), pointy, edgeLength);
        actionSelector.layoutXProperty().unbind();
        actionSelector.layoutYProperty().unbind();
        actionSelector.layoutXProperty().bind(hexPane.XOffsetProperty().negate().add(shippos.getX() + hexPane.getTileWidth() / 2).divide(hexPane.scaleProperty()));
        actionSelector.layoutYProperty().bind(hexPane.YOffsetProperty().negate().add(shippos.getY() + hexPane.getTileHeight() / 2).divide(hexPane.scaleProperty()));
        actionSelector.setCurrentNode(ActionsCatalog.allPossiblePlannableActions);
        final ShipView sv = shipViews.get(s);
        ensureVisible(shipsScroll, sv);
    }


    private static final Background yellow = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background transparent = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));

    private void addShipView(Ship s) {
        ShipView sv = new ShipView(s);
        sv.setRequestDeleteListener(ship -> s.getTeam().removeShip(s.getName()));

        Platform.runLater(() -> shipsBox.getChildren().add(sv));
        sv.setOnCenterShip(sh -> {
            hexPane.centerInParent(sh.getPosition().getCoordinate());
            hexPane.highlightTile(sh.getPosition().getCoordinate());

        });

        sv.setOnShipDetails(this::showShipDetails);

        sv.backgroundProperty().bind(Bindings.when(ActionsCatalog.relatedShip.isEqualTo(s)).then(yellow).otherwise(transparent));

        shipViews.put(s, sv);
    }

    private void removeShipView(Ship s) {
        shipsBox.getChildren().remove(shipViews.get(s));
        shipViews.remove(s);

    }

    @FXML
    private void loremIpsum() {
        int a = 0;
    }

    @FXML
    private void evaluateRound() {
        game.get().closeRoundAndEvaluate();
    }

    @FXML
    private void createShip() {
        game.get().getTeams().get(0).createAndAddNewDefaultShip();
    }

    @FXML
    private void addTeam() {
        game.get().createAndAddNewDefaultTeam();
    }

    private void setHexPanePanAndZoom() {
        hexPane.setOnMousePressed(MouseEvent -> {
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnMouseDragged(MouseEvent -> {
            hexPane.setXOffset(Math.min(Math.max(hexPane.getXOffset() + (lastMouse.getX() - MouseEvent.getX()) * hexPane.getScale(), minMove), maxMove));
            hexPane.setYOffset(Math.min(Math.max(hexPane.getYOffset() + (lastMouse.getY() - MouseEvent.getY()) * hexPane.getScale(), minMove), maxMove));
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnScroll(ScrollEvent -> {
            double scale = Math.pow(1.005, -ScrollEvent.getDeltaY());
            double newScale = scale * hexPane.getScale();
            if (newScale > 2) {
                newScale = 2;
                scale = 2 / hexPane.getScale();
            }
            if (newScale < 0.1) {
                newScale = 0.1;
                scale = 0.1 / hexPane.getScale();
            }

            Point2D mouse = new Point2D(hexPane.getXOffset() + (ScrollEvent.getX() * hexPane.getScale()), hexPane.getYOffset() + (ScrollEvent.getY() * hexPane.getScale()));

            hexPane.setXOffset(Math.min(Math.max(mouse.getX() - (mouse.getX() - hexPane.getXOffset()) * scale, minMove), maxMove));
            hexPane.setYOffset(Math.min(Math.max(mouse.getY() - (mouse.getY() - hexPane.getYOffset()) * scale, minMove), maxMove));
            hexPane.setScale(newScale);
        });
    }

    private static void ensureVisible(ScrollPane pane, Node node) {
        double height = pane.getContent().getBoundsInLocal().getHeight();


        double y = node.getBoundsInParent().getMinY();

        if (y / height > 0.5) {
            y = node.getBoundsInParent().getMaxY();
        }

        pane.setVvalue(y / height);
    }
}

