package com.vztekoverflow.lospiratos.webapp;

import com.vztekoverflow.lospiratos.model.Game;
import com.vztekoverflow.lospiratos.model.ResourceM;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnrichedGame {
    public Game game;
    public List<ExtendedShipDetails> extendedShipDetails;
    public int roundNo;

    public static class ExtendedShipDetails
    {
        public String shipId;
        public int maxHP;
        public int cannonsCount;
        public int cargoCapacity;
        public int speed;

        public ExtendedShipDetails(Ship ship) {
            this.shipId = ship.getShipModel().getId();
            this.maxHP = ship.getMaxHP();
            this.cannonsCount = ship.getCannonsCount();
            this.cargoCapacity = ship.getStorage().getCapacityMaximum();
            this.speed = ship.getSpeed();


        }
    }


    public EnrichedGame(com.vztekoverflow.lospiratos.viewmodel.Game game) {
        this.game = game.getGameModel();

        extendedShipDetails = game.getAllShips().stream().map(ExtendedShipDetails::new).collect(Collectors.toList());
        this.roundNo = game.getRoundNo();


    }
}
