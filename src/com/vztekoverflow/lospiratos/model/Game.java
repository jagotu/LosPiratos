package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Game {
    public ListProperty<Ship> ships = new SimpleListProperty<>();
    public ListProperty<Team> teams = new SimpleListProperty<>();
    public ObjectProperty<Map> map = new SimpleObjectProperty<>();
}
