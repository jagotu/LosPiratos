package com.vztekoverflow.lospiratos.viewmodel;



import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEntity;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Brig;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.util.Map;


public class Ship {

    //initializers:

    /* Creates a new ship and binds it to a shipModel.
     * @param emptyShipModel a ship model with no values set. If values as name or shipType are set in the model, they will be overwritten by parameters.
     * @param owner is not bound to any property in model. It must correspond to the owner as represented in model. If you set a team that is not an owner according to the model, behaviour is not defined.
     */
    public <T extends ShipType> Ship(Class<T> shipType, String name, Team owner, com.vztekoverflow.lospiratos.model.Ship emptyShipModel ){
        this.shipModel = emptyShipModel;
        this.owner = owner;
        emptyShipModel.nameProperty().set(name);
        emptyShipModel.typeProperty().set(shipType.toString());
        bindToModel();

        //default values:
        currentHP.set(getMaxHP());
    }

    /*
     * Creates a new ship object with values as defined in the @shipModel.
     */
    public Ship(com.vztekoverflow.lospiratos.model.Ship shipModel) {
        this.shipModel = shipModel;
        bindToModel();
    }

    private void bindToModel(){
        name.bindBidirectional(shipModel.nameProperty());
        captainName.bindBidirectional(shipModel.captainProperty());

        shipModel.typeProperty().addListener((observable, oldValue, newValue) -> trySettingType(newValue) );
        boolean typeSet = trySettingType(shipModel.getType());
        if(!typeSet){
            Warnings.makeWarning(toString(), "Fallbacking to Brig.");
            shipType = new Brig(); //to make sure that some type is always set
        }

        addEnhancements(shipModel.enhancementsProperty().get());
        shipModel.enhancementsProperty().addListener((observable, oldValue, newValue) -> {
            //simple O(N) implementations: simply load all enhancements again
            //todo rewrite to something smarter
            Ship.this.enhancements.set(FXCollections.observableArrayList()); //makes the list empty
            addEnhancements(newValue);
        });

        destroyed.bindBidirectional(shipModel.destroyedProperty());
        currentHP.bindBidirectional(shipModel.HPProperty());
    }

    private boolean trySettingType(String type){
        if(type == null || type.isEmpty()){
            Warnings.makeWarning(toString(), "Invalid ship type description (null or empty).");
            return false;
        }
        try{
            Ship.this.shipType = ShipType.getInstaceFromString(type);
            return true;
        }catch (IllegalArgumentException e){
            Warnings.makeWarning(toString(),"Invalid ship type description: " + type);
            return  false;
        }
    }

    private void addEnhancements(Map<String, ShipEnhancementStatus> map){
        for(Map.Entry<String, ShipEnhancementStatus> entry : map.entrySet()){
            try{
                ShipEnhancement e = ShipEnhancement.getInstaceFromString(entry.getKey());
                if(entry.getValue() == ShipEnhancementStatus.damaged){
                    e.setDestroyed(true);
                }
                e.onAddedToShip(this);
                enhancements.add(e);
            }catch(IllegalArgumentException e){
                Warnings.makeWarning(toString(),"Unknown ship enhancement name: "+ entry.getKey());
            }


        }
    }

    //properties:


    private ShipType shipType;
    public ShipType getShipType() {
        return shipType;
    }
    public <T extends ShipType> void setShipType(Class<T> shipType) {
        shipModel.typeProperty().set(shipType.toString());
    }
    private Team owner;
    public Team getOwner() {
        return owner;
    }
    public void setOwner(Team owner) {
        this.owner = owner;
    }

    private com.vztekoverflow.lospiratos.model.Ship shipModel;

    private IntegerProperty currentHP = new SimpleIntegerProperty();
    public void addToCurrentHP(int value) {
        currentHP.set(currentHP.get() + value);
    }
    public int getCurrentHP() {
        return currentHP.getValue();
    }
    //stats to implement: cannons, currentHp, maxHp, cargo, garrison, customAdditionalSpeed

    private BooleanProperty destroyed = new SimpleBooleanProperty(true);
    public boolean isDestroyed() {
        return destroyed.getValue();
    }
    public void setDestroyed(boolean destroyed) {
        this.destroyed.setValue(destroyed);
    }

    private StringProperty name = new SimpleStringProperty();
    public String getName() {
        return name.getValue();
    }
    public void setName(String name) {
        this.name.set(name);
    }

    private StringProperty captainName = new SimpleStringProperty();
    public String getCaptainName() {
        return captainName.getValue();
    }
    public void setCaptainName(String captainName) {
        this.captainName.set(captainName);
    }


    private ListProperty<ShipEnhancement> enhancements = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ObservableList<ShipEnhancement> getEnhancements() {
        return enhancements;
    }

    //todo to be implemented:
    private ListProperty<ShipMechanics> mechanics = new SimpleListProperty<>(FXCollections.observableArrayList());


    //getters of non-properties:

    public int getMaxHP(){
        int val = 0;
        for(ShipEntity e : enhancements){
            val += e.getBonusMaxHP();
        }
        val += shipType.getBonusMaxHP();
        return val;
    }


    // enhnacements:

    public <Enhancement extends ShipEnhancement> void addNewEnhancement(Class<Enhancement> enhancementClass){
        shipModel.enhancementsProperty().put(enhancementClass.toString(), ShipEnhancementStatus.active);
    }

    /*
     * @returns null if Ship does not contain any enhancement of given type
     */
    public <Enhancement extends ShipEnhancement> Enhancement getEnhancement(Class<Enhancement> enhancementClass){
        //todo rewrite to faster implementation where enhancemetns is Map<Type, ShipEnhancement>.
        for(ShipEnhancement e : enhancements){
            if(enhancementClass.isInstance(e)) return (Enhancement) e;
        }
        return null;
    }

    public <Enhancement extends ShipEnhancement> boolean hasActiveEnhancement(Class<Enhancement> enhancementClass){
        //todo rewrite to faster implementation where enhancemetns is Map<Type, ShipEnhancement>.
        for(ShipEnhancement e : enhancements){
            if(enhancementClass.isInstance(e)) return e.isDestroyed() ? false : true;
        }
        return false;
    }

    //functions:

    @Override
    public String toString() {
        String name = this.name.get();
        if(name == null || name.equals("")) name = "<empty>";
        return "Ship \"" + name + "\"";
    }


}