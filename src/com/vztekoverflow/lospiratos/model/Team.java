package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Team {
    public Team() {

    }
    public Team(String name, String color, int money, int ownedMetal, int ownedWood, int ownedCloth, int ownedRum, int ownedTobacco) {

        this.name.set(name);
        this.color.set(color);
        this.money.set(money);
        this.ownedMetal.set(ownedMetal);
        this.ownedWood.set(ownedWood);
        this.ownedCloth.set(ownedCloth);
        this.ownedRum.set(ownedRum);
        this.ownedTobacco.set(ownedTobacco);
    }

    private StringProperty name = new SimpleStringProperty("");
    private StringProperty id = new SimpleStringProperty(""); //useless for now
    private StringProperty color = new SimpleStringProperty("");

    private IntegerProperty money = new SimpleIntegerProperty(0);
    private IntegerProperty ownedMetal = new SimpleIntegerProperty(0);
    private IntegerProperty ownedWood = new SimpleIntegerProperty(0);
    private IntegerProperty ownedCloth = new SimpleIntegerProperty(0);
    private IntegerProperty ownedRum = new SimpleIntegerProperty(0);
    private IntegerProperty ownedTobacco = new SimpleIntegerProperty(0);

    private ListProperty<Ship> ships = new SimpleListProperty<>(FXCollections.observableArrayList());

    //getters:

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public int getMoney() {
        return money.get();
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public int getOwnedMetal() {
        return ownedMetal.get();
    }

    public IntegerProperty ownedMetalProperty() {
        return ownedMetal;
    }

    public int getOwnedWood() {
        return ownedWood.get();
    }

    public IntegerProperty ownedWoodProperty() {
        return ownedWood;
    }

    public int getOwnedCloth() {
        return ownedCloth.get();
    }

    public IntegerProperty ownedClothProperty() {
        return ownedCloth;
    }

    public int getOwnedRum() {
        return ownedRum.get();
    }

    public IntegerProperty ownedRumProperty() {
        return ownedRum;
    }

    public int getOwnedTobacco() {
        return ownedTobacco.get();
    }

    public IntegerProperty ownedTobaccoProperty() {
        return ownedTobacco;
    }

    public ObservableList<Ship> getShips() {
        return ships.get();
    }

    public ListProperty<Ship> shipsProperty() {
        return ships;
    }
}
