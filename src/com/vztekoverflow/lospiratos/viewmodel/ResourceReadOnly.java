package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.PartialOrdering;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ResourceReadOnly {
    //todo tahle trida ve skutecnosti neni read only, na tech properties jde normalne volat set
    //reseni: at to vubec nejsou properties, ale jenom hodnoty; properties at ma az Resource

    public static final ResourceReadOnly ZERO = new ResourceReadOnly();

    protected IntegerProperty money = new SimpleIntegerProperty(0);
    protected IntegerProperty cloth = new SimpleIntegerProperty(0);
    protected IntegerProperty metal = new SimpleIntegerProperty(0);
    //those weird names for rum, tobacco and wood are so that all resource have same number of characters (better code readability)
    protected IntegerProperty rum__ = new SimpleIntegerProperty(0);
    protected IntegerProperty tobco = new SimpleIntegerProperty(0);
    protected IntegerProperty wood_ = new SimpleIntegerProperty(0);

    public ResourceReadOnly(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        this.money.set(money);
        this.cloth.set(cloth);
        this.metal.set(metal);
        this.rum__.set(rum);
        this.tobco.set(tobacco);
        this.wood_.set(wood);
    }

    public ResourceReadOnly(ResourceReadOnly original) {
        this.money.set(original.money.get());
        this.cloth.set(original.cloth.get());
        this.metal.set(original.metal.get());
        this.rum__.set(original.rum__.get());
        this.tobco.set(original.tobco.get());
        this.wood_.set(original.wood_.get());
    }

    public ResourceReadOnly() {
    }

    public ResourceReadOnly createCopy() {
        return new ResourceReadOnly(this.money.get(), this.cloth.get(), this.metal.get(), this.rum__.get(), this.tobco.get(), this.wood_.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!ResourceReadOnly.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        ResourceReadOnly r = (ResourceReadOnly) obj;
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

    public boolean isGreaterThanOrEqual(ResourceReadOnly r) {
        PartialOrdering result = this.compare(r);
        return (result == PartialOrdering.GreaterThanOrEqual || result == PartialOrdering.GreaterThan || result == PartialOrdering.Equal);
    }

    public boolean isLesserThanOrEqual(ResourceReadOnly r) {
        PartialOrdering result = this.compare(r);
        return (result == PartialOrdering.LessThanOrEqual || result == PartialOrdering.LessThan || result == PartialOrdering.Equal);
    }

    //TODO: odmrdat tento dokumentační komentář

    /**
     * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
     *
     * @return Uncomparable if in one component holds > and in other <.
     */
    public PartialOrdering compare(ResourceReadOnly r) {
        return compare(r.money.get(), r.cloth.get(), r.metal.get(), r.wood_.get(), r.rum__.get(), r.tobco.get());
    }


    //TODO: odmrdat tento dokumentační komentář

    /**
     * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
     *
     * @returns PartialOrdering, trying to find the most precise result (in the sense of inclusion)
     * I.e., if, in every component, the object's value > argument's value, @returns GreaterThan
     * Then, if, in every component, the object's value equals, @returns Equals
     * Then, if, in every component, the object's value >= argument's value, @returns GreaterThanOrEqual
     * Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
     * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
     * (Or use the method isGreaterThanOrEqual)
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

    /**
     * Creates a new instance of Resource that is mutable and contains copies of original values
     */
    public Resource createMutableCopy() {
        return new Resource(this.money.get(), this.cloth.get(), this.metal.get(), this.rum__.get(), this.tobco.get(), this.wood_.get());
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly times(double value) {
        ResourceReadOnly v = new ResourceReadOnly(this);
        v.money.set((int) (v.money.get() * value));
        v.cloth.set((int) (v.cloth.get() * value));
        v.metal.set((int) (v.metal.get() * value));
        v.rum__.set((int) (v.rum__.get() * value));
        v.tobco.set((int) (v.tobco.get() * value));
        v.wood_.set((int) (v.wood_.get() * value));
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly plus(ResourceReadOnly rightOperand) {
        ResourceReadOnly v = new ResourceReadOnly();
        v.money.set(this.money.get() + rightOperand.money.get());
        v.cloth.set(this.cloth.get() + rightOperand.cloth.get());
        v.metal.set(this.metal.get() + rightOperand.metal.get());
        v.rum__.set(this.rum__.get() + rightOperand.rum__.get());
        v.tobco.set(this.tobco.get() + rightOperand.tobco.get());
        v.wood_.set(this.wood_.get() + rightOperand.wood_.get());
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly timesComponenWise(ResourceReadOnly rightOperand) {
        ResourceReadOnly v = new ResourceReadOnly();
        v.money.set(this.money.get() * rightOperand.money.get());
        v.cloth.set(this.cloth.get() * rightOperand.cloth.get());
        v.metal.set(this.metal.get() * rightOperand.metal.get());
        v.rum__.set(this.rum__.get() * rightOperand.rum__.get());
        v.tobco.set(this.tobco.get() * rightOperand.tobco.get());
        v.wood_.set(this.wood_.get() * rightOperand.wood_.get());
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public int scalarProduct(ResourceReadOnly rightOperand) {
        int result = 0;
        result += (this.money.get() * rightOperand.money.get());
        result += (this.cloth.get() * rightOperand.cloth.get());
        result += (this.metal.get() * rightOperand.metal.get());
        result += (this.rum__.get() * rightOperand.rum__.get());
        result += (this.tobco.get() * rightOperand.tobco.get());
        result += (this.wood_.get() * rightOperand.wood_.get());
        return result;
    }

    public static ResourceReadOnly fromMoney(int value) {
        return new ResourceReadOnly(value, 0, 0, 0, 0, 0);
    }

}
