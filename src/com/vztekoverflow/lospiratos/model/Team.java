package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Team {
    public StringProperty name = new SimpleStringProperty("");
    public StringProperty id = new SimpleStringProperty("");
    public StringProperty color = new SimpleStringProperty("");

    public IntegerProperty money = new SimpleIntegerProperty(0);
    public IntegerProperty ownedMetal = new SimpleIntegerProperty(0);
    public IntegerProperty ownedWood = new SimpleIntegerProperty(0);
    public IntegerProperty ownedCloth = new SimpleIntegerProperty(0);
    public IntegerProperty ownedRum = new SimpleIntegerProperty(0);
    public IntegerProperty ownedTobacco = new SimpleIntegerProperty(0);

}
