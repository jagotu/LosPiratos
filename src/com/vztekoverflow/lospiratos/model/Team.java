package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Team {
    public Team() {

    }


    public Team(String name, String id, String color, int money, int ownedMetal, int ownedWood, int ownedCloth, int ownedRum, int ownedTobacco) {

        this.name.set(name);
        this.id.set(id);
        this.color.set(color);
        this.money.set(money);
        this.ownedMetal.set(ownedMetal);
        this.ownedWood.set(ownedWood);
        this.ownedCloth.set(ownedCloth);
        this.ownedRum.set(ownedRum);
        this.ownedTobacco.set(ownedTobacco);
    }

    public StringProperty name = new SimpleStringProperty("");
    public StringProperty id = new SimpleStringProperty("");
    public StringProperty color = new SimpleStringProperty("");

    public IntegerProperty money = new SimpleIntegerProperty(0);
    public IntegerProperty ownedMetal = new SimpleIntegerProperty(0);
    public IntegerProperty ownedWood = new SimpleIntegerProperty(0);
    public IntegerProperty ownedCloth = new SimpleIntegerProperty(0);
    public IntegerProperty ownedRum = new SimpleIntegerProperty(0);
    public IntegerProperty ownedTobacco = new SimpleIntegerProperty(0);

    public ListProperty<Ship> ships = new SimpleListProperty<>(FXCollections.observableArrayList());
}
