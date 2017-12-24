package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Team {
    List<Ship> ships = new ArrayList<>();
    private com.vztekoverflow.lospiratos.model.Team teamModel;
    Game game;

    public Team(Game g, com.vztekoverflow.lospiratos.model.Team teamModel) {
        this.teamModel = teamModel;
        this.game = game;
    }

    public void createNewShip(ShipType shipType, String shipName, String captainName){
        com.vztekoverflow.lospiratos.model.Ship modelShip = new com.vztekoverflow.lospiratos.model.Ship();
        Ship s = new Ship(shipType, modelShip);
        s.setName(shipName);
        s.setCaptainName(captainName);
        teamModel.ships.add(modelShip);
    }

}
