package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.*;

public class Ship {

    public StringProperty name = new SimpleStringProperty("");
    public StringProperty id = new SimpleStringProperty("");
    public StringProperty teamId = new SimpleStringProperty("");
    public StringProperty captain = new SimpleStringProperty("");
    public StringProperty type = new SimpleStringProperty("");

    public IntegerProperty HP = new SimpleIntegerProperty(0);
    public IntegerProperty maxHP = new SimpleIntegerProperty(0);
    public IntegerProperty cannonCount = new SimpleIntegerProperty(0);
    public IntegerProperty speed = new SimpleIntegerProperty(0);
    public IntegerProperty positionP = new SimpleIntegerProperty(0);
    public IntegerProperty positionQ = new SimpleIntegerProperty(0);

    public BooleanProperty hasUpgradedCannon = new SimpleBooleanProperty(false);
    public BooleanProperty hasUpgradedHull = new SimpleBooleanProperty(false);
    public BooleanProperty hasUpgradedMortar = new SimpleBooleanProperty(false);
    public BooleanProperty hasUpgradeChainShot = new SimpleBooleanProperty(false);
    public BooleanProperty hasUpgradeHeavyShot = new SimpleBooleanProperty(false);
    public BooleanProperty hasUpgradeRam = new SimpleBooleanProperty(false);

    public ListProperty<String> customExtensions = new SimpleListProperty<>();


}