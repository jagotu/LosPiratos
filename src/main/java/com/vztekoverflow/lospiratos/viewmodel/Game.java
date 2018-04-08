package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.evaluator.GameEvaluator;
import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.model.ShipwreckM;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.*;
import com.vztekoverflow.lospiratos.viewmodel.logs.EventLogger;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.*;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;
import com.vztekoverflow.lospiratos.viewmodel.transitions.OnMovementsEvaluatedListener;
import com.vztekoverflow.lospiratos.viewmodel.transitions.Transition;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.*;
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
        Map<Ship, List<Transition>> transitions = evaluator.evaluateRound(roundNo);
        logger.logRoundHasEnded(roundNo);
        for(OnMovementsEvaluatedListener l : onMovementsEvaluatedListeners){
            try{
                l.OnMovementsEvaluated(transitions);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ++roundNo;
        List<OnNextRoundStartedListener> copy = new ArrayList<>(onNextRoundStartedListeners);
        for (OnNextRoundStartedListener l : copy) {
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

    private ListProperty<Ship> allShips = new SimpleListProperty<>(FXCollections.observableArrayList());
    //private UnmO<String, Ship> unmodifiableAllShips = new UnmodifiableObservableMap<>(allShips.get());
    private ObservableList<Ship> unmodifiableAllShips = FXCollections.unmodifiableObservableList(allShips);

    /**
     * The resulting List is unmodifiable and attempt to add a new ship throws an exception
     */
    public ObservableList<Ship> getAllShips() {
        return unmodifiableAllShips;
    }


    public boolean mayCreateShipWithName(String name) {
        if (name == null || name.isEmpty()) {
            Warnings.makeDebugWarning(toString(), "Ship's name is empty or null.");
            return false;
        }
        return allShips.stream().noneMatch(s -> s.getName().equalsIgnoreCase(name));
    }

    private final Board board;

    public final Board getBoard() {
        return board;
    }

    void registerShip(Ship ship) {
        if (allShips.stream().anyMatch(s -> s.getName().equalsIgnoreCase(ship.getName()))) {
            Warnings.makeStrongWarning(toString(), "allShips already contains a ship with this name: " + ship.getName());
            return;
        }
        allShips.add(ship);
        addOnNextRoundStartedListener(ship);
        board.figuresProperty().add(ship);
        //allShips.sort(Comparator.comparing(s -> s.getTeam().getName(), Comparator.naturalOrder()));
    }

    void unregisterShip(Ship s) {
            boolean removedFromBoard = board.figuresProperty().remove(s);
            if (!removedFromBoard) {
                Warnings.panic(toString() + ".unregisterShip()", "Attempt to remove a ship which is in allShips but not in board.figures: " + s.getName());
            }
            removeOnNextRoundStartedListener(s);
            allShips.remove(s);
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

    private Set<OnMovementsEvaluatedListener> onMovementsEvaluatedListeners = new HashSet<>();

    public void addOnMovementsEvaluatedListener(OnMovementsEvaluatedListener listener) {
        onMovementsEvaluatedListeners.add(listener);
    }

    public void removeOnMovementsEvaluatedListener(OnMovementsEvaluatedListener listener) {
        onMovementsEvaluatedListeners.remove(listener);
    }

    public Shipwreck createAndAddNewDefaultShipwreck() {
        return createAndAddNewShipwreck(AxialCoordinate.ZERO, ResourceReadOnly.ZERO);
    }

    public Shipwreck createAndAddNewShipwreck(AxialCoordinate position, ResourceReadOnly resource) {
        ShipwreckM model = new ShipwreckM(position);
        Shipwreck w = new Shipwreck(position, this, model);
        getGameModel().getShipwrecks().add(model);

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
        for (ShipwreckM wreckModel : gameModel.getShipwrecks()) {
            g.board.figuresProperty().add(Shipwreck.loadFrom(wreckModel, g));
            //todo ukldani a nacitani wrecku je spis zprasene, slo by ucesat
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


//    public static Game CreateNewMockGame() {
//        final int teamCount = 4; //beter do not make bigger than 5
//        int captainIdx = 0;
//        Game g = new Game();
//
//
//        //tiles position are according to the bitmap game map (by Bratr)
//
//        List<AxialCoordinate> ports = new ArrayList<>();
//        ports.add(new AxialCoordinate(-2, -3));
//        ports.add(new AxialCoordinate(2, -3));
//        ports.add(new AxialCoordinate(6, -2));
//        ports.add(new AxialCoordinate(-4, 1));
//        ports.add(new AxialCoordinate(-6, 6));
//        ports.add(new AxialCoordinate(-3, 5));
//        ports.add(new AxialCoordinate(2, 4));
//        ports.add(new AxialCoordinate(3, -6));
//
//        List<AxialCoordinate> shores = new ArrayList<>();
//        shores.add(new AxialCoordinate(2, -6));
//        shores.add(new AxialCoordinate(1, -2));
//        shores.add(new AxialCoordinate(2, -2));
//        shores.add(new AxialCoordinate(3, -3));
//        shores.add(new AxialCoordinate(5, -2));
//        shores.add(new AxialCoordinate(1, 1));
//        shores.add(new AxialCoordinate(2, 1));
//        shores.add(new AxialCoordinate(1, 5));
//        shores.add(new AxialCoordinate(3, 3));
//        shores.add(new AxialCoordinate(-4, 5));
//        shores.add(new AxialCoordinate(-3, 4));
//        shores.add(new AxialCoordinate(-6, 4));
//        shores.add(new AxialCoordinate(-3, 1));
//        shores.add(new AxialCoordinate(-3, 0));
//        shores.add(new AxialCoordinate(-1, -3));
//
//        List<AxialCoordinate> plantations = new ArrayList<>();
//        //plantations.add(new AxialCoordinate( 0,  0));
//        plantations.add(new AxialCoordinate(2, 2));
//        plantations.add(new AxialCoordinate(0, 6));
//        plantations.add(new AxialCoordinate(-5, 4));
//        plantations.add(new AxialCoordinate(-5, -1));
//        plantations.add(new AxialCoordinate(0, 0));
//        plantations.add(new AxialCoordinate(0, -6));
//        plantations.add(new AxialCoordinate(6, -5));
//        //plantations.add(new AxialCoordinate(6, -6));
//
//
//
//        //board:
//        Board b = g.getBoard();
//        int boardDiameter = 7;
//        for (int i = -boardDiameter; i <= boardDiameter; i++) {
//            for (int j = -boardDiameter; j <= boardDiameter; j++) {
//                AxialCoordinate c = new AxialCoordinate(i, j);
//                if (c.distanceTo(0, 0) >= boardDiameter) continue;
//
//                BoardTile tile;
//                if (shores.contains(c)) {
//                    tile = new Shore(c, b);
//                } else if (ports.contains(c)) {
//                    tile = new Port(c, b);
//                } else if (plantations.contains(c)) {
//                    tile = new Plantation(c, b);
//                } else if (c.equals(AxialCoordinate.ZERO)) {
//                    tile = new PlantationExtra(c, b);
//                } else {
//                    tile = new Sea(c, b);
//                }
//                b.tilesProperty().put(c, tile);
//
//            }
//        }
//
//
//        //ships' positions:
//
//        List<AxialCoordinate> team1_shipPositions = new ArrayList<>();
//        team1_shipPositions.add(new AxialCoordinate(-3, -3));
//
//        List<AxialCoordinate> team2_shipPositions = new ArrayList<>();
//        team2_shipPositions.add(new AxialCoordinate(-2, -2));
//        team2_shipPositions.add(new AxialCoordinate(-2, -1));
//
//        List<AxialCoordinate> team3_shipPositions = new ArrayList<>();
//        team3_shipPositions.add(new AxialCoordinate(0, -3));
//        team3_shipPositions.add(new AxialCoordinate(0, -2));
//        team3_shipPositions.add(new AxialCoordinate(0, -1));
//
//        List<AxialCoordinate> team4_shipPositions = new ArrayList<>();
//        team4_shipPositions.add(new AxialCoordinate(1, -3));
//        team4_shipPositions.add(new AxialCoordinate(2, -3));
//        team4_shipPositions.add(new AxialCoordinate(4, -4));
//        team4_shipPositions.add(new AxialCoordinate(6, -5));
//
//        List<AxialCoordinate> team5_shipPositions = new ArrayList<>();
//        team5_shipPositions.add(new AxialCoordinate(-3, 5));
//        team5_shipPositions.add(new AxialCoordinate(-1, 3));
//        team5_shipPositions.add(new AxialCoordinate(3, 1));
//        team5_shipPositions.add(new AxialCoordinate(6, -1));
//        team5_shipPositions.add(new AxialCoordinate(-2, 2));
//
//        List<List<AxialCoordinate>> teamsShipPositions = new ArrayList<>();
//        teamsShipPositions.add(team1_shipPositions);
//        teamsShipPositions.add(team2_shipPositions);
//        teamsShipPositions.add(team3_shipPositions);
//        teamsShipPositions.add(team4_shipPositions);
//        teamsShipPositions.add(team5_shipPositions);
//
//        //create teams:
//        for (int i = 1; i <= teamCount; i++) {
//            String name = "(" + i + ") " + teamNames[i];
//            Color c = Color.color((255 - (i - 1) * 49) / 255d, ((i - 1) * 49) / 255d, ((i - 1) * 49d) / 255d);
//            Team team = g.createAndAddNewTeam(name, c);
//            team.getOwnedResource().setMoney(50000 + i);
//            team.getOwnedResource().setCloth(100 * i);
//            team.getOwnedResource().setMetal(200 * i);
//            team.getOwnedResource().setRum(300 * i);
//            team.getOwnedResource().setWood(400 * i);
//            //Create ships:
//            for (int j = 0; j < i; j++) {
//                AxialCoordinate position = teamsShipPositions.get(i - 1).get(j);
//                //AxialCoordinate position = AxialCoordinate.ZERO;
//                name = "Tým" + i + "_Loď" + (j + 1);
//                String captain = captainNames[captainIdx++];
//                Class<ShipType> type = (Class<ShipType>) shipTypes[j % 4];
//                Ship s = team.createAndAddNewShip(type, name, captain, position);
//                s.getPosition().setRotation(60 * j * (i + 1));
//                s.getStorage().addMoney(500 * i + 10 * j);
//                s.getStorage().addCloth(10 * i + j);
//                s.getStorage().addMetal(20 * i + j);
//                s.getStorage().addRum(30 * i + j);
//                s.getStorage().addWood(40 * i + j);
//                if (i != 3) //random value
//                    s.takeDamage(6 * j);
//                for (int k = 0; k < j + i; k++) {
//                    if (k >= shipEnhancements.length) continue;
//                    Class<ShipEnhancement> enh = (Class<ShipEnhancement>) shipEnhancements[k];
//                    // s.addNewEnhancement(enh);
//                }
//                if ((position.getQ() == -2 && position.getR() == -2)) { //second ship of second team
//                    s.destroyShipAndEnhancements();
//                    s.incrementXP(); //does not make sense according to the rules, is for test only
//                }
//            }
//        }
//
//        List<AxialCoordinate> shipwrecks = new ArrayList<>();
//        shipwrecks.add(new AxialCoordinate(-2, 0));
//        shipwrecks.add(new AxialCoordinate(3, 0));
//        shipwrecks.add(new AxialCoordinate(5, 0));
//        shipwrecks.add(new AxialCoordinate(2, -4));
//
//        ResourceReadOnly r = new ResourceReadOnly(100, 10, 10, 10, 10, 10);
//        for (AxialCoordinate c : shipwrecks) {
//            g.createAndAddNewShipwreck(c, r);
//        }
//
//        return g;
//    }

    //endregion


    public static Game CreateNewMockGame() {
        final int teamCount = 4;
        Game g = new Game();


        //tiles position are according to the bitmap game map (by Bratr)

        List<AxialCoordinate> ports = new ArrayList<>();
        ports.add(new AxialCoordinate(-2, -3));
        ports.add(new AxialCoordinate(2, -3));
        ports.add(new AxialCoordinate(6, -2));
        ports.add(new AxialCoordinate(-4, 1));
        ports.add(new AxialCoordinate(-6, 6));
        ports.add(new AxialCoordinate(-3, 5));
        ports.add(new AxialCoordinate(2, 4));
        ports.add(new AxialCoordinate(3, -6));

        List<AxialCoordinate> shores = new ArrayList<>();
        shores.add(new AxialCoordinate(2, -6));
        shores.add(new AxialCoordinate(1, -2));
        shores.add(new AxialCoordinate(2, -2));
        shores.add(new AxialCoordinate(3, -3));
        shores.add(new AxialCoordinate(5, -2));
        shores.add(new AxialCoordinate(1, 1));
        shores.add(new AxialCoordinate(2, 1));
        shores.add(new AxialCoordinate(1, 5));
        shores.add(new AxialCoordinate(3, 3));
        shores.add(new AxialCoordinate(-4, 5));
        shores.add(new AxialCoordinate(-3, 4));
        shores.add(new AxialCoordinate(-6, 4));
        shores.add(new AxialCoordinate(-3, 1));
        shores.add(new AxialCoordinate(-3, 0));
        shores.add(new AxialCoordinate(-1, -3));

        List<AxialCoordinate> plantations = new ArrayList<>();
        //plantations.add(new AxialCoordinate( 0,  0));
        plantations.add(new AxialCoordinate(2, 2));
        plantations.add(new AxialCoordinate(0, 6));
        plantations.add(new AxialCoordinate(-5, 4));
        plantations.add(new AxialCoordinate(-5, -1));
        //plantations.add(new AxialCoordinate(0, 0));
        plantations.add(new AxialCoordinate(0, -6));
        plantations.add(new AxialCoordinate(6, -5));
        //plantations.add(new AxialCoordinate(6, -6));


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
                } else if (c.equals(AxialCoordinate.ZERO)) {
                    tile = new PlantationExtra(c, b);
                } else {
                    tile = new Sea(c, b);
                }
                b.tilesProperty().put(c, tile);

            }
        }


        //ships' positions:
        List<AxialCoordinate> team_blue_shipPositions = new ArrayList<>();
        team_blue_shipPositions.add(new AxialCoordinate(3, -6));
        team_blue_shipPositions.add(new AxialCoordinate(2, -3));

        List<AxialCoordinate> team_red_shipPositions = new ArrayList<>();
        team_red_shipPositions.add(new AxialCoordinate(-2, -3));
        team_red_shipPositions.add(new AxialCoordinate(-4, 1));

        List<AxialCoordinate> team_green_shipPositions = new ArrayList<>();
        team_green_shipPositions.add(new AxialCoordinate(-3, 5));
        team_green_shipPositions.add(new AxialCoordinate(-6, 6));

        List<AxialCoordinate> team_yellow_shipPositions = new ArrayList<>();
        team_yellow_shipPositions.add(new AxialCoordinate(6, -2));
        team_yellow_shipPositions.add(new AxialCoordinate(2, 4));

        List<List<AxialCoordinate>> teamsShipPositions = new ArrayList<>();
        teamsShipPositions.add(team_blue_shipPositions);
        teamsShipPositions.add(team_red_shipPositions);
        teamsShipPositions.add(team_green_shipPositions);
        teamsShipPositions.add(team_yellow_shipPositions);

        List<Color> teamColors = new ArrayList<>();
        teamColors.add(Color.color(0.4,0.4,1));
        teamColors.add(Color.color(1,0.3,0.3));
        teamColors.add(Color.color(0.3,1,0.3));
        teamColors.add(Color.color(1,1,0.2));

        //create teams:
        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= teamCount; i++) {
            String name = "Tým " + i;
            Color c = teamColors.get(i-1);
            Team team = g.createAndAddNewTeam(name, c);
            teams.add(team);
            team.getOwnedResource().setMoney(500);
            team.getOwnedResource().setCloth(50);
            team.getOwnedResource().setMetal(0);
            team.getOwnedResource().setRum(0);
            team.getOwnedResource().setWood(50);
        }

        //Create ships:
        AxialCoordinate positionSchooner = teamsShipPositions.get(0).get(0);
        String nameSchooner = "Tým" + 1 + "_Loď1";
        String captainSchooner = "Kapitán1" + "_Tým" + 1;
        Ship schooner = teams.get(0).createAndAddNewShip(Schooner.class, nameSchooner, captainSchooner, positionSchooner);
        schooner.getPosition().setRotation(180);

        AxialCoordinate positionBrig = teamsShipPositions.get(0).get(1);
        String nameBrig = "Tým" + 1 + "_Loď2";
        String captainBrig = "Kapitán2" + "_Tým" + 1;
        Ship brig = teams.get(0).createAndAddNewShip(Brig.class, nameBrig, captainBrig, positionBrig);
        brig.getPosition().setRotation(300);


        positionSchooner = teamsShipPositions.get(1).get(0);
        nameSchooner = "Tým" + 2 + "_Loď1";
        captainSchooner = "Kapitán1" + "_Tým" + 2;
        schooner = teams.get(1).createAndAddNewShip(Schooner.class, nameSchooner, captainSchooner, positionSchooner);
        schooner.getPosition().setRotation(120);

        positionBrig = teamsShipPositions.get(1).get(1);
        nameBrig = "Tým" + 2 + "_Loď2";
        captainBrig = "Kapitán2" + "_Tým" + 2;
        brig = teams.get(1).createAndAddNewShip(Brig.class, nameBrig, captainBrig, positionBrig);
        brig.getPosition().setRotation(300);


        positionSchooner = teamsShipPositions.get(2).get(0);
        nameSchooner = "Tým" + 3 + "_Loď1";
        captainSchooner = "Kapitán1" + "_Tým" + 3;
        schooner = teams.get(2).createAndAddNewShip(Schooner.class, nameSchooner, captainSchooner, positionSchooner);
        schooner.getPosition().setRotation(60);

        positionBrig = teamsShipPositions.get(2).get(1);
        nameBrig = "Tým" + 3 + "_Loď2";
        captainBrig = "Kapitán2" + "_Tým" + 3;
        brig = teams.get(2).createAndAddNewShip(Brig.class, nameBrig, captainBrig, positionBrig);
        brig.getPosition().setRotation(60);


        positionSchooner = teamsShipPositions.get(3).get(0);
        nameSchooner = "Tým" + 4 + "_Loď1";
        captainSchooner = "Kapitán1" + "_Tým" + 4;
        schooner = teams.get(3).createAndAddNewShip(Schooner.class, nameSchooner, captainSchooner, positionSchooner);
        schooner.getPosition().setRotation(300);

        positionBrig = teamsShipPositions.get(3).get(1);
        nameBrig = "Tým" + 4 + "_Loď2";
        captainBrig = "Kapitán2" + "_Tým" + 4;
        brig = teams.get(3).createAndAddNewShip(Brig.class, nameBrig, captainBrig, positionBrig);
        brig.getPosition().setRotation(300);

        return g;
    }

    //endregion

}
