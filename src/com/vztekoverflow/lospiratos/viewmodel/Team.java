package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Team {

    public static final int INITIAL_MONEY = 500;

    private com.vztekoverflow.lospiratos.model.Team teamModel;
    private Game game;

    public Team(Game game, com.vztekoverflow.lospiratos.model.Team teamModel) {
        this.teamModel = teamModel;
        this.game = game;

        ownedTobacco.bindBidirectional(teamModel.ownedTobaccoProperty());
        ownedRum.bindBidirectional(teamModel.ownedRumProperty());
        ownedCloth.bindBidirectional(teamModel.ownedClothProperty());
        ownedWood.bindBidirectional(teamModel.ownedWoodProperty());
        ownedMetal.bindBidirectional(teamModel.ownedMetalProperty());
        money.bindBidirectional(teamModel.moneyProperty());

        name.bindBidirectional(teamModel.nameProperty());

        teamModel.colorProperty().addListener((observable, oldValue, newValue) -> trySettingColor(newValue));
        trySettingColor(teamModel.getColor());

        teamModel.shipsProperty().addListener((observable, oldValue, newValue) -> {
            //todo how to make this? I want add new ships or remove removed ships, but do not change currently existing ship object (don't want to recreate them)
            Warnings.makeWarning(toString(), "NotImplemented: Team.shipsProperty.changedListener.");
        });
        loadShipsFromModel(teamModel.getShips());

    }
    private void loadShipsFromModel(List<com.vztekoverflow.lospiratos.model.Ship> ships){
        for(com.vztekoverflow.lospiratos.model.Ship modelShip: ships){
            Ship s = new Ship(modelShip);
            this.ships.add(s);
        }
    }

    //properties:

    private ListProperty<Ship> ships = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty name = new SimpleStringProperty("");
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();

    private IntegerProperty money = new SimpleIntegerProperty(0);
    private IntegerProperty ownedMetal = new SimpleIntegerProperty(0);
    private IntegerProperty ownedWood = new SimpleIntegerProperty(0);
    private IntegerProperty ownedCloth = new SimpleIntegerProperty(0);
    private IntegerProperty ownedRum = new SimpleIntegerProperty(0);
    private IntegerProperty ownedTobacco = new SimpleIntegerProperty(0);

    public ObservableList<Ship> getShips() {
        return ships.get();
    }

    public ListProperty<Ship> shipsProperty() {
        return ships;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color color) {
        teamModel.colorProperty().set(FxUtils.toRGBCode(color));
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public int getMoney() {
        return money.get();
    }

    public void addMoney(int value) {
        money.set(money.get() + value);
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public int getOwnedMetal() {
        return ownedMetal.get();
    }

    public void addOwnedMetal(int value) {
        ownedMetal.set(ownedMetal.get() + value);
    }

    public IntegerProperty ownedMetalProperty() {
        return ownedMetal;
    }

    public int getOwnedWood() {
        return ownedWood.get();
    }

    public void addOwnedWood(int value) {
        ownedWood.set(ownedWood.get() + value);
    }

    public IntegerProperty ownedWoodProperty() {
        return ownedWood;
    }

    public int getOwnedCloth() {
        return ownedCloth.get();
    }

    public void addOwnedCloth(int value) {
        ownedCloth.set(ownedCloth.get() + value);
    }

    public IntegerProperty ownedClothProperty() {
        return ownedCloth;
    }

    public int getOwnedRum() {
        return ownedRum.get();
    }

    public void addOwnedRum(int value) {
        ownedRum.set(ownedRum.get() + value);
    }

    public IntegerProperty ownedRumProperty() {
        return ownedRum;
    }

    public int getOwnedTobacco() {
        return ownedTobacco.get();
    }

    public void addOwnedTobacco(int value) {
        ownedTobacco.set(ownedTobacco.get() + value);
    }

    public IntegerProperty ownedTobaccoProperty() {
        return ownedTobacco;
    }


    //public methods:

    public <T extends ShipType> Ship createAndAddNewShip(Class<T> shipType, String shipName, String captainName) {
        com.vztekoverflow.lospiratos.model.Ship modelShip = new com.vztekoverflow.lospiratos.model.Ship();
        Ship s = new Ship(modelShip);
        s.setShipType(shipType);
        s.setName(shipName);
        s.setCaptainName(captainName);
        s.addToCurrentHP(s.getMaxHP());
        teamModel.shipsProperty().add(modelShip);
        return s;
    }

    //private methods:
    private void trySettingColor(String color) {
        try {
            this.color.set(Color.web(color));
        } catch (IllegalArgumentException | NullPointerException e) {
            Warnings.makeWarning("Ship \"" + name.get() + "\"", "Invalid color: " + color);
        }
    }

}
