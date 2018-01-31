package com.vztekoverflow.lospiratos.viewmodel;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;

public class ResourceStorage extends Resource {

    private static final int clothCargoUsageCoefficient = 1;
    private static final int metalCargoUsageCoefficient = 1;
    private static final int rumCargoUsageCoefficient = 1;
    private static final int tobaccoCargoUsageCoefficient = 1;
    private static final int woodCargoUsageCoefficient = 1;
    private static final int moneyCargoUsageCoefficient = 0;

    /**
     * params resource properties to bind the storage to
     */
    public ResourceStorage(IntegerProperty cloth, IntegerProperty metal, IntegerProperty rum, IntegerProperty tobacco, IntegerProperty wood, IntegerProperty money, IntegerBinding capacity) {

        this.capacityMaximum = capacity;

        this.money.bindBidirectional(money);
        this.cloth.bindBidirectional(cloth);
        this.metal.bindBidirectional(metal);
        this.rum__.bindBidirectional(rum);
        this.tobco.bindBidirectional(tobacco);
        this.wood_.bindBidirectional(wood);


        this.cloth.addListener(__ -> capacityLeft.invalidate());
        this.metal.addListener(__ -> capacityLeft.invalidate());
        this.rum__.addListener(__ -> capacityLeft.invalidate());
        this.tobco.addListener(__ -> capacityLeft.invalidate());
        this.wood_.addListener(__ -> capacityLeft.invalidate());
        this.capacityMaximum.addListener(__ -> capacityLeft.invalidate());
    }

    private final IntegerBinding capacityMaximum;

    private final IntegerBinding capacityLeft = new IntegerBinding() {
        @Override
        protected int computeValue() {
            int result = capacityMaximum.get();
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
        return capacityLeft.get() >= clothCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreMetal(int amount){
        return capacityLeft.get() >= metalCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreRum(int amount){
        return capacityLeft.get() >= rumCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreTobacco(int amount){
        return capacityLeft.get() >= tobaccoCargoUsageCoefficient * amount;
    }
    public boolean canStoreMoreWood(int amount){
        return capacityLeft.get() >= woodCargoUsageCoefficient * amount;
    }

    /**
     * adds cloth to the ships storage, if there is enough storage room
     * @return value indicating whether the amount has been successfully added
     */
    public boolean tryAddCloth(int amount){
        if(!canStoreMoreCloth(amount)) return false;
        cloth.set(getCloth()+amount);
        return true;
    }
    /**
     * adds metal to the ships storage, if there is enough storage room
     * @return value indicating whether the amount has been successfully added
     */
    public boolean tryAddMetal(int amount){
        if(!canStoreMoreMetal(amount)) return false;
        metal.set(getMetal()+amount);
        return true;
    }
    /**
     * adds rum to the ships storage, if there is enough storage room
     * @return value indicating whether the amount has been successfully added
     */
    public boolean tryAddRum(int amount){
        if(!canStoreMoreRum(amount)) return false;
        rum__.set(getRum()+amount);
        return true;
    }
    /**
     * adds tobacco to the ships storage, if there is enough storage room
     * @return value indicating whether the amount has been successfully added
     */
    public boolean tryAddTobacco(int amount){
        if(!canStoreMoreTobacco(amount)) return false;
        tobco.set(getTobacco()+amount);
        return true;
    }
    /**
     * adds wood to the ships storage, if there is enough storage room
     * @return value indicating whether the amount has been successfully added
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

    public int getCapacityLeft() {
        return capacityLeft.get();
    }

    public IntegerBinding capacityLeftProperty() {
        return capacityLeft;
    }

    public int getCapacityMaximum() {
        return capacityMaximum.get();
    }

    public IntegerBinding capacityMaximumProperty() {
        return capacityMaximum;
    }
}
