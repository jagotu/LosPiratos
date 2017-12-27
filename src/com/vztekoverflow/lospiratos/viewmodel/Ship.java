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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import java.util.Map;


public class Ship {

    //initializers:

    public Ship(com.vztekoverflow.lospiratos.model.Ship shipModel) {
        this.shipModel = shipModel;
        name.bindBidirectional(shipModel.nameProperty());
        captainName.bindBidirectional(shipModel.captainProperty());

        shipModel.typeProperty().addListener((observable, oldValue, newValue) -> trySettingType(newValue) );
        trySettingType(shipModel.getType());
        if(shipType == null){
            Warnings.makeWarning(toString(), "ShipType not set (or invalid). Fallbacking to Brig.");
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

    }

    private void trySettingType(String type){
        try{
            Ship.this.shipType = ShipType.getInstaceFromString(type);
        }catch (IllegalArgumentException e){
            Warnings.makeWarning(toString(),"Invalid ship type description: " + type);
        }
    }

    private void addEnhancements(Map<String, ShipEnhancementStatus> map){
        for(Map.Entry<String, ShipEnhancementStatus> entry : map.entrySet()){
            try{
                ShipEnhancement e = ShipEnhancement.getInstaceFromString(entry.getKey());
                if(entry.getValue() == ShipEnhancementStatus.damaged);
                e.setDestroyed(true);
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
    public void setShipType(ShipType shipType) {
        shipModel.typeProperty().set(shipType.toString());
    }


    private com.vztekoverflow.lospiratos.model.Ship shipModel;

    private IntegerProperty currentHP = new SimpleIntegerProperty();
    public void addToCurrentHP(int value) {
        this.currentHP.add(currentHP);
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

    public <Enhancement extends ShipEnhancement> boolean hasEnhancement(Class<Enhancement> enhancementClass){
        //todo rewrite to faster implementation where enhancemetns is Map<Type, ShipEnhancement>.
        for(ShipEnhancement e : enhancements){
            if(enhancementClass.isInstance(e)) return true;
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


    //static:

    public static Ship LoadFromModel(com.vztekoverflow.lospiratos.model.Ship model){
        throw new NotImplementedException();
        //todo tohle mozna vubec neni potreba, protoze to cele zvladne konstruktor
    }

}