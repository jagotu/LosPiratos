package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.FxUtils;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;

import java.util.Collection;

public class Team {

    public static final int INITIAL_MONEY = 500;

    //initializers:
    /*
     * Sets properties' values to default.
     * Should be called only after the object has been created.
     */
    public void initialize(){
        ownedResource.money.set(Team.INITIAL_MONEY);
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
        ownedResource.moneyProperty().bindBidirectional(teamModel.moneyProperty());
        ownedResource.clothProperty().bindBidirectional(teamModel.ownedClothProperty());
        ownedResource.metalProperty().bindBidirectional(teamModel.ownedMetalProperty());
        ownedResource.rumProperty().bindBidirectional(teamModel.ownedRumProperty());
        ownedResource.tobaccoProperty().bindBidirectional(teamModel.ownedTobaccoProperty());
        ownedResource.woodProperty().bindBidirectional(teamModel.ownedWoodProperty());

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
        ships.put(shipName, s);
        game.registerShip(s);
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

    public ObservableMap<String, Ship> getShips() {
        return ships.get();
    }

    public MapProperty<String, Ship> shipsProperty() {
        return ships;
    }

    private MapProperty<String, Ship> ships = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private StringProperty name = new SimpleStringProperty("");
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();

    private final Resource ownedResource = new Resource();

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

    public Resource getOwnedResource() {
        return ownedResource;
    }

    //public methods:

    /* Creates a new ship owned by the team and adds it to its list of ships
     * @param name If a ship with this name already exists, returns null
     * @param name if is empty or null, returns null
     * @returns null if a ship with the same name already exists
     */
    public <T extends ShipType> Ship createAndAddNewShip(Class<T> shipType, String shipName, String captainName, AxialCoordinate position) {
        if(!game.mayCreateShipWithName(shipName)) return null;
        com.vztekoverflow.lospiratos.model.Ship modelShip = new com.vztekoverflow.lospiratos.model.Ship();
        modelShip.nameProperty().set(shipName);
        modelShip.typeProperty().set(ShipType.getPersistentName(shipType));
        modelShip.captainProperty().set(captainName);
        modelShip.setPosition(position);
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
