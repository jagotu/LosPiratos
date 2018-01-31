package com.vztekoverflow.lospiratos.viewmodel;

import com.sun.javafx.collections.UnmodifiableObservableMap;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks.CannonsAbstractVolley;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks.FrontalAssault;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Attacks.MortarShot;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.Actions.PerformableAction;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.BoardTiles.Port;
import com.vztekoverflow.lospiratos.viewmodel.BoardTiles.Sea;
import com.vztekoverflow.lospiratos.viewmodel.BoardTiles.Shipwreck;
import com.vztekoverflow.lospiratos.viewmodel.BoardTiles.Shore;
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

import java.util.List;
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
    public Team createAndAddNewDefaultTeam(){
        Team t = null;
        while (t == null){
            String name = "Tým #" + ++createAndAddNewDefaultTeamCounter;
            t = createAndAddNewTeam(name, Color.BLACK);
        }
        return  t;

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


    //region evaluate
    private int roundNo = 0;
    public void closeRoundAndEvaluate(){
        //zavolat agenty
        evaluateVolleys();
        evaluate(Maneuver.class);
        evaluate(MortarShot.class);
        evaluate(Transaction.class);
        ++roundNo;
        for(Ship s : getAllShips().values()){
            s.onNextRoundStarted(roundNo);
        }
    }
    private void evaluateVolleys(){
        for(Ship s : allShips.values()){
            Position oldPosition = s.getPosition().createCopy();
            for(PerformableAction a : s.getPlannedActions()){
                if(a instanceof Maneuver || a instanceof CannonsAbstractVolley || a instanceof FrontalAssault){
                    a.performOnTarget();
                }
            }
            s.getPosition().setFrom(oldPosition);
        }
    }
    private void evaluate(Class<? extends Action> action){
        for(Ship s : allShips.values()){
            for(PerformableAction a : s.getPlannedActions()){
                if(action.isAssignableFrom(a.getClass())){
                    a.performOnTarget();
                }
            }
        }
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
    public UnmodifiableObservableMap<String,Ship> getAllShips() {
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
        board.figuresProperty().add(ship);
    }

    void unregisterShip(String shipName) {
        if (allShips.containsKey(shipName)){
            boolean removedFromBoard = board.figuresProperty().remove(allShips.get(shipName));
            if(!removedFromBoard){
                Warnings.panic(toString() + ".unregisterShip()","Attempt to remove a ship which is in allShips but not in board.figures: " + shipName);
            }
            allShips.remove(shipName);
        }
        else {
            Warnings.makeStrongWarning(toString() + ".unregisterShip()", "Attempt to remove a ship whose name is unknown: " + shipName);
        }
    }
    public void deleteShip(Ship s){
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

    public static Game LoadFromModel(com.vztekoverflow.lospiratos.model.Game gameModel) {
        Game g = new Game(gameModel);
        for (com.vztekoverflow.lospiratos.model.Team teamModel : gameModel.getTeams()) {
            g.addNewTeamFromModel(teamModel);
        }
        return g;
    }

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
    };
    private static Class<?>[] shipTypes = {
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
        final int teamCount = 6; //beter do not make bigger than 6
        int captainIdx = 0;
        Game g = new Game();

        //teams:
        for (int i = 1; i <= teamCount; i++) {
            String name = "(" + i + ") " + teamNames[i];
            Color c = Color.color((255 - (i - 1) * 49) / 255d, (255 - (i - 1) * 49) / 255d, ((i - 1) * 49d) / 255d);
            Team team = g.createAndAddNewTeam(name, c);
            team.getOwnedResource().setMoney(5000 + i);
            team.getOwnedResource().setCloth(10 * i);
            team.getOwnedResource().setMetal(20 * i);
            team.getOwnedResource().setRum(30 * i);
            team.getOwnedResource().setWood(40 * i);
            //Create ships:
            for (int j = 0; j < i; j++) {
                name = "Tým" + i + "_Loď" + j;
                String captain = captainNames[captainIdx++];
                Class<ShipType> type = (Class<ShipType>) shipTypes[j % 4];
                AxialCoordinate position = new AxialCoordinate(i - teamCount/2, j - teamCount/2);
                Ship s = team.createAndAddNewShip(type, name, captain, position);
                s.getPosition().setRotation(60*j);
                s.getStorage().addMoney(500 * i + 10 * j);
                s.getStorage().addCloth(10 * i + j);
                s.getStorage().addMetal(20 * i + j);
                s.getStorage().addRum(30 * i + j);
                s.getStorage().addWood(40 * i + j);
                if (i != 3) //random value
                    s.takeDamage(8 * j);
                for (int k = 0; k < j; k++) {
                    Class<ShipEnhancement> enh = (Class<ShipEnhancement>) shipEnhancements[k];
                    s.addNewEnhancement(enh);
                }
                if ((i == 3 && j == 2) || (i == 5 && j == 4)) { //random values
                    s.destroyShipAndEnhancements();
                }
                if (i == 2) { //random value
                    s.destroyShipAndEnhancements();
                    s.repairShip();
                }
            }
        }

        //board:
        Board b = g.getBoard();
        int boardDiameter = 8;
        for (int i = -boardDiameter; i <= boardDiameter; i++) {
            for (int j = -boardDiameter; j <= boardDiameter; j++) {
                AxialCoordinate c = new AxialCoordinate(i, j);
                if(c.distanceTo(0,0) >= boardDiameter) continue;
                BoardTile tile;
                if (c.distanceTo(0,0) >= boardDiameter -1) { //random value
                    tile = new Shore(c);
                } else if (i == 1 && j == 1) { //random value
                    tile = new Shipwreck(c);
                } else if (i == -6 && j == 0) { //random value
                    tile = new Port(c);
                }else {
                    tile = new Sea(c);
                }
                b.tilesProperty().put(c, tile);
            }
        }

        return g;
    }
}
