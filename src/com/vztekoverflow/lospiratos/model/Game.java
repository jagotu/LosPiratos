package com.vztekoverflow.lospiratos.model;


import com.vztekoverflow.lospiratos.util.AxialDirection;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;


public class Game {

    private ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Map> map = new SimpleObjectProperty<>();

    public ObservableList<Team> getTeams() {
        return teams.get();
    }

    public ListProperty<Team> teamsProperty() {
        return teams;
    }

    public Map getMap() {
        return map.get();
    }

    public ObjectProperty<Map> mapProperty() {
        return map;
    }



    public static Game CreateNewMockGame() {
        Game g = new Game();
        Ship s1 = new Ship(
                "Ship1",
                "teamId1",
                "Captain1",
                "Schooner",
                15,
                30,
                8,
                4,
                0,
                -1,
                60,
                ShipEnhancementStatus.destroyed,
                ShipEnhancementStatus.empty,
                ShipEnhancementStatus.empty,
                ShipEnhancementStatus.empty,
                ShipEnhancementStatus.empty,
                ShipEnhancementStatus.empty,
                40,
                1,
                0,
                1,
                0,
                2,
                5
                );

        Ship s2 = new Ship(
                "Ship2",
                "teamId1",
                "Captain2",
                "Galleon",
                75,
                100,
                30,
                2,
                1,
                0,
                150,
                ShipEnhancementStatus.empty,
                ShipEnhancementStatus.destroyed,
                ShipEnhancementStatus.active,
                ShipEnhancementStatus.active,
                ShipEnhancementStatus.active,
                ShipEnhancementStatus.active,
                1000,
                20,
                30,
                40,
                20,
                10,
                10000 );
        s2.activeMechanicsProperty().set(FXCollections.observableArrayList());
        s2.activeMechanicsProperty().add(ShipMechanics.chained);
        s2.customExtensionsProperty().set(FXCollections.observableMap(new HashMap<>()));
        s2.customExtensionsProperty().put("MortarDmg","5");
        s2.customExtensionsProperty().put("MortarRange","2");


        Team t1 = new Team(
                "Los Bratros în trikos",
                "#70a3f4",
                5000,
                10,
                20,
                0,
                30,
                40);

        t1.shipsProperty().addAll(s1, s2);

        g.teams.add(t1);




        Map m = new Map();
        MapHexagon center = new MapHexagon(0, 0, "shipwreck");
        center.customExtensionsProperty().set(FXCollections.observableMap(new HashMap<>()));
        center.customExtensionsProperty().put("gain","40");
        MapHexagon port = new MapHexagon(AxialDirection.FlatDown, "port");
        port.customExtensionsProperty().set(FXCollections.observableMap(new HashMap<>()));
        port.customExtensionsProperty().put("name","Port Royale");
        m.hexagonsProperty().addAll(
                center,
                new MapHexagon(AxialDirection.FlatUp, "sea"),
                new MapHexagon(AxialDirection.FlatRightUp, "sea"),
                new MapHexagon(AxialDirection.FlatRightDown, "sea"),
                port,
                new MapHexagon(AxialDirection.FlatLeftDown, "shore"),
                new MapHexagon(AxialDirection.FlatLeftUp, "shore")
        );
        m.backgroundColorProperty().set("#d6d6d6");
        g.map.setValue(m);

        return g;
    }


}
