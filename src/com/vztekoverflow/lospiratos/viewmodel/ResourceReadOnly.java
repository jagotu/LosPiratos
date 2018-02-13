package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.PartialOrdering;

public class ResourceReadOnly {

    public static final ResourceReadOnly ZERO = new ResourceReadOnly();
    public static final ResourceReadOnly MAX = new ResourceReadOnly(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);

    private int money;
    private int cloth;
    private int metal;
    //those weird names for rum, tobacco and wood are so that all resource have same number of characters (better code readability)
    private int rum__;
    private int tobco;
    private int wood_;

    public ResourceReadOnly(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        this.money = (money);
        this.cloth = (cloth);
        this.metal = (metal);
        this.rum__ = (rum);
        this.tobco = (tobacco);
        this.wood_ = (wood);
    }

    public ResourceReadOnly(ResourceReadOnly original) {
        this.money = (original.money);
        this.cloth = (original.cloth);
        this.metal = (original.metal);
        this.rum__ = (original.rum__);
        this.tobco = (original.tobco);
        this.wood_ = (original.wood_);
    }

    public ResourceReadOnly() {
    }

    public ResourceReadOnly createCopy() {
        return new ResourceReadOnly(this.money,
                this.cloth,
                this.metal,
                this.rum__,
                this.tobco,
                this.wood_);
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
        return equals(r.money,
                r.cloth,
                r.metal,
                r.wood_,
                r.rum__,
                r.tobco);
    }

    public boolean equals(int money, int cloth, int metal, int wood, int rum, int tobacco) {
        return this.money == money &&
                this.cloth == cloth &&
                this.metal == metal &&
                this.rum__ == rum &&
                this.tobco == tobacco &&
                this.wood_ == wood;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(money) +
                Integer.hashCode(cloth) +
                Integer.hashCode(metal) +
                Integer.hashCode(wood_) +
                Integer.hashCode(rum__) +
                Integer.hashCode(tobco);
    }

    public boolean isGreaterThanOrEqual(ResourceReadOnly r) {
        PartialOrdering result = this.compare(r);
        return (result == PartialOrdering.GreaterThanOrEqual || result == PartialOrdering.GreaterThan || result == PartialOrdering.Equal);
    }

    public boolean isLesserThanOrEqual(ResourceReadOnly r) {
        PartialOrdering result = this.compare(r);
        return (result == PartialOrdering.LessThanOrEqual || result == PartialOrdering.LessThan || result == PartialOrdering.Equal);
    }

    /**
     * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
     *
     * The function tries to find the most precise result (in the sense of inclusion)
     * I.e., if, in every component, the object's value > argument's value, returns GreaterThan
     * Then, if, in every component, the object's value equals, returns Equals
     * Then, if, in every component, the object's value >= argument's value, returns GreaterThanOrEqual
     *
     * Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
     * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
     * (Or use the method isGreaterThanOrEqual). Similarly for < and <=.
     * @returns A PartialOrdering. Uncomparable if in one component holds '>' and in some other '<'.
     */
    public PartialOrdering compare(ResourceReadOnly r) {
        return compare(r.money,
                r.cloth,
                r.metal,
                r.wood_,
                r.rum__,
                r.tobco);
    }

    /**
     * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
     *
     * The function tries to find the most precise result (in the sense of inclusion)
     * I.e., if, in every component, the object's value > argument's value, returns GreaterThan
     * Then, if, in every component, the object's value equals, returns Equals
     * Then, if, in every component, the object's value >= argument's value, returns GreaterThanOrEqual
     *
     * Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
     * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
     * (Or use the method isGreaterThanOrEqual). Similarly for < and <=.
     * @returns A PartialOrdering. Uncomparable if in one component holds '>' and in some other '<'.
     */
    public PartialOrdering compare(int money, int cloth, int metal, int wood, int rum, int tobacco) {
        if (this.equals(money, cloth, metal, wood, rum, tobacco)) return PartialOrdering.Equal;
        if (this.money > money &&
                this.cloth > cloth &&
                this.metal > metal &&
                this.rum__ > rum &&
                this.tobco > tobacco &&
                this.wood_ > wood) return PartialOrdering.GreaterThan;
        if (this.money >= money &&
                this.cloth >= cloth &&
                this.metal >= metal &&
                this.rum__ >= rum &&
                this.tobco >= tobacco &&
                this.wood_ >= wood) return PartialOrdering.GreaterThanOrEqual;
        if (this.money < money &&
                this.cloth < cloth &&
                this.metal < metal &&
                this.rum__ < rum &&
                this.tobco < tobacco &&
                this.wood_ < wood) return PartialOrdering.LessThan;
        if (this.money <= money &&
                this.cloth <= cloth &&
                this.metal <= metal &&
                this.rum__ <= rum &&
                this.tobco <= tobacco &&
                this.wood_ <= wood) return PartialOrdering.LessThanOrEqual;
        //otherwise
        return PartialOrdering.Uncomparable;
    }

    public int getMoney() {
        return money;
    }

    public int getCloth() {
        return cloth;
    }

    public int getMetal() {
        return metal;
    }

    public int getRum() {
        return rum__;
    }

    public int getTobacco() {
        return tobco;
    }

    public int getWood() {
        return wood_;
    }

    /**
     * Creates a new instance of Resource that is mutable and contains copies of original values
     */
    public Resource createMutableCopy() {
        return new Resource(this.money,
                this.cloth,
                this.metal,
                this.rum__,
                this.tobco,
                this.wood_);
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly times(double value) {
        ResourceReadOnly v = new ResourceReadOnly(this);
        v.money = ((int) (v.money * value));
        v.cloth = ((int) (v.cloth * value));
        v.metal = ((int) (v.metal * value));
        v.rum__ = ((int) (v.rum__ * value));
        v.tobco = ((int) (v.tobco * value));
        v.wood_ = ((int) (v.wood_ * value));
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly plus(ResourceReadOnly rightOperand) {
        ResourceReadOnly v = new ResourceReadOnly();
        v.money = (this.money + rightOperand.money);
        v.cloth = (this.cloth + rightOperand.cloth);
        v.metal = (this.metal + rightOperand.metal);
        v.rum__ = (this.rum__ + rightOperand.rum__);
        v.tobco = (this.tobco + rightOperand.tobco);
        v.wood_ = (this.wood_ + rightOperand.wood_);
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly timesComponenWise(ResourceReadOnly rightOperand) {
        ResourceReadOnly v = new ResourceReadOnly();
        v.money = (this.money * rightOperand.money);
        v.cloth = (this.cloth * rightOperand.cloth);
        v.metal = (this.metal * rightOperand.metal);
        v.rum__ = (this.rum__ * rightOperand.rum__);
        v.tobco = (this.tobco * rightOperand.tobco);
        v.wood_ = (this.wood_ * rightOperand.wood_);
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public int scalarProduct(ResourceReadOnly rightOperand) {
        int result = 0;
        result += (this.money * rightOperand.money);
        result += (this.cloth * rightOperand.cloth);
        result += (this.metal * rightOperand.metal);
        result += (this.rum__ * rightOperand.rum__);
        result += (this.tobco * rightOperand.tobco);
        result += (this.wood_ * rightOperand.wood_);
        return result;
    }

    public static ResourceReadOnly fromMoney(int value) {
        return new ResourceReadOnly(value, 0, 0, 0, 0, 0);
    }

}
