package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Team {

    public static final int INITIAL_MONEY = 500;

    private com.vztekoverflow.lospiratos.model.Team teamModel;
    private Game game;

    /* Creates a new team and binds it to a teamModel.
     * @param emptyTeamModel a team model with no values set. If values as name or color are set in the model, they will be overwritten by parameters.
     * @param owner is not bound to any property in model. It must correspond to the owner game as represented in model. If you set a game that is not an owner according to the model, behaviour is not defined.
     */
    public Team(Game owner, String name, Color color, com.vztekoverflow.lospiratos.model.Team teamModel){
        this.teamModel = teamModel;
        this.game = owner;
        teamModel.colorProperty().set(FxUtils.toRGBCode(color));
        teamModel.nameProperty().set(name);
        bindToModel();

        //default values:
        money.set(Team.INITIAL_MONEY);
    }

    /*
     * Creates a new team object with values as defined in the @teamModel.
     */
    public Team(Game owner, com.vztekoverflow.lospiratos.model.Team teamModel) {
        this.teamModel = teamModel;
        this.game = owner;
        bindToModel();
    }
    private void bindToModel(){
        ownedTobacco.bindBidirectional(teamModel.ownedTobaccoProperty());
        ownedRum.bindBidirectional(teamModel.ownedRumProperty());
        ownedCloth.bindBidirectional(teamModel.ownedClothProperty());
        ownedWood.bindBidirectional(teamModel.ownedWoodProperty());
        ownedMetal.bindBidirectional(teamModel.ownedMetalProperty());
        money.bindBidirectional(teamModel.moneyProperty());

        name.bindBidirectional(teamModel.nameProperty());

        teamModel.colorProperty().addListener((observable, oldValue, newValue) -> trySettingColor(newValue));
        trySettingColor(teamModel.getColor());

        teamModel.shipsProperty().addListener((observable, oldValue, newValue) -> {
            //todo how to make this? I want add new ships or remove removed ships, but do not change currently existing ship object (don't want to recreate them) (Github issue #7)
            Warnings.makeStrongWarning(toString(), "NotImplemented: Team.shipsProperty.changedListener.");
        });
        loadShipsFromModel(teamModel.getShips());
    }

    private void loadShipsFromModel(List<com.vztekoverflow.lospiratos.model.Ship> ships){
        for(com.vztekoverflow.lospiratos.model.Ship modelShip: ships){
            Ship s = new Ship(modelShip);
            this.ships.add(s);
        }
    }

    //properties:

    private ListProperty<Ship> ships = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty name = new SimpleStringProperty("");
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();

    private IntegerProperty money = new SimpleIntegerProperty(0);
    private IntegerProperty ownedMetal = new SimpleIntegerProperty(0);
    private IntegerProperty ownedWood = new SimpleIntegerProperty(0);
    private IntegerProperty ownedCloth = new SimpleIntegerProperty(0);
    private IntegerProperty ownedRum = new SimpleIntegerProperty(0);
    private IntegerProperty ownedTobacco = new SimpleIntegerProperty(0);

    public ObservableList<Ship> getShips() {
        return ships.get();
    }

    public ListProperty<Ship> shipsProperty() {
        return ships;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color color) {
        teamModel.colorProperty().set(FxUtils.toRGBCode(color));
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public int getMoney() {
        return money.get();
    }

    public void addMoney(int value) {
        money.set(money.get() + value);
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public int getOwnedMetal() {
        return ownedMetal.get();
    }

    public void addOwnedMetal(int value) {
        ownedMetal.set(ownedMetal.get() + value);
    }

    public IntegerProperty ownedMetalProperty() {
        return ownedMetal;
    }

    public int getOwnedWood() {
        return ownedWood.get();
    }

    public void addOwnedWood(int value) {
        ownedWood.set(ownedWood.get() + value);
    }

    public IntegerProperty ownedWoodProperty() {
        return ownedWood;
    }

    public int getOwnedCloth() {
        return ownedCloth.get();
    }

    public void addOwnedCloth(int value) {
        ownedCloth.set(ownedCloth.get() + value);
    }

    public IntegerProperty ownedClothProperty() {
        return ownedCloth;
    }

    public int getOwnedRum() {
        return ownedRum.get();
    }

    public void addOwnedRum(int value) {
        ownedRum.set(ownedRum.get() + value);
    }

    public IntegerProperty ownedRumProperty() {
        return ownedRum;
    }

    public int getOwnedTobacco() {
        return ownedTobacco.get();
    }

    public void addOwnedTobacco(int value) {
        ownedTobacco.set(ownedTobacco.get() + value);
    }

    public IntegerProperty ownedTobaccoProperty() {
        return ownedTobacco;
    }


    //public methods:

    public <T extends ShipType> Ship createAndAddNewShip(Class<T> shipType, String shipName, String captainName) {
        com.vztekoverflow.lospiratos.model.Ship modelShip = new com.vztekoverflow.lospiratos.model.Ship();
        Ship s = new Ship(shipType, shipName, this, modelShip);
        s.setCaptainName(captainName);
        teamModel.shipsProperty().add(modelShip);
        return s;
    }

    @Override
    public String toString() {
        String name = this.name.get();
        if(name == null || name.equals("")) name = "<empty>";
        return "Team \"" + name + "\"";
    }

    /*
     * @returns null when no team with the name has been found
     */
    public Ship findShipByName(String shipName) {
        //todo this is a O(N) implementation. Here, faster impl should be used
        //todo Github issue #8
        List<Ship> result = ships.stream().filter(t -> t.getName().equals(shipName)).collect(Collectors.toList());
        int size = result.size();
        if(size == 0){
            Warnings.makeWarning(toString(), "No ship with this name found: " + shipName);
            return  null;
        }
        if(size > 1) Warnings.panic(toString(), "More ships (" + size+ ") with the same name in one team: " + shipName);
        return result.get(0);
    }

    //private methods:
    private void trySettingColor(String color) {
        if(color == null || color.isEmpty()){
            Warnings.makeWarning(toString(), "Invalid color (null or empty).");
            return;
        }
        try {
            this.color.set(Color.web(color));
        } catch (IllegalArgumentException | NullPointerException e) {
            Warnings.makeWarning(toString(), "Invalid color: " + color);
        }
    }

}
