package com.vztekoverflow.lospiratos.viewmodel;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Game {


    private com.vztekoverflow.lospiratos.model.Game gameModel;

    public Game() {
        this.gameModel = new com.vztekoverflow.lospiratos.model.Game();
    }

    public void addNewTeam(String name){
        com.vztekoverflow.lospiratos.model.Team teamModel = new com.vztekoverflow.lospiratos.model.Team();
        Team t = new Team(teamModel);
        gameModel.teams.add(teamModel);
        teams.add(t);
    }

    private List<Team> teams = new ArrayList<>();

    public static Game LoadFromModel(com.vztekoverflow.lospiratos.model.Game gameModel){
        Game g = new Game();
        g.gameModel = gameModel;
        throw new NotImplementedException();
    }
}
