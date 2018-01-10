package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private com.vztekoverflow.lospiratos.model.Game gameModel;

    public Game(){
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
    }
    private void addNewTeamFromModel(com.vztekoverflow.lospiratos.model.Team teamModel){
        if(teams.stream().anyMatch(p -> p.getName().equalsIgnoreCase(teamModel.getName()))){
            Warnings.makeWarning(toString(),"Attempt to create a team with a name that is already used: " + teamModel.getName());
            return;
        }
        Team t = new Team(this, teamModel);
        teams.add(t);
    }

    /*
     * @returns null if team with this name (case insensitive) already exists
     */
    public Team createAndAddNewTeam(String teamName, Color teamColor){
        if(teamName == null || teamName.isEmpty()){
            Warnings.makeWarning(toString(), "Attempt to create a team with null or empty name.");
            return null;
        }
        if(teams.stream().anyMatch(p -> p.getName().equalsIgnoreCase(teamName))){
            Warnings.makeWarning(toString(),"Attempt to create a team with a name that is already used: " + teamName);
            return null;
        }
        com.vztekoverflow.lospiratos.model.Team teamModel = new com.vztekoverflow.lospiratos.model.Team();
        teamModel.nameProperty().set(teamName);
        teamModel.colorProperty().set(FxUtils.toRGBCode(teamColor));
        gameModel.teamsProperty().add(teamModel);
        //at this place, gameModel.teamsProperty's change calls my observer
        //   which then adds the team to this game's collection (if valid)
        Team t = findTeamByName(teamName);
        if(t == null) return null; //this means the model was somehow invalid
        t.initialize();
        return t;
    }
    public com.vztekoverflow.lospiratos.model.Game getGameModel() {
        return gameModel;
    }



    private ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ObservableList<Team> getTeams() {
        return teams.get();
    }
    public ListProperty<Team> teamsProperty() {
        return teams;
    }

    private MapProperty<String, Ship> allShips = new SimpleMapProperty<>(FXCollections.observableHashMap());
    public Collection<Ship> getAllShips() {
        return allShips.get().values();
    }
    public MapProperty<String, Ship> allShipsProperty() {
        return allShips;
    }

    public boolean mayCreateShipWithName(String name){
        if (name == null || name.isEmpty()) {
            Warnings.makeDebugWarning(toString(), "Ship's name is empty or null.");
            return false;
        }
        return ! allShips.containsKey(name);
    }
    public void registerShip(Ship ship){
        if(allShips.containsKey(ship.getName())){
            Warnings.makeStrongWarning(toString(), "allShips already contains a ship with this name: " + ship.getName());
            return;
        }
        allShips.put(ship.getName(), ship);
    }
    public void unregisterShip(String shipName){
        if(allShips.containsKey(shipName))
            allShips.remove(shipName);
        else{
            Warnings.makeStrongWarning(toString()+".unregisterShip()", "Attempt to remove a ship whose name is unknown: " + shipName);
        }
    }

    /*
     * @returns null when no team with the name has been found
     */
    public Team findTeamByName(String teamName) {
        //this is a O(N) implementation. But there are just up to 10 teams in our game anyway...
        List<Team> result = getTeams().stream().filter(t -> t.getName().equals(teamName)).collect(Collectors.toList());
        int size = result.size();
        if(size == 0){
            Warnings.makeWarning(toString(), "No team with this name found: " + teamName);
            return  null;
        }
        if(size > 1) Warnings.panic(toString(), "More teams (" + size+ ") with the same name: " + teamName);
        return result.get(0);
    }

    public static Game LoadFromModel(com.vztekoverflow.lospiratos.model.Game gameModel){
        Game g = new Game(gameModel);
        for(com.vztekoverflow.lospiratos.model.Team teamModel : gameModel.getTeams()){
            g.addNewTeamFromModel(teamModel);
        }
        return g;
    }
}
