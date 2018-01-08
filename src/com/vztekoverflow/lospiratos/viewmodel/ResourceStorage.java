package com.vztekoverflow.lospiratos.viewmodel;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResourceStorage {

    private static final int clothCargoUsageCoefficient = 1;
    private static final int metalCargoUsageCoefficient = 1;
    private static final int rumCargoUsageCoefficient = 1;
    private static final int tobaccoCargoUsageCoefficient = 1;
    private static final int woodCargoUsageCoefficient = 1;
    private static final int moneyCargoUsageCoefficient = 0;

    /*
     * @params: resource properties to bind the storage to
     */
    public ResourceStorage(IntegerProperty cloth, IntegerProperty metal, IntegerProperty rum, IntegerProperty tobacco, IntegerProperty wood, IntegerProperty money, IntegerBinding capacity) {

        this.capacity = capacity;

        this.cloth.bindBidirectional(cloth);
        this.metal.bindBidirectional(metal);
        this.rum.bindBidirectional(rum);
        this.tobacco.bindBidirectional(tobacco);
        this.wood.bindBidirectional(wood);

        this.money.bindBidirectional(money);

        this.cloth.addListener(__ -> roomLeft.invalidate());
        this.metal.addListener(__ -> roomLeft.invalidate());
        this.rum.addListener(__ -> roomLeft.invalidate());
        this.tobacco.addListener(__ -> roomLeft.invalidate());
        this.wood.addListener(__ -> roomLeft.invalidate());
    }

    private IntegerBinding capacity;
    private IntegerProperty cloth = new SimpleIntegerProperty();
    private IntegerProperty metal = new SimpleIntegerProperty();
    private IntegerProperty rum = new SimpleIntegerProperty();
    private IntegerProperty tobacco = new SimpleIntegerProperty();
    private IntegerProperty wood = new SimpleIntegerProperty();
    private IntegerProperty money = new SimpleIntegerProperty();

    private IntegerBinding roomLeft = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int result = capacity.get();
            result -= getCloth() * clothCargoUsageCoefficient;
            result -= getMetal() * metalCargoUsageCoefficient;
            result -= getRum() * rumCargoUsageCoefficient;
            result -= getTobacco() * tobaccoCargoUsageCoefficient;
            result -= getWood() * woodCargoUsageCoefficient;
            result -= getMoney() * moneyCargoUsageCoefficient;
            return result;
        }
    };


    public boolean canStoreMoreCloth(int amount){
        return roomLeft.intValue() < clothCargoUsageCoefficient * getCloth();
    }
    public boolean canStoreMoreMetal(int amount){
        return roomLeft.intValue() < metalCargoUsageCoefficient * getMetal();
    }
    public boolean canStoreMoreRum(int amount){
        return roomLeft.intValue() < rumCargoUsageCoefficient * getRum();
    }
    public boolean canStoreMoreTobacco(int amount){
        return roomLeft.intValue() < tobaccoCargoUsageCoefficient * getTobacco();
    }
    public boolean canStoreMoreWood(int amount){
        return roomLeft.intValue() < woodCargoUsageCoefficient * getWood();
    }

    /* adds cloth to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean addCloth(int amount){
        if(!canStoreMoreCloth(amount)) return false;
        cloth.set(getCloth()+amount);
        return true;
    }
    /* adds metal to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean addMetal(int amount){
        if(!canStoreMoreMetal(amount)) return false;
        metal.set(getMetal()+amount);
        return true;
    }
    /* adds rum to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean addRum(int amount){
        if(!canStoreMoreRum(amount)) return false;
        rum.set(getRum()+amount);
        return true;
    }
    /* adds tobacco to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean addTobacco(int amount){
        if(!canStoreMoreTobacco(amount)) return false;
        tobacco.set(getTobacco()+amount);
        return true;
    }
    /* adds wood to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean addWood(int amount){
        if(!canStoreMoreWood(amount)) return false;
        wood.set(getWood()+amount);
        return true;
    }

    //todo the setters do not check whether the capacity has been overflown

    public int getRoomLeft() {
        return roomLeft.get();
    }

    public IntegerBinding roomLeftProperty() {
        return roomLeft;
    }

    public int getMetal() {
        return metal.get();
    }

    public IntegerProperty metalProperty() {
        return metal;
    }

    public void setMetal(int metal) {
        this.metal.set(metal);
    }

    public int getWood() {
        return wood.get();
    }

    public IntegerProperty woodProperty() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood.set(wood);
    }

    public int getCloth() {
        return cloth.get();
    }

    public IntegerProperty clothProperty() {
        return cloth;
    }

    public void setCloth(int cloth) {
        this.cloth.set(cloth);
    }

    public int getRum() {
        return rum.get();
    }

    public IntegerProperty rumProperty() {
        return rum;
    }

    public void setRum(int rum) {
        this.rum.set(rum);
    }

    public int getTobacco() {
        return tobacco.get();
    }

    public IntegerProperty tobaccoProperty() {
        return tobacco;
    }

    public void setTobacco(int tobacco) {
        this.tobacco.set(tobacco);
    }

    public int getMoney() {
        return money.get();
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public void setMoney(int money) {
        this.money.set(money);
    }

    public int getCapacity() {
        return capacity.get();
    }

    public IntegerBinding capacityProperty() {
        return capacity;
    }
}
