package com.vztekoverflow.lospiratos.viewmodel;

import javafx.beans.property.IntegerProperty;

public class Resource extends ResourceReadOnly {
    public Resource(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        super(money, cloth, metal, rum, tobacco, wood);
    }

    public Resource() {
    }

    public void setAll(ResourceReadOnly value) {
        setMoney(value.getMoney());
        setCloth(value.getCloth());
        setMetal(value.getMetal());
        setTobacco(value.getTobacco());
        setRum(value.getRum());
        setWood(value.getWood());
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
     * component-wise increases resource number by value in {@code value}
     */
    public void add(ResourceReadOnly value) {
        addMoney(value.money.get());
        addCloth(value.cloth.get());
        addMetal(value.metal.get());
        addRum(value.rum__.get());
        addTobacco(value.tobco.get());
        addWood(value.wood_.get());
    }

    /**
     * component-wise decreases resource number by value in {@code value}
     */
    public void subtract(ResourceReadOnly value) {
        addMoney(-value.money.get());
        addCloth(-value.cloth.get());
        addMetal(-value.metal.get());
        addRum(-value.rum__.get());
        addTobacco(-value.tobco.get());
        addWood(-value.wood_.get());
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

    private int clampInt(int lower, int value, int upper) {
        return Math.max(lower, Math.min(upper, value));
    }

    @Override
    public IntegerProperty moneyProperty() {
        return money;
    }

    @Override
    public IntegerProperty clothProperty() {
        return cloth;
    }

    @Override
    public IntegerProperty metalProperty() {
        return metal;
    }

    @Override
    public IntegerProperty rumProperty() {
        return rum__;
    }

    @Override
    public IntegerProperty tobaccoProperty() {
        return tobco;
    }

    @Override
    public IntegerProperty woodProperty() {
        return wood_;
    }

}
