package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.PartialOrdering;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResourceImmutable {

    protected IntegerProperty money = new SimpleIntegerProperty(0);
    protected IntegerProperty cloth = new SimpleIntegerProperty(0);
    protected IntegerProperty metal = new SimpleIntegerProperty(0);
    //those wierd names for rum, tobacco and wood are so that all resource have same number of characters (better code readibility)
    protected IntegerProperty rum__ = new SimpleIntegerProperty(0);
    protected IntegerProperty tobco = new SimpleIntegerProperty(0);
    protected IntegerProperty wood_ = new SimpleIntegerProperty(0);

    public ResourceImmutable(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        this.money.set(money);
        this.cloth.set(cloth);
        this.metal.set(metal);
        this.rum__.set(rum);
        this.tobco.set(tobacco);
        this.wood_.set(wood);
    }

    public ResourceImmutable() {
    }
    public ResourceImmutable createCopy() {
        return new ResourceImmutable(this.money.get(), this.cloth.get(), this.metal.get(), this.rum__.get(), this.tobco.get(), this.wood_.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!ResourceImmutable.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        ResourceImmutable r = (ResourceImmutable) obj;
        return equals(r.money.get(), r.cloth.get(), r.metal.get(), r.wood_.get(), r.rum__.get(), r.tobco.get());
    }

    public boolean equals(int money, int cloth, int metal, int wood, int rum, int tobacco) {
        return this.money.get() == money &&
                this.cloth.get() == cloth &&
                this.metal.get() == metal &&
                this.rum__.get() == rum &&
                this.tobco.get() == tobacco &&
                this.wood_.get() == wood;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(money.get()) +
                Integer.hashCode(cloth.get()) +
                Integer.hashCode(metal.get()) +
                Integer.hashCode(wood_.get()) +
                Integer.hashCode(rum__.get()) +
                Integer.hashCode(tobco.get());
    }

    public boolean isGreaterThanOrEqual(ResourceImmutable r){
        PartialOrdering result = this.compare(r);
        return (result == PartialOrdering.GreaterThanOrEqual || result == PartialOrdering.GreaterThan || result == PartialOrdering.Equal);
    }
    public boolean isLesserThanOrEqual(ResourceImmutable r){
        PartialOrdering result = this.compare(r);
        return (result == PartialOrdering.LessThanOrEqual || result == PartialOrdering.LessThan || result == PartialOrdering.Equal);
    }

    /*
         * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
         * @returns PartialOrdering, trying to find the most precise result (in the sense of inclusion)
         * I.e., if, in every component, the object's value > argument's value, @returns GreaterThan
         *    Then, if, in every component, the object's value equals, @returns Equals
         *    Then, if, in every component, the object's value >= argument's value, @returns GreaterThanOrEqual
         *    Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
         * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
         *     (Or use the method isGreaterThanOrEqual)
         * Similarly for < and <=.
         * @returns Uncomparable if in one component holds > and in other <.
         */
    public PartialOrdering compare(ResourceImmutable r) {
        return compare(r.money.get(), r.cloth.get(), r.metal.get(), r.wood_.get(), r.rum__.get(), r.tobco.get());
    }


    /*
     * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
     * @returns PartialOrdering, trying to find the most precise result (in the sense of inclusion)
     * I.e., if, in every component, the object's value > argument's value, @returns GreaterThan
     *    Then, if, in every component, the object's value equals, @returns Equals
     *    Then, if, in every component, the object's value >= argument's value, @returns GreaterThanOrEqual
     *    Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
     * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
     *     (Or use the method isGreaterThanOrEqual)
     * Similarly for < and <=.
     * @returns Uncomparable if in one component holds > and in other <.
     */
    public PartialOrdering compare(int money, int cloth, int metal, int wood, int rum, int tobacco) {
        if (this.equals(money, cloth, metal, wood, rum, tobacco)) return PartialOrdering.Equal;
        if (this.money.get() > money &&
                this.cloth.get() > cloth &&
                this.metal.get() > metal &&
                this.rum__.get() > rum &&
                this.tobco.get() > tobacco &&
                this.wood_.get() > wood) return PartialOrdering.GreaterThan;
        if (this.money.get() >= money &&
                this.cloth.get() >= cloth &&
                this.metal.get() >= metal &&
                this.rum__.get() >= rum &&
                this.tobco.get() >= tobacco &&
                this.wood_.get() >= wood) return PartialOrdering.GreaterThanOrEqual;
        if (this.money.get() < money &&
                this.cloth.get() < cloth &&
                this.metal.get() < metal &&
                this.rum__.get() < rum &&
                this.tobco.get() < tobacco &&
                this.wood_.get() < wood) return PartialOrdering.LessThan;
        if (this.money.get() <= money &&
                this.cloth.get() <= cloth &&
                this.metal.get() <= metal &&
                this.rum__.get() <= rum &&
                this.tobco.get() <= tobacco &&
                this.wood_.get() <= wood) return PartialOrdering.LessThanOrEqual;
        //otherwise
        return PartialOrdering.Uncomparable;
    }

    public int getMoney() {
        return money.get();
    }

    public ReadOnlyIntegerProperty moneyProperty() {
        return money;
    }

    public int getCloth() {
        return cloth.get();
    }

    public ReadOnlyIntegerProperty clothProperty() {
        return cloth;
    }

    public int getMetal() {
        return metal.get();
    }

    public ReadOnlyIntegerProperty metalProperty() {
        return metal;
    }

    public int getRum() {
        return rum__.get();
    }

    public ReadOnlyIntegerProperty rumProperty() {
        return rum__;
    }

    public int getTobacco() {
        return tobco.get();
    }

    public ReadOnlyIntegerProperty tobaccoProperty() {
        return tobco;
    }

    public int getWood() {
        return wood_.get();
    }

    public ReadOnlyIntegerProperty woodProperty() {
        return wood_;
    }

    /*
     * Creates a new instance of Resource that is mutable and contains copies of original values
     */
    public Resource toMutable(){
        return new Resource(this.money.get(), this.cloth.get(), this.metal.get(), this.rum__.get(), this.tobco.get(), this.wood_.get());
    }

}
