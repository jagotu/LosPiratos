package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.model.ResourceM;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Resource extends ResourceReadOnly implements Observable {
    public Resource(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        super();
        this.moneyProp.set(money);
        this.clothProp.set(cloth);
        this.metalProp.set(metal);
        this.rumProp.set(rum);
        this.woodProp.set(wood);
    }

    public Resource() {
    }

    protected IntegerProperty moneyProp = new SimpleIntegerProperty(0);
    protected IntegerProperty clothProp = new SimpleIntegerProperty(0);
    protected IntegerProperty metalProp = new SimpleIntegerProperty(0);
    //those weird names for rum, tobacco and wood are so that all resource have same number of characters (better code readability)
    protected IntegerProperty rumProp = new SimpleIntegerProperty(0);
    protected IntegerProperty tobcoProp = new SimpleIntegerProperty(0);
    protected IntegerProperty woodProp = new SimpleIntegerProperty(0);

    /**
     * sets all values to 0
     */
    public void clear(){
        setAll(ResourceReadOnly.ZERO);
    }

    public void setAll(ResourceReadOnly value) {
        setMoney(value.getMoney());
        setCloth(value.getCloth());
        setMetal(value.getMetal());
        setTobacco(value.getTobacco());
        setRum(value.getRum());
        setWood(value.getWood());
    }

    @Override
    public int getMoney() {
        return moneyProp.get();
    }

    @Override
    public int getCloth() {
        return clothProp.get();
    }

    @Override
    public int getMetal() {
        return metalProp.get();
    }

    @Override
    public int getRum() {
        return rumProp.get();
    }

    @Override
    public int getWood() {
        return woodProp.get();
    }

    @Override
    public int getTobacco() {
        return tobcoProp.get();
    }

    public void setMoney(int moneyProp) {
        this.moneyProp.set(moneyProp);
    }

    public void setCloth(int cloth) {
        this.clothProp.set(cloth);
    }

    public void setMetal(int metal) {
        this.metalProp.set(metal);
    }

    public void setRum(int rum) {
        this.rumProp.set(rum);
    }

    public void setTobacco(int tobacco) {
        this.tobcoProp.set(tobacco);
    }

    public void setWood(int wood) {
        this.woodProp.set(wood);
    }

    public void addMoney(int value) {
        moneyProp.set(moneyProp.get() + value);
    }

    public void addCloth(int value) {
        clothProp.set(clothProp.get() + value);
    }

    public void addMetal(int value) {
        metalProp.set(metalProp.get() + value);
    }

    public void addRum(int value) {
        rumProp.set(rumProp.get() + value);
    }

    public void addTobacco(int value) {
        tobcoProp.set(tobcoProp.get() + value);
    }

    public void addWood(int value) {
        woodProp.set(woodProp.get() + value);
    }

    /**
     * The listener will listen for changes on any of the resources' component
     * @param listener
     */
    public void addListener(InvalidationListener listener){
        moneyPropProperty().addListener(listener);
        clothProperty().addListener(listener);
        metalProperty().addListener(listener);
        rumProperty().addListener(listener);
        tobaccoProperty().addListener(listener);
        woodProperty().addListener(listener);
    }
    public void removeListener(InvalidationListener listener){
        moneyPropProperty().removeListener(listener);
        clothProperty().removeListener(listener);
        metalProperty().removeListener(listener);
        rumProperty().removeListener(listener);
        tobaccoProperty().removeListener(listener);
        woodProperty().removeListener(listener);
    }

    /**
     * component-wise increases resource number by value in {@code value}
     */
    public void add(ResourceReadOnly value) {
        addMoney(value.getMoney());
        addCloth(value.getCloth());
        addMetal(value.getMetal());
        addRum(value.getRum());
        addTobacco(value.getTobacco());
        addWood(value.getWood());
    }

    /**
     * component-wise decreases resource number by value in {@code value}
     */
    public void subtract(ResourceReadOnly value) {
        addMoney(-value.getMoney());
        addCloth(-value.getCloth());
        addMetal(-value.getMetal());
        addRum(-value.getRum());
        addTobacco(-value.getTobacco());
        addWood(-value.getWood());
    }

    /**
     * component-wise multiplies resource number by {@code value} and rounds to Integers
     */
    public void multiply(double value) {
        setMoney((int) (getMoney() * value));
        setCloth((int) (getCloth() * value));
        setMetal((int) (getMetal() * value));
        setRum((int) (getRum() * value));
        setTobacco((int) (getTobacco() * value));
        setWood((int) (getWood() * value));
    }

    /**
     * Clamps all contained resources to be between min (inclusive) and max (inclusive)
     * Typical usage: clamp(0,Integer.MAX_VALUE);
     */
    public void clamp(int min, int max) {
        moneyProp.set(clampInt(min, moneyProp.get(), max));
        clothProp.set(clampInt(min, clothProp.get(), max));
        metalProp.set(clampInt(min, metalProp.get(), max));
        rumProp.set(clampInt(min, rumProp.get(), max));
        tobcoProp.set(clampInt(min, tobcoProp.get(), max));
        woodProp.set(clampInt(min, woodProp.get(), max));
    }

    /**
     * Clamps all contained resources component-wise to be between min (inclusive) and max (inclusive)
     */
    public void clamp(ResourceReadOnly min, ResourceReadOnly max) {
        setMoney(clampInt(min.getMoney(), getMoney(), max.getMoney()));
        setCloth(clampInt(min.getCloth(), getCloth(), max.getCloth()));
        setMetal(clampInt(min.getMetal(), getMetal(), max.getMetal()));
        setRum(clampInt(min.getRum(), getRum(), max.getRum()));
        setTobacco(clampInt(min.getTobacco(), getTobacco(), max.getTobacco()));
        setWood(clampInt(min.getWood(), getWood(), max.getWood()));
    }

    private int clampInt(int lower, int value, int upper) {
        return Math.max(lower, Math.min(upper, value));
    }


    public IntegerProperty moneyPropProperty() {
        return moneyProp;
    }


    public IntegerProperty clothProperty() {
        return clothProp;
    }


    public IntegerProperty metalProperty() {
        return metalProp;
    }


    public IntegerProperty rumProperty() {
        return rumProp;
    }


    public IntegerProperty tobaccoProperty() {
        return tobcoProp;
    }


    public IntegerProperty woodProperty() {
        return woodProp;
    }

    public void bindBidirectional(Resource other){
        this.moneyPropProperty()  .bindBidirectional(other.moneyPropProperty()   );
        this.clothProperty()  .bindBidirectional(other.clothProperty()   );
        this.metalProperty()  .bindBidirectional(other.metalProperty()   );
        this.rumProperty()    .bindBidirectional(other.rumProperty()     );
        this.tobaccoProperty().bindBidirectional(other.tobaccoProperty() );
        this.woodProperty()   .bindBidirectional(other.woodProperty()   );
    }

    public void bindBidirectional(ResourceM other){
        this.moneyPropProperty()  .bindBidirectional(other.money   );
        this.clothProperty()  .bindBidirectional(other.cloth   );
        this.metalProperty()  .bindBidirectional(other.metal   );
        this.rumProperty()    .bindBidirectional(other.rum     );
        this.woodProperty()   .bindBidirectional(other.wood );
        //tobbaco is not present in resourceM
    }

    /**
     * Same as bindBidirectional but in opposite binding order.
     * I.e. this function will do other.property().bindBidirectioal(this.property())
     * @param other
     */
    public void bindBidirectionalFrom(ResourceM other){
         other.money.bindBidirectional(this.moneyPropProperty() );
         other.cloth.bindBidirectional(this.clothProperty() );
         other.metal.bindBidirectional(this.metalProperty() );
         other.rum  .bindBidirectional(this.rumProperty()   );
         other.wood .bindBidirectional(this.woodProperty()  );
        //tobbaco is not present in resourceM
    }

}
