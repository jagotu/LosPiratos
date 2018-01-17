package com.vztekoverflow.lospiratos.view.stages;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.CubeCoordinateMutable;
import com.vztekoverflow.lospiratos.view.controls.ShipView;
import com.vztekoverflow.lospiratos.view.controls.TeamView;
import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;

public class OrgStage {

    private ObjectProperty<Game> game;

    //Controls
    @FXML
    private FlowPane teamsBox;
    @FXML
    private FlowPane shipsBox;
    @FXML
    private SplitPane root;
    private VirtualizingHexGridPane hexPane;
    @FXML
    private ScrollPane shipsScroll;
    @FXML
    private Tab shipsTab;
    @FXML
    private TabPane tabPane;

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


    //TODO: Will be removed once HexTileContentsFactory is outsourced
    private static final int SIZE = 8;
    private HexTileContentsFactory fact = (coords, tileWidth, tileHeight) -> {

        CubeCoordinateMutable cube = coords.toCubeCoordinate();
        if (cube.getQ() < -SIZE || cube.getQ() > SIZE ||
                cube.getR() < -SIZE || cube.getR() > SIZE ||
                cube.getS() < -SIZE || cube.getS() > SIZE) {
            return null;
        }


        final Label l = new Label();
        l.setText(String.format("[%s,%s]", coords.getQ(), coords.getR()));

        final String cssClassName = coords.getR() % 2 == 0 ? "even" : "odd";

        return new HexTileContents() {

            ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(l);
            StringProperty cssClass = new ReadOnlyStringWrapper(cssClassName);

            @Override
            public ObjectProperty<Node> contentsProperty() {
                return contents;
            }

            @Override
            public StringProperty cssClassProperty() {
                return cssClass;
            }


        };

    };

    @FXML
    private void initialize() {
        hexPane = new VirtualizingHexGridPane(40, true, fact);

        Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        hexPane.setClip(clipRect);
        root.getItems().add(0, hexPane);

        setHexPanePanAndZoom();
        Platform.runLater(() -> hexPane.centerInParent(new AxialCoordinate(0, 0)));
        root.setDividerPosition(0, 0.75);

        connectToGame();
    }

    private void connectToGame() {
        teamsBox.getChildren().clear();
        teamViews.clear();


        for (Team t : game.get().teamsProperty()) {
            addTeamView(t);
        }

        game.get().teamsProperty().addListener((ListChangeListener.Change<? extends Team> c) -> {
            while (c.next()) {
                if (!(c.wasPermutated() || c.wasUpdated())) {
                    for (Team t : c.getAddedSubList()) {
                        addTeamView(t);
                    }
                    for (Team t : c.getRemoved()) {
                        removeTeamView(t);
                    }
                }
            }
        });

        shipsBox.getChildren().clear();
        shipViews.clear();


        for (Ship s : game.get().getAllShips()) {
            addShipView(s);
        }

        game.get().allShipsProperty().addListener((MapChangeListener<? super String, ? super Ship>) change -> {
            if (change.wasRemoved()) {
                removeShipView(change.getValueRemoved());
            }
            if (change.wasAdded()) {
                addShipView(change.getValueAdded());
            }

        });
    }

    private void addTeamView(Team t) {
        TeamView tv = new TeamView(t);
        tv.setRequestDeleteListener(team -> game.get().getTeams().remove(team));
        teamsBox.getChildren().add(tv);
        tv.setOnCenterShip(s -> {
            hexPane.centerInParent(s.getPosition());
            hexPane.highlightTile(s.getPosition());

        });
        tv.setOnShipDetails(s -> {
            tabPane.getSelectionModel().select(shipsTab);
            final ShipView sv = shipViews.get(s);
            ensureVisible(shipsScroll, sv);

            final Animation animation = new Transition() {

                {
                    setCycleDuration(Duration.millis(3000));
                    setInterpolator(Interpolator.EASE_OUT);
                }

                @Override
                protected void interpolate(double frac) {
                    Color vColor = new Color(1, 0, 0, 1 - frac);
                    sv.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            };
            animation.play();
        });

        teamViews.put(t, tv);
    }

    private void removeTeamView(Team t) {
        teamsBox.getChildren().remove(teamViews.get(t));
        teamViews.remove(t);

    }

    private void addShipView(Ship s) {
        ShipView sv = new ShipView(s);
        sv.setRequestDeleteListener(ship -> s.getTeam().removeShip(s.getName()));

        shipsBox.getChildren().add(sv);
        sv.setOnCenterShip(sh -> {
            hexPane.centerInParent(sh.getPosition());
            hexPane.highlightTile(sh.getPosition());

        });

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
    private void addTeam() {
        int i = 1;
        while (true) {
            final int j = i;
            if (game.get().getTeams().stream().noneMatch(t -> t.getName().equalsIgnoreCase("Tým #" + j))) break;
            i++;
        }

        game.get().createAndAddNewTeam("Tým #" + i, Color.BLACK);

    }

    private void setHexPanePanAndZoom() {
        hexPane.setOnMousePressed(MouseEvent -> lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY()));

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

