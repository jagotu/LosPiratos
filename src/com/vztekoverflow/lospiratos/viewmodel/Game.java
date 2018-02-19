package com.vztekoverflow.lospiratos.viewmodel;

import com.sun.javafx.collections.UnmodifiableObservableMap;
import com.vztekoverflow.lospiratos.evaluator.GameEvaluator;
import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.*;
import com.vztekoverflow.lospiratos.viewmodel.logs.EventLogger;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.*;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {

    private com.vztekoverflow.lospiratos.model.Game gameModel;

    public Game() {
        this(new com.vztekoverflow.lospiratos.model.Game());
    }

    private Game(com.vztekoverflow.lospiratos.model.Game gameModel) {
        this.gameModel = gameModel;

        gameModel.teamsProperty().addListener((ListChangeListener<com.vztekoverflow.lospiratos.model.Team>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (com.vztekoverflow.lospiratos.model.Team addedItem : c.getAddedSubList()) {
                        addNewTeamFromModel(addedItem);
                    }
                } else if (c.wasRemoved()) {
                    for (com.vztekoverflow.lospiratos.model.Team removedItem : c.getRemoved()) {
                        teams.removeIf(t -> t.getName().equalsIgnoreCase(removedItem.getName()));
                    }
                }
            }
        });
        board = new Board(this, gameModel.getMap());
    }

    private void addNewTeamFromModel(com.vztekoverflow.lospiratos.model.Team teamModel) {
        if (teams.stream().anyMatch(p -> p.getName().equalsIgnoreCase(teamModel.getName()))) {
            Warnings.makeWarning(toString(), "Attempt to create a team with a name that is already used: " + teamModel.getName());
            return;
        }
        Team t = new Team(this, teamModel);
        teams.add(t);
    }

    private int createAndAddNewDefaultTeamCounter = 1;

    /**
     * Is guaranteed to always return a new team, with some default initial values (including name)
     */
    public Team createAndAddNewDefaultTeam() {
        Team t = null;
        while (t == null) {
            String name = "Tým #" + ++createAndAddNewDefaultTeamCounter;
            t = createAndAddNewTeam(name, Color.BLACK);
        }
        return t;

    }

    /**
     * @return null if team with this name (case insensitive) already exists
     */
    public Team createAndAddNewTeam(String teamName, Color teamColor) {
        if (teamName == null || teamName.isEmpty()) {
            Warnings.makeWarning(toString(), "Attempt to create a team with null or empty name.");
            return null;
        }
        if (teams.stream().anyMatch(p -> p.getName().equalsIgnoreCase(teamName))) {
            Warnings.makeWarning(toString(), "Attempt to create a team with a name that is already used: " + teamName);
            return null;
        }
        com.vztekoverflow.lospiratos.model.Team teamModel = new com.vztekoverflow.lospiratos.model.Team();
        teamModel.nameProperty().set(teamName);
        teamModel.colorProperty().set(FxUtils.toRGBCode(teamColor));
        gameModel.teamsProperty().add(teamModel);
        //at this place, gameModel.teamsProperty's change calls my observer
        //   which then adds the team to this game's collection (if valid)
        Team t = findTeamByName(teamName);
        if (t == null) return null; //this means the model was somehow invalid
        t.initialize();
        return t;
    }

    public com.vztekoverflow.lospiratos.model.Game getGameModel() {
        return gameModel;
    }

    EventLogger logger = new EventLogger();

    public EventLogger getLogger() {
        return logger;
    }

    //region evaluate
    private int roundNo = 0;
    private GameEvaluator evaluator = GameEvaluator.createInstance(this);

    public void closeRoundAndEvaluate() {

        evaluator.evaluateRound(roundNo);
        logger.logRoundHasEnded(roundNo);
        ++roundNo;
        for (OnNextRoundStartedListener l : onNextRoundStartedListeners) {
            l.onNextRoundStarted(roundNo);
        }
        GameSerializer.SaveGameToFile(new File(Instant.now().toString().replace(':', '-') + "_round" + roundNo + ".json"), gameModel, false);
    }

    //endregion

    private final ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ReadOnlyListProperty<Team> unmodifiableTeams = new SimpleListProperty<>(FXCollections.unmodifiableObservableList(teams));

    /**
     * resulting list is unmodifiable
     */
    public ObservableList<Team> getTeams() {
        return unmodifiableTeams.get();
    }

    /**
     * resulting list is unmodifiable
     */
    public ReadOnlyListProperty<Team> teamsProperty() {
        return unmodifiableTeams;
    }

    private MapProperty<String, Ship> allShips = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private UnmodifiableObservableMap<String, Ship> unmodifiableAllShips = new UnmodifiableObservableMap<>(allShips.get());
    private ReadOnlyMapProperty<String, Ship> unmodifiableAllShipsProperty = new SimpleMapProperty<>(unmodifiableAllShips);

    public UnmodifiableObservableMap<String, Ship> getAllShips() {
        return unmodifiableAllShips;
    }

    /**
     * The resulting Map is unmodifiable and attempt to add a new ship throws an exception
     */
    public ReadOnlyMapProperty<String, Ship> allShipsProperty() {
        return unmodifiableAllShipsProperty;
    }

    public boolean mayCreateShipWithName(String name) {
        if (name == null || name.isEmpty()) {
            Warnings.makeDebugWarning(toString(), "Ship's name is empty or null.");
            return false;
        }
        return !allShips.containsKey(name);
    }

    private final Board board;

    public final Board getBoard() {
        return board;
    }

    void registerShip(Ship ship) {
        if (allShips.containsKey(ship.getName())) {
            Warnings.makeStrongWarning(toString(), "allShips already contains a ship with this name: " + ship.getName());
            return;
        }
        allShips.put(ship.getName(), ship);
        addOnNextRoundStartedListener(ship);
        board.figuresProperty().add(ship);
    }

    void unregisterShip(String shipName) {
        if (allShips.containsKey(shipName)) {
            boolean removedFromBoard = board.figuresProperty().remove(allShips.get(shipName));
            if (!removedFromBoard) {
                Warnings.panic(toString() + ".unregisterShip()", "Attempt to remove a ship which is in allShips but not in board.figures: " + shipName);
            }
            removeOnNextRoundStartedListener(allShips.get(shipName));
            allShips.remove(shipName);
        } else {
            Warnings.makeStrongWarning(toString() + ".unregisterShip()", "Attempt to remove a ship whose name is unknown: " + shipName);
        }
    }

    public void deleteShip(Ship s) {
        s.getTeam().removeShip(s.getName());
    }

    /**
     * @return null when no team with the name has been found
     */
    public Team findTeamByName(String teamName) {
        //this is a O(N) implementation. But there are just up to 10 teams in our game anyway...
        List<Team> result = getTeams().stream().filter(t -> t.getName().equals(teamName)).collect(Collectors.toList());
        int size = result.size();
        if (size == 0) {
            Warnings.makeWarning(toString(), "No team with this name found: " + teamName);
            return null;
        }
        if (size > 1) Warnings.panic(toString(), "More teams (" + size + ") with the same name: " + teamName);
        return result.get(0);
    }

    private Set<OnNextRoundStartedListener> onNextRoundStartedListeners = new HashSet<>();

    public void addOnNextRoundStartedListener(OnNextRoundStartedListener listener) {
        onNextRoundStartedListeners.add(listener);
    }

    public void removeOnNextRoundStartedListener(OnNextRoundStartedListener listener) {
        onNextRoundStartedListeners.remove(listener);
    }

    public Shipwreck createAndAddNewDefaultShipwreck() {
        return createAndAddNewShipwreck(AxialCoordinate.ZERO, ResourceReadOnly.ZERO);
    }

    public Shipwreck createAndAddNewShipwreck(AxialCoordinate position, ResourceReadOnly resource) {
        Shipwreck w = new Shipwreck(position, this);
        w.setResource(resource);
        board.figuresProperty().add(w);
        return w;
    }

    void remove(Shipwreck w) {
        board.figuresProperty().remove(w);
    }

    public static Game LoadFromModel(com.vztekoverflow.lospiratos.model.Game gameModel) {
        Game g = new Game(gameModel);
        for (com.vztekoverflow.lospiratos.model.Team teamModel : gameModel.getTeams()) {
            g.addNewTeamFromModel(teamModel);
        }
        return g;
    }

    //region mock

    private static String[] teamNames = {
            "Pirrraten mit nem Schwert",
            "You are a pirate",
            "Los Piratos",
            "Piráti na Vltavě",
            "пираты z Ruska",
            "Greek πειρατές",
            "海賊 z Japonska",
    };
    private static String[] captainNames = {
            "Jack",
            "Jack Sparrow",
            "Jack Black",
            "Jack Daniels",
            "Jack Reacher",
            "Jack Kerouac",
            "Jack Kennedy",
            "Jack O'Neill",
            "Jack Crawford",
            "Jack London",
            "Jack Nicholson",
            "Handsome Jack",
            "Jack de la Casa",
            "Jim&Jack",
            "Wild Jack",
            "Jumpin' Jack",
            "Jaccuzi",
            "Jackitas",
            "Jack and Jill",
            "Big Jack",
            "Hit the Road Jack",
            "Víťa BoJack"
    };

    public static Class<? extends ShipType>[] getShipTypes() {
        return shipTypes;
    }

    private static Class[] shipTypes = new Class[]{
            Schooner.class,
            Brig.class,
            Frigate.class,
            Galleon.class
    };

    private static Class<?>[] shipEnhancements = {
            CannonUpgrade.class,
            ChainShot.class,
            Ram.class,
            Mortar.class,
            HeavyShot.class,
            HullUpgrade.class,
    };


    public static Game CreateNewMockGame() {
        final int teamCount = 5; //beter do not make bigger than 5
        int captainIdx = 0;
        Game g = new Game();

        //ships' positions:

        List<AxialCoordinate> team1_shipPositions = new ArrayList<>();
        team1_shipPositions.add(new AxialCoordinate(-2, -3));

        List<AxialCoordinate> team2_shipPositions = new ArrayList<>();
        team2_shipPositions.add(new AxialCoordinate(-2, -2));
        team2_shipPositions.add(new AxialCoordinate(-2, -1));

        List<AxialCoordinate> team3_shipPositions = new ArrayList<>();
        team3_shipPositions.add(new AxialCoordinate(0, -3));
        team3_shipPositions.add(new AxialCoordinate(0, -2));
        team3_shipPositions.add(new AxialCoordinate(0, -1));

        List<AxialCoordinate> team4_shipPositions = new ArrayList<>();
        team4_shipPositions.add(new AxialCoordinate(1, -3));
        team4_shipPositions.add(new AxialCoordinate(2, -3));
        team4_shipPositions.add(new AxialCoordinate(4, -4));
        team4_shipPositions.add(new AxialCoordinate(6, -5));

        List<AxialCoordinate> team5_shipPositions = new ArrayList<>();
        team5_shipPositions.add(new AxialCoordinate(-3, 5));
        team5_shipPositions.add(new AxialCoordinate(-1, 3));
        team5_shipPositions.add(new AxialCoordinate(3, 1));
        team5_shipPositions.add(new AxialCoordinate(6, -1));
        team5_shipPositions.add(new AxialCoordinate(-2, 2));

        List<List<AxialCoordinate>> teamsShipPositions = new ArrayList<>();
        teamsShipPositions.add(team1_shipPositions);
        teamsShipPositions.add(team2_shipPositions);
        teamsShipPositions.add(team3_shipPositions);
        teamsShipPositions.add(team4_shipPositions);
        teamsShipPositions.add(team5_shipPositions);

        //create teams:
        for (int i = 1; i <= teamCount; i++) {
            String name = "(" + i + ") " + teamNames[i];
            Color c = Color.color((255 - (i - 1) * 49) / 255d, ((i - 1) * 49) / 255d, ((i - 1) * 49d) / 255d);
            Team team = g.createAndAddNewTeam(name, c);
            team.getOwnedResource().setMoney(5000 + i);
            team.getOwnedResource().setCloth(10 * i);
            team.getOwnedResource().setMetal(20 * i);
            team.getOwnedResource().setRum(30 * i);
            team.getOwnedResource().setWood(40 * i);
            //Create ships:
            for (int j = 0; j < i; j++) {
                AxialCoordinate position = teamsShipPositions.get(i - 1).get(j);
                //AxialCoordinate position = AxialCoordinate.ZERO;
                name = "Tým" + i + "_Loď" + (j + 1);
                String captain = captainNames[captainIdx++];
                Class<ShipType> type = (Class<ShipType>) shipTypes[j % 4];
                Ship s = team.createAndAddNewShip(type, name, captain, position);
                s.getPosition().setRotation(60 * j * (i + 1));
                s.getStorage().addMoney(500 * i + 10 * j);
                s.getStorage().addCloth(10 * i + j);
                s.getStorage().addMetal(20 * i + j);
                s.getStorage().addRum(30 * i + j);
                s.getStorage().addWood(40 * i + j);
                if (i != 3) //random value
                    s.takeDamage(6 * j);
                for (int k = 0; k < j + i; k++) {
                    if (k >= shipEnhancements.length) continue;
                    Class<ShipEnhancement> enh = (Class<ShipEnhancement>) shipEnhancements[k];
                    s.addNewEnhancement(enh);
                }
                if ((position.getQ() == -2 && position.getR() == -2)) { //second ship of second team
                    s.destroyShipAndEnhancements();
                }
            }
        }

        List<AxialCoordinate> shipwrecks = new ArrayList<>();
        shipwrecks.add(new AxialCoordinate(-2, 0));
        shipwrecks.add(new AxialCoordinate(3, 0));
        shipwrecks.add(new AxialCoordinate(5, 0));
        shipwrecks.add(new AxialCoordinate(2, -4));

        ResourceReadOnly r = new ResourceReadOnly(100, 10, 10, 10, 10, 10);
        for (AxialCoordinate c : shipwrecks) {
            g.createAndAddNewShipwreck(c, r);
        }

        //tiles position are according to the bitmap game map (by Bratr)

        List<AxialCoordinate> ports = new ArrayList<>();
        ports.add(new AxialCoordinate(-2, -3));
        ports.add(new AxialCoordinate(2, -3));
        ports.add(new AxialCoordinate(6, -2));
        ports.add(new AxialCoordinate(-4, 1));
        ports.add(new AxialCoordinate(-6, 6));
        ports.add(new AxialCoordinate(-3, 5));
        ports.add(new AxialCoordinate(2, 4));


        List<AxialCoordinate> shores = new ArrayList<>();
        shores.add(new AxialCoordinate(3, -6));
        shores.add(new AxialCoordinate(1, -2));
        shores.add(new AxialCoordinate(2, -2));
        shores.add(new AxialCoordinate(3, -3));
        shores.add(new AxialCoordinate(5, -2));
        shores.add(new AxialCoordinate(1, 1));
        shores.add(new AxialCoordinate(2, 1));
        shores.add(new AxialCoordinate(2, 3));
        shores.add(new AxialCoordinate(3, 3));
        shores.add(new AxialCoordinate(-2, 6));
        shores.add(new AxialCoordinate(-4, 5));
        shores.add(new AxialCoordinate(-3, 4));
        shores.add(new AxialCoordinate(-6, 4));
        shores.add(new AxialCoordinate(-3, 1));
        shores.add(new AxialCoordinate(-3, 0));
        shores.add(new AxialCoordinate(-1, -3));

        List<AxialCoordinate> plantations = new ArrayList<>();
        //plantations.add(new AxialCoordinate( 0,  0));
        plantations.add(new AxialCoordinate(3, 1));
        plantations.add(new AxialCoordinate(6, -5));
        plantations.add(new AxialCoordinate(1, -5));
        plantations.add(new AxialCoordinate(-5, -1));
        plantations.add(new AxialCoordinate(-5, 4));
        plantations.add(new AxialCoordinate(-1, 5));


        //board:
        Board b = g.getBoard();
        int boardDiameter = 7;
        for (int i = -boardDiameter; i <= boardDiameter; i++) {
            for (int j = -boardDiameter; j <= boardDiameter; j++) {
                AxialCoordinate c = new AxialCoordinate(i, j);
                if (c.distanceTo(0, 0) >= boardDiameter) continue;

                BoardTile tile;
                if (shores.contains(c)) {
                    tile = new Shore(c, b);
                } else if (ports.contains(c)) {
                    tile = new Port(c, b);
                } else if (plantations.contains(c)) {
                    tile = new Plantation(c, b);
                } else {
                    tile = new Sea(c, b);
                }
                b.tilesProperty().put(c, tile);

            }
        }
        b.tilesProperty().put(AxialCoordinate.ZERO, new PlantationExtra(AxialCoordinate.ZERO, b));

        return g;
    }

    //endregion
}
