package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Game {

    private ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final Map map = new Map();

    public ObservableList<Team> getTeams() {
        return teams.get();
    }

    public ListProperty<Team> teamsProperty() {
        return teams;
    }

    public final Map getMap() {
        return map;
    }

}
