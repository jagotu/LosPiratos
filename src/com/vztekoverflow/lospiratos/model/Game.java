package com.vztekoverflow.lospiratos.model;


import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.AxialDirection;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.HashMap;


public class Game {
    public ListProperty<Ship> ships = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ObjectProperty<Map> map = new SimpleObjectProperty<>();


    public static Game CreateNewMockGame() {
        Game g = new Game();
        Ship s1 = new Ship(
                "Ship1",
                "shipId1",
                "teamId1",
                "Captain1",
                "Schooner",
                15,
                30,
                8,
                4,
                0,
                -1,
                true,
                true,
                false,
                false,
                false,
                true);

        Ship s2 = new Ship(
                "Ship2",
                "shipId2",
                "teamId1",
                "Captain2",
                "Galleon",
                75,
                100,
                30,
                2,
                1,
                0,
                false,
                false,
                true,
                true,
                true,
                true);
        g.ships.addAll(s1, s2);
        Team t1 = new Team(
                "SuperTeam",
                "teamId1",
                "#70a3f4",
                5000,
                10,
                20,
                0,
                30,
                40);
        g.teams.add(t1);

        Map m = new Map();
        MapHexagon center = new MapHexagon(0, 0, "shipwreck");
        center.customExtensions.set(FXCollections.observableMap(new HashMap<>()));
        center.customExtensions.put("gain","40");
        MapHexagon port = new MapHexagon(AxialDirection.FlatDown, "port");
        port.customExtensions.set(FXCollections.observableMap(new HashMap<>()));
        port.customExtensions.put("name","Port Royale");
        m.hexagons.addAll(
                center,
                new MapHexagon(AxialDirection.FlatUp, "sea"),
                new MapHexagon(AxialDirection.FlatRightUp, "sea"),
                new MapHexagon(AxialDirection.FlatRightDown, "sea"),
                port,
                new MapHexagon(AxialDirection.FlatLeftDown, "shore"),
                new MapHexagon(AxialDirection.FlatLeftUp, "shore")
        );
        m.backgroundColor.set("#d6d6d6");
        g.map.setValue(m);

        return g;
    }


}
