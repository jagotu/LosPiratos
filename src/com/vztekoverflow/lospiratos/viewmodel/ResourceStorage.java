package com.vztekoverflow.lospiratos.viewmodel;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResourceStorage extends Resource {

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

        this.money.bindBidirectional(money);
        this.cloth.bindBidirectional(cloth);
        this.metal.bindBidirectional(metal);
        this.rum__.bindBidirectional(rum);
        this.tobco.bindBidirectional(tobacco);
        this.wood_.bindBidirectional(wood);


        this.cloth.addListener(__ -> roomLeft.invalidate());
        this.metal.addListener(__ -> roomLeft.invalidate());
        this.rum__.addListener(__ -> roomLeft.invalidate());
        this.tobco.addListener(__ -> roomLeft.invalidate());
        this.wood_.addListener(__ -> roomLeft.invalidate());
        this.capacity.addListener(__ -> roomLeft.invalidate());
    }

    private IntegerBinding capacity;

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
        return roomLeft.get() >= clothCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreMetal(int amount){
        return roomLeft.get() >= metalCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreRum(int amount){
        return roomLeft.get() >= rumCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreTobacco(int amount){
        return roomLeft.get() >= tobaccoCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreWood(int amount){
        return roomLeft.get() >= woodCargoUsageCoefficient * amount;
    }

    /* adds cloth to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean tryAddCloth(int amount){
        if(!canStoreMoreCloth(amount)) return false;
        cloth.set(getCloth()+amount);
        return true;
    }
    /* adds metal to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean tryAddMetal(int amount){
        if(!canStoreMoreMetal(amount)) return false;
        metal.set(getMetal()+amount);
        return true;
    }
    /* adds rum to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean tryAddRum(int amount){
        if(!canStoreMoreRum(amount)) return false;
        rum__.set(getRum()+amount);
        return true;
    }
    /* adds tobacco to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean tryAddTobacco(int amount){
        if(!canStoreMoreTobacco(amount)) return false;
        tobco.set(getTobacco()+amount);
        return true;
    }
    /* adds wood to the ships storage, if there is enough storage room
     * @returns value indicating whether the amount has been successfully added
     */
    public boolean tryAddWood(int amount){
        if(!canStoreMoreWood(amount)) return false;
        wood_.set(getWood()+amount);
        return true;
    }


    @Override
    public void addCloth(int value) {
        tryAddCloth(value);
    }

    @Override
    public void addMetal(int value) {
        tryAddMetal(value);
    }

    @Override
    public void addRum(int value) {
        tryAddRum(value);
    }

    @Override
    public void addTobacco(int value) {
        tryAddTobacco(value);
    }

    @Override
    public void addWood(int value) {
        tryAddWood(value);
    }
    //todo the setters do not check whether the capacity has been overflown
    //maybe this could be used as intended? It would allow to overcome storage limit if wanted

    public int getRoomLeft() {
        return roomLeft.get();
    }

    public IntegerBinding roomLeftProperty() {
        return roomLeft;
    }

    public int getCapacity() {
        return capacity.get();
    }

    public IntegerBinding capacityProperty() {
        return capacity;
    }
}