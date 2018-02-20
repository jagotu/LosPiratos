package com.vztekoverflow.lospiratos.model;


import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;


public class Ship {
        public Ship() {

    }

    //properties:

    private StringProperty name = new SimpleStringProperty("");
    private StringProperty id = new SimpleStringProperty(""); //useless for now
    private StringProperty teamId = new SimpleStringProperty("");
    private StringProperty captain = new SimpleStringProperty("");
    private StringProperty type = new SimpleStringProperty("");
    private BooleanProperty destroyed = new SimpleBooleanProperty(false);

    private IntegerProperty XP = new SimpleIntegerProperty(0);
    private IntegerProperty HP = new SimpleIntegerProperty(0);

    private IntegerProperty orientationDeg = new SimpleIntegerProperty(0);
    private ObjectProperty<AxialCoordinate> position = new SimpleObjectProperty<>(new AxialCoordinate(0,0));

    private IntegerProperty customAdditionalHPmax = new SimpleIntegerProperty(0);
    private IntegerProperty customAdditionalCannons = new SimpleIntegerProperty(0);
    private IntegerProperty customAdditionalSpeed = new SimpleIntegerProperty(0);

    private IntegerProperty carriesMetalUnits = new SimpleIntegerProperty(0);
    private IntegerProperty carriesWoodUnits = new SimpleIntegerProperty(0);
    private IntegerProperty carriesClothUnits = new SimpleIntegerProperty(0);
    private IntegerProperty carriesRumUnits = new SimpleIntegerProperty(0);
    private IntegerProperty carriesTobaccoUnits = new SimpleIntegerProperty(0);
    private IntegerProperty carriesMoney = new SimpleIntegerProperty(0);

    private MapProperty<String, ShipEnhancementStatus> enhancements = new SimpleMapProperty<>(FXCollections.observableHashMap());

    private MapProperty<String, String> customExtensions = new SimpleMapProperty<>(FXCollections.observableHashMap());

    private SetProperty<ShipMechanics> activeMechanics = new SimpleSetProperty<>(FXCollections.observableSet());


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

    public String getTeamId() {
        return teamId.get();
    }

    public StringProperty teamIdProperty() {
        return teamId;
    }

    public String getCaptain() {
        return captain.get();
    }

    public StringProperty captainProperty() {
        return captain;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public boolean isDestroyed() {
        return destroyed.get();
    }

    public BooleanProperty destroyedProperty() {
        return destroyed;
    }

    public int getHP() {
        return HP.get();
    }

    public IntegerProperty HPProperty() {
        return HP;
    }

    public int getXP() {
        return XP.get();
    }

    public IntegerProperty XPProperty() {
        return XP;
    }

    public int getOrientationDeg() {
        return orientationDeg.get();
    }

    public IntegerProperty orientationDegProperty() {
        return orientationDeg;
    }

    public AxialCoordinate getPosition() {
        return position.get();
    }

    public ObjectProperty<AxialCoordinate> positionProperty() {
        return position;
    }

    public void setPosition(AxialCoordinate position) {
        this.position.set(position);
    }

    public int getCustomAdditionalHPmax() {
        return customAdditionalHPmax.get();
    }

    public IntegerProperty customAdditionalHPmaxProperty() {
        return customAdditionalHPmax;
    }

    public int getCustomAdditionalCannons() {
        return customAdditionalCannons.get();
    }

    public IntegerProperty customAdditionalCannonsProperty() {
        return customAdditionalCannons;
    }

    public int getCustomAdditionalSpeed() {
        return customAdditionalSpeed.get();
    }

    public IntegerProperty customAdditionalSpeedProperty() {
        return customAdditionalSpeed;
    }

    public int getCarriesMetalUnits() {
        return carriesMetalUnits.get();
    }

    public IntegerProperty carriesMetalUnitsProperty() {
        return carriesMetalUnits;
    }

    public int getCarriesWoodUnits() {
        return carriesWoodUnits.get();
    }

    public IntegerProperty carriesWoodUnitsProperty() {
        return carriesWoodUnits;
    }

    public int getCarriesClothUnits() {
        return carriesClothUnits.get();
    }

    public IntegerProperty carriesClothUnitsProperty() {
        return carriesClothUnits;
    }

    public int getCarriesRumUnits() {
        return carriesRumUnits.get();
    }

    public IntegerProperty carriesRumUnitsProperty() {
        return carriesRumUnits;
    }

    public int getCarriesTobaccoUnits() {
        return carriesTobaccoUnits.get();
    }

    public IntegerProperty carriesTobaccoUnitsProperty() {
        return carriesTobaccoUnits;
    }

    public int getCarriesMoney() {
        return carriesMoney.get();
    }

    public IntegerProperty carriesMoneyProperty() {
        return carriesMoney;
    }

    public ObservableMap<String, ShipEnhancementStatus> getEnhancements() {
        return enhancements.get();
    }

    public MapProperty<String, ShipEnhancementStatus> enhancementsProperty() {
        return enhancements;
    }

    public ObservableMap<String, String> getCustomExtensions() {
        return customExtensions.get();
    }

    public MapProperty<String, String> customExtensionsProperty() {
        return customExtensions;
    }

    public ObservableSet<ShipMechanics> getActiveMechanics() {
        return activeMechanics.get();
    }

    public SetProperty<ShipMechanics> activeMechanicsProperty() {
        return activeMechanics;
    }
}