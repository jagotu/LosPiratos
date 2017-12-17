package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.*;


public class Ship {
    public Ship(String name, String id, String teamId, String captain, String type, int HP, int maxHP, int cannonCount, int speed, int positionP, int positionQ, boolean hasUpgradedCannon, boolean hasUpgradedHull, boolean hasUpgradedMortar, boolean hasUpgradeChainShot, boolean hasUpgradeHeavyShot, boolean hasUpgradeRam) {
        this.name.set(name);
        this.id.set(id);
        this.teamId.set(teamId);
        this.captain.set(captain);
        this.type.set(type);
        this.HP.set(HP);
        this.maxHP.set(maxHP);
        this.cannonCount.set(cannonCount);
        this.speed.set(speed);
        this.positionP.set(positionP);
        this.positionQ.set(positionQ);
        this.hasUpgradedCannon.set(hasUpgradedCannon);
        this.hasUpgradedHull.set(hasUpgradedHull);
        this.hasUpgradedMortar.set(hasUpgradedMortar);
        this.hasUpgradeChainShot.set(hasUpgradeChainShot);
        this.hasUpgradeHeavyShot.set(hasUpgradeHeavyShot);
        this.hasUpgradeRam.set(hasUpgradeRam);
    }

    public Ship() {

    }

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

    public MapProperty<String, String> customExtensions = new SimpleMapProperty<>();


}