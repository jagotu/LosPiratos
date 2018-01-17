package com.vztekoverflow.lospiratos.model;


import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;


public class Ship {
    public Ship(String name, String teamId, String captain, String type,
                int HP, int additionalHPmax, int additionalCannons, int additionalSpeed, int positionQ, int positionR, int orientationDeg,
                ShipEnhancementStatus upgradeCannon,
                ShipEnhancementStatus upgradeHull,
                ShipEnhancementStatus enhMortar,
                ShipEnhancementStatus enhChainShot,
                ShipEnhancementStatus enhHeavyShot,
                ShipEnhancementStatus enhRam,
                int capacityLeft /*not used, historical reasons*/, int carriesMetalUnits, int carriesWoodUnits, int carriesClothUnits, int carriesRumUnits,
                int carriesTobaccoUnits, int carriesMoney
    ) {
        this.name.set(name);
        this.teamId.set(teamId);
        this.captain.set(captain);
        this.type.set(type);

        this.HP.set(HP);
        this.position.set( new AxialCoordinate(positionQ, positionR));
        this.orientationDeg.set(orientationDeg);

        this.customAdditionalHPmax.set(additionalHPmax);
        this.customAdditionalCannons.set(additionalCannons);
        this.customAdditionalSpeed.set(additionalSpeed);

        this.enhancements.put("com.vztekoverflow.lospiratos.viewmodel.shipEntities.enhancements.CannonUpgrade", upgradeCannon);
        this.enhancements.put("com.vztekoverflow.lospiratos.viewmodel.shipEntities.enhancements.HullUpgrade", upgradeHull);

        this.carriesMetalUnits.setValue(carriesMetalUnits);
        this.carriesWoodUnits.setValue(carriesWoodUnits);
        this.carriesClothUnits.setValue(carriesClothUnits);
        this.carriesRumUnits.setValue(carriesRumUnits);
        this.carriesTobaccoUnits.setValue(carriesTobaccoUnits);
        this.carriesMoney.setValue(carriesMoney);

    }

    public Ship() {

    }

    //properties:

    private StringProperty name = new SimpleStringProperty("");
    private StringProperty id = new SimpleStringProperty(""); //useless for now
    private StringProperty teamId = new SimpleStringProperty("");
    private StringProperty captain = new SimpleStringProperty("");
    private StringProperty type = new SimpleStringProperty("");
    private BooleanProperty destroyed = new SimpleBooleanProperty(false);

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

    private ListProperty<ShipMechanics> activeMechanics = new SimpleListProperty<>(FXCollections.observableArrayList());


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

    public ObservableList<ShipMechanics> getActiveMechanics() {
        return activeMechanics.get();
    }

    public ListProperty<ShipMechanics> activeMechanicsProperty() {
        return activeMechanics;
    }
}