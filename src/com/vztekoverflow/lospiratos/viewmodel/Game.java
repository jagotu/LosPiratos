package com.vztekoverflow.lospiratos.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Game {


    private com.vztekoverflow.lospiratos.model.Game gameModel;


    public Game() {
        this.gameModel = new com.vztekoverflow.lospiratos.model.Game();
    }

    public Team createAndAddNewTeam(String teamName, Color teamColor){
        com.vztekoverflow.lospiratos.model.Team teamModel = new com.vztekoverflow.lospiratos.model.Team();
        Team t = new Team(this, teamModel);
        t.setColor(teamColor);
        t.setName(teamName);
        t.moneyProperty().set(Team.INITIAL_MONEY);
        gameModel.teamsProperty().add(teamModel);
        teams.add(t);
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
