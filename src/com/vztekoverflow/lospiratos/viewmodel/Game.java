package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.Warnings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {


    private com.vztekoverflow.lospiratos.model.Game gameModel;


    public Game() {
        this.gameModel = new com.vztekoverflow.lospiratos.model.Game();
        //todo proper binding of teams ListProperty missing (Github issue #7)
    }

    public Team createAndAddNewTeam(String teamName, Color teamColor){
        com.vztekoverflow.lospiratos.model.Team teamModel = new com.vztekoverflow.lospiratos.model.Team();
        Team t = new Team(this, teamName, teamColor, teamModel);

        gameModel.teamsProperty().add(teamModel);
        teams.add(t); //todo this should be done via a callback from model (Github issue #7)
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
        Game g = new Game();
        g.gameModel = gameModel;
        for(com.vztekoverflow.lospiratos.model.Team teamModel : gameModel.getTeams()){
            Team t = new Team(g, teamModel);
            g.teams.add(t);
        }
        return g;
    }
}
