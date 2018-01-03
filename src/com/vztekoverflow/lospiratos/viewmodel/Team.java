package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Team {

    public static final int INITIAL_MONEY = 500;

    //initializers:
    /*
     * Sets properties' values to default.
     * Should be called only after the object has been created.
     */
    public void initialize(){
        money.set(Team.INITIAL_MONEY);
    }

    private com.vztekoverflow.lospiratos.model.Team teamModel;
    private Game game;
    /*
     * Creates a new team object with values as defined in the @teamModel.
     * @param owner is not bound to any property in model. It must correspond to the owner game as represented in model. If you set a game that is not an owner according to the model, behaviour is not defined.
     */
    public Team(Game owner, com.vztekoverflow.lospiratos.model.Team teamModel) {
        this.teamModel = teamModel;
        this.game = owner;
        bindToModel();
    }

    private void bindToModel() {
        ownedTobacco.bindBidirectional(teamModel.ownedTobaccoProperty());
        ownedRum.bindBidirectional(teamModel.ownedRumProperty());
        ownedCloth.bindBidirectional(teamModel.ownedClothProperty());
        ownedWood.bindBidirectional(teamModel.ownedWoodProperty());
        ownedMetal.bindBidirectional(teamModel.ownedMetalProperty());
        money.bindBidirectional(teamModel.moneyProperty());

        name.bindBidirectional(teamModel.nameProperty());

        teamModel.colorProperty().addListener((observable, oldValue, newValue) -> trySettingColor(newValue));
        trySettingColor(teamModel.getColor());

        teamModel.shipsProperty().addListener((ListChangeListener<com.vztekoverflow.lospiratos.model.Ship>) c -> {
            while (c.next()) {
                if (c.wasAdded()){
                    for (com.vztekoverflow.lospiratos.model.Ship addedItem : c.getAddedSubList()) {
                        tryCreatingAndAdd(addedItem);
                    }
                } else if(c.wasRemoved()) {
                    for (com.vztekoverflow.lospiratos.model.Ship removedItem : c.getRemoved()) {
                        removeShipFromCollections(removedItem.getName());
                    }
                }
            }
        });
        for (com.vztekoverflow.lospiratos.model.Ship modelShip : teamModel.getShips()) {
            tryCreatingAndAdd(modelShip);
        }
    }
    private void tryCreatingAndAdd(com.vztekoverflow.lospiratos.model.Ship model){
        String shipName = model.getName();
        if (!game.mayCreateShipWithName(shipName)) {
            Warnings.makeWarning(toString()+".tryCreatingAndAdd()", "No ship created, because ship's name already exists or is invalid: " + shipName);
            return;
        }
        if(ships.containsKey(shipName)){
            Warnings.panic(toString()+".tryCreatingAndAdd()","Map<Ship> ships contains a key which game.ships does not!!! : " + shipName);
            return;
        }
        Ship s = new Ship(this, model);
        game.registerShip(s);
        ships.put(shipName, s);
    }
    private void removeShipFromCollections(String shipName){
        if(ships.containsKey(shipName)){
            ships.remove(shipName);
            game.unregisterShip(shipName);
        }
        else{
            Warnings.makeStrongWarning(toString()+".removeShipFromCollections()", "Attempt to remove a ship whose name is unknown: " + shipName);
        }
    }

    //properties:

    private MapProperty<String, Ship> ships = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private StringProperty name = new SimpleStringProperty("");
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();

    private IntegerProperty money = new SimpleIntegerProperty(0);
    private IntegerProperty ownedMetal = new SimpleIntegerProperty(0);
    private IntegerProperty ownedWood = new SimpleIntegerProperty(0);
    private IntegerProperty ownedCloth = new SimpleIntegerProperty(0);
    private IntegerProperty ownedRum = new SimpleIntegerProperty(0);
    private IntegerProperty ownedTobacco = new SimpleIntegerProperty(0);

    public Collection<Ship> getShips() {
        return ships.get().values();
    }
    //shipProperty() is not available, because it does not support modification.
    //    Team class provides API for ship manipulation instead.

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Game getGame() {
        return game;
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

    /* Creates a new ship owned by the team and adds it to its list of ships
     * @param name If a ship with this name already exists, returns null
     * @param name if is empty or null, returns null
     * @returns null if a ship with the same name already exists
     */
    public <T extends ShipType> Ship createAndAddNewShip(Class<T> shipType, String shipName, String captainName) {
        if(!game.mayCreateShipWithName(shipName)) return null;
        com.vztekoverflow.lospiratos.model.Ship modelShip = new com.vztekoverflow.lospiratos.model.Ship();
        modelShip.nameProperty().set(shipName);
        modelShip.typeProperty().set(ShipType.getPersistentName(shipType));
        modelShip.captainProperty().set(captainName);
        teamModel.shipsProperty().add(modelShip);
        //at this place, teamModel.shipsProperty's change calls my observer
        //   which then adds the ship to this team's collection (if valid)
        Ship s = findShipByName(shipName);
        if(s == null) return null; //this means the model was somehow invalid
        s.initialize();
        return s;
    }
    public void removeShip(String shipName){
        teamModel.shipsProperty().removeIf(s -> s.getName().equals(shipName));
    }

    @Override
    public String toString() {
        String name = this.name.get();
        if (name == null || name.equals("")) name = "<empty>";
        return "Team \"" + name + "\"";
    }

    /*
     * @returns null when no ship with the name has been found
     */
    public Ship findShipByName(String shipName) {
        if (ships.containsKey(shipName)) {
            return ships.get(shipName);
        }
        Warnings.makeWarning(toString(), "No ship with this name found: " + shipName);
        return null;
    }

    //private methods:
    private void trySettingColor(String color) {
        if (color == null || color.isEmpty()) {
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
