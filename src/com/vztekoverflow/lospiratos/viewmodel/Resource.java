package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.model.ResourceM;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Resource extends ResourceReadOnly implements Observable {
    public Resource(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        super(money, cloth, metal, rum, tobacco, wood);
    }

    public Resource() {
    }

    protected IntegerProperty money = new SimpleIntegerProperty(0);
    protected IntegerProperty cloth = new SimpleIntegerProperty(0);
    protected IntegerProperty metal = new SimpleIntegerProperty(0);
    //those weird names for rum, tobacco and wood are so that all resource have same number of characters (better code readability)
    protected IntegerProperty rum__ = new SimpleIntegerProperty(0);
    protected IntegerProperty tobco = new SimpleIntegerProperty(0);
    protected IntegerProperty wood_ = new SimpleIntegerProperty(0);

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
        return money.get();
    }

    @Override
    public int getCloth() {
        return cloth.get();
    }

    @Override
    public int getMetal() {
        return metal.get();
    }

    @Override
    public int getRum() {
        return rum__.get();
    }

    @Override
    public int getWood() {
        return wood_.get();
    }

    @Override
    public int getTobacco() {
        return tobco.get();
    }

    public void setMoney(int money) {
        this.money.set(money);
    }

    public void setCloth(int cloth) {
        this.cloth.set(cloth);
    }

    public void setMetal(int metal) {
        this.metal.set(metal);
    }

    public void setRum(int rum) {
        this.rum__.set(rum);
    }

    public void setTobacco(int tobacco) {
        this.tobco.set(tobacco);
    }

    public void setWood(int wood) {
        this.wood_.set(wood);
    }

    public void addMoney(int value) {
        money.set(money.get() + value);
    }

    public void addCloth(int value) {
        cloth.set(cloth.get() + value);
    }

    public void addMetal(int value) {
        metal.set(metal.get() + value);
    }

    public void addRum(int value) {
        rum__.set(rum__.get() + value);
    }

    public void addTobacco(int value) {
        tobco.set(tobco.get() + value);
    }

    public void addWood(int value) {
        wood_.set(wood_.get() + value);
    }

    /**
     * The listener will listen for changes on any of the resources' component
     * @param listener
     */
    public void addListener(InvalidationListener listener){
        moneyProperty().addListener(listener);
        clothProperty().addListener(listener);
        metalProperty().addListener(listener);
        rumProperty().addListener(listener);
        tobaccoProperty().addListener(listener);
        woodProperty().addListener(listener);
    }
    public void removeListener(InvalidationListener listener){
        moneyProperty().removeListener(listener);
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
        money.set(clampInt(min, money.get(), max));
        cloth.set(clampInt(min, cloth.get(), max));
        metal.set(clampInt(min, metal.get(), max));
        rum__.set(clampInt(min, rum__.get(), max));
        tobco.set(clampInt(min, tobco.get(), max));
        wood_.set(clampInt(min, wood_.get(), max));
    }

    /**
     * Clamps all contained resources component-wise to be between min (inclusive) and max (inclusive)
     */
    public void clamp(ResourceReadOnly min, ResourceReadOnly max) {
        money.set(clampInt(min.getMoney(), money.get(), max.getMoney()));
        cloth.set(clampInt(min.getCloth(), cloth.get(), max.getCloth()));
        metal.set(clampInt(min.getMetal(), metal.get(), max.getMetal()));
        rum__.set(clampInt(min.getRum(), rum__.get(), max.getRum()));
        tobco.set(clampInt(min.getTobacco(), tobco.get(), max.getTobacco()));
        wood_.set(clampInt(min.getWood(), wood_.get(), max.getWood()));
    }

    private int clampInt(int lower, int value, int upper) {
        return Math.max(lower, Math.min(upper, value));
    }


    public IntegerProperty moneyProperty() {
        return money;
    }


    public IntegerProperty clothProperty() {
        return cloth;
    }


    public IntegerProperty metalProperty() {
        return metal;
    }


    public IntegerProperty rumProperty() {
        return rum__;
    }


    public IntegerProperty tobaccoProperty() {
        return tobco;
    }


    public IntegerProperty woodProperty() {
        return wood_;
    }

    public void bindBidirectional(Resource other){
        this.moneyProperty()  .bindBidirectional(other.moneyProperty()   );
        this.clothProperty()  .bindBidirectional(other.clothProperty()   );
        this.metalProperty()  .bindBidirectional(other.metalProperty()   );
        this.rumProperty()    .bindBidirectional(other.rumProperty()     );
        this.tobaccoProperty().bindBidirectional(other.tobaccoProperty() );
        this.woodProperty()   .bindBidirectional(other.woodProperty()   );
    }

    public void bindBidirectional(ResourceM other){
        this.moneyProperty()  .bindBidirectional(other.money   );
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
         other.money.bindBidirectional(this.moneyProperty() );
         other.cloth.bindBidirectional(this.clothProperty() );
         other.metal.bindBidirectional(this.metalProperty() );
         other.rum  .bindBidirectional(this.rumProperty()   );
         other.wood .bindBidirectional(this.woodProperty()  );
        //tobbaco is not present in resourceM
    }

}
