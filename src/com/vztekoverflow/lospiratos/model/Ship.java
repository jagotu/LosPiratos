package com.vztekoverflow.lospiratos.model;


import javafx.beans.property.*;


public class Ship {
    public Ship(String name, String id, String teamId, String captain, String type,
                int HP, int additionalHPmax, int additionalCannons, int additionalSpeed, int positionP, int positionQ, int orientationDeg,
                ShipEnhancementStatus upgradeCannon,
                ShipEnhancementStatus upgradeHull,
                ShipEnhancementStatus enhMortar,
                ShipEnhancementStatus enhChainShot,
                ShipEnhancementStatus enhHeavyShot,
                ShipEnhancementStatus enhRam,
                int capacityLeft, int carriesMetalUnits, int carriesWoodUnits, int carriesClothUnits, int carriesRumUnits,
                int carriesTobaccoUnits, int carriesMoney
    ) {
        this.name.set(name);
        this.id.set(id);
        this.teamId.set(teamId);
        this.captain.set(captain);
        this.type.set(type);

        this.HP.set(HP);
        this.positionP.set(positionP);
        this.positionQ.set(positionQ);
        this.orientationDeg.set(orientationDeg);

        this.customAdditionalHPmax.set(additionalHPmax);
        this.customAdditionalCannons.set(additionalCannons);
        this.customAdditionalSpeed.set(additionalSpeed);

        //todo those strings are here for testing only.
        this.enhancements.put("upgCannon", upgradeCannon);
        this.enhancements.put("upgHull", upgradeHull);
        this.enhancements.put("chainShot", enhChainShot);
        this.enhancements.put("heavyShot", enhHeavyShot);
        this.enhancements.put("ram", enhRam);
        this.enhancements.put("mortar", enhMortar);

        this.capacityLeft.setValue(capacityLeft);
        this.carriesMetalUnits.setValue(carriesMetalUnits);
        this.carriesWoodUnits.setValue(carriesWoodUnits);
        this.carriesClothUnits.setValue(carriesClothUnits);
        this.carriesRumUnits.setValue(carriesRumUnits);
        this.carriesTobaccoUnits.setValue(carriesTobaccoUnits);
        this.carriesMoney.setValue(carriesMoney);

    }

    public Ship() {

    }

    public StringProperty name = new SimpleStringProperty("");
    public StringProperty id = new SimpleStringProperty("");
    public StringProperty teamId = new SimpleStringProperty("");
    public StringProperty captain = new SimpleStringProperty("");
    public StringProperty type = new SimpleStringProperty("");

    public IntegerProperty HP = new SimpleIntegerProperty(0);
    public IntegerProperty positionP = new SimpleIntegerProperty(0);
    public IntegerProperty positionQ = new SimpleIntegerProperty(0);
    public IntegerProperty orientationDeg = new SimpleIntegerProperty(0);

    public IntegerProperty customAdditionalHPmax = new SimpleIntegerProperty(0);
    public IntegerProperty customAdditionalCannons = new SimpleIntegerProperty(0);
    public IntegerProperty customAdditionalSpeed = new SimpleIntegerProperty(0);

    public IntegerProperty capacityLeft = new SimpleIntegerProperty(0);
    public IntegerProperty carriesMetalUnits = new SimpleIntegerProperty(0);
    public IntegerProperty carriesWoodUnits = new SimpleIntegerProperty(0);
    public IntegerProperty carriesClothUnits = new SimpleIntegerProperty(0);
    public IntegerProperty carriesRumUnits = new SimpleIntegerProperty(0);
    public IntegerProperty carriesTobaccoUnits = new SimpleIntegerProperty(0);
    public IntegerProperty carriesMoney = new SimpleIntegerProperty(0);

    public MapProperty<String, ShipEnhancementStatus> enhancements = new SimpleMapProperty<>();

    public MapProperty<String, String> customExtensions = new SimpleMapProperty<>();

    public ListProperty<ShipMechanics> activeMechanics = new SimpleListProperty<>();


}