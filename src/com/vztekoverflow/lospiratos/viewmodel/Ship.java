package com.vztekoverflow.lospiratos.viewmodel;



import com.vztekoverflow.lospiratos.model.ShipEnhancementStatus;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEntity;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipMechanics;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Ship {

    //initializers:

    public Ship(ShipType shipType, com.vztekoverflow.lospiratos.model.Ship shipModel) {
        this.shipType = shipType;
        this.shipModel = shipModel;
        name.bindBidirectional(shipModel.name);
        captainName.bindBidirectional(shipModel.captain);
        shipModel.type.set(ShipType.class.toString());
        shipModel.type.addListener((observable, oldValue, newValue) -> {
            try{
                Ship.this.shipType = ShipType.getInstaceFromString(newValue);
            }catch (IllegalArgumentException e){
                Warnings.makeWarning("Invalid ship type description: " + newValue);
            }
        });
        shipModel.enhancements.addListener((observable, oldValue, newValue) -> {
            //simple O(N) implementations: simply load all enhancements again
            Ship.this.enhancements = new ArrayList<>();
            addEnhancements(newValue);
        });
        addEnhancements(shipModel.enhancements.get());
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
                Warnings.makeWarning("Unknown ship enhancement name: "+ entry.getKey());
            }


        }
    }

    //properties:

    private ShipType shipType;
    public ShipType getShipType() {
        return shipType;
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


    private List<ShipEnhancement> enhancements;
    public List<ShipEnhancement> getEnhancements() {
        return enhancements;
    }
    public void addEnhancement(ShipEnhancement item){
//        item.onAddedToShip(this);
//        enhancements.add(item);
//        addToCurrentHP(item.getBonusMaxHP());
        //in current version, it deletes the old ShipEnhancement and then the Notify callback creates a new one:
        item.onAddedToShip(this);  //this is actually useless, because item will be destroyed anyway
        ShipEnhancementStatus status = ShipEnhancementStatus.active;
        if(item.isDestroyed()) status = ShipEnhancementStatus.damaged;
        shipModel.enhancements.put(item.getClass().toString(), status);
    }
    private List<ShipMechanics> mechanics;
    public List<ShipMechanics> getMechanics() {
        return mechanics;
    }
    public void addEnhancement(ShipMechanics item){
        item.onAddedToShip(this);
        mechanics.add(item);
    }

    //getters of non-properties:

    public int getMaxHP(){
        int val = 0;
        for(ShipEntity e : enhancements){
            val += e.getBonusMaxHP();
        }
        val += shipType.getBonusMaxHP();
        return val;
    }

    //functions:


    //static:

    public static Ship LoadFromModel(com.vztekoverflow.lospiratos.model.Ship model){
        throw new NotImplementedException();
    }

}