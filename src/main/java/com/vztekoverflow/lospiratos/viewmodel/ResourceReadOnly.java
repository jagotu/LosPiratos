package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.util.PartialOrdering;

public class ResourceReadOnly {

    public static final ResourceReadOnly ZERO = new ResourceReadOnly();
    public static final ResourceReadOnly ONE = new ResourceReadOnly(1, 1, 1, 1, 1, 1);
    public static final ResourceReadOnly MAX = new ResourceReadOnly(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final ResourceReadOnly MOCK_VALUE = new ResourceReadOnly(1092, 42, 42, 42, 0, 42);

    private final int money;
    private final int cloth;
    private final int metal;
    //those weird names for rum, tobacco and wood are so that all resource have same number of characters (better code readability)
    private final int rum__;
    private final int tobco;
    private final int wood_;

    public ResourceReadOnly(int money, int cloth, int metal, int rum, int tobacco, int wood) {
        this.money = (money);
        this.cloth = (cloth);
        this.metal = (metal);
        this.rum__ = (rum);
        this.tobco = (tobacco);
        this.wood_ = (wood);
    }

    private ResourceReadOnly(ResourceReadOnly original) {
        this.money = (original.money);
        this.cloth = (original.cloth);
        this.metal = (original.metal);
        this.rum__ = (original.rum__);
        this.tobco = (original.tobco);
        this.wood_ = (original.wood_);
    }

    public ResourceReadOnly() {
        money = 0;
        cloth = 0;
        metal = 0;
        rum__ = 0;
        tobco = 0;
        wood_ = 0;
    }

    public ResourceReadOnly createCopy() {
        return new ResourceReadOnly(this.getMoney(),
                this.getCloth(),
                this.getMetal(),
                this.getRum(),
                this.getTobacco(),
                this.getWood());
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
        return equals(r.getMoney(),
                r.getCloth(),
                r.getMetal(),
                r.getWood(),
                r.getRum(),
                r.getTobacco());
    }

    public boolean equals(int money, int cloth, int metal, int wood, int rum, int tobacco) {
        return this.getMoney() == money &&
                this.getCloth() == cloth &&
                this.getMetal() == metal &&
                this.getRum() == rum &&
                this.getTobacco() == tobacco &&
                this.getWood() == wood;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getMoney()) +
                Integer.hashCode(getCloth()) +
                Integer.hashCode(getMetal()) +
                Integer.hashCode(getWood()) +
                Integer.hashCode(getRum()) +
                Integer.hashCode(getTobacco());
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
     * <p>
     * The function tries to find the most precise result (in the sense of inclusion)
     * I.e., if, in every component, the object's value > argument's value, returns GreaterThan
     * Then, if, in every component, the object's value equals, returns Equals
     * Then, if, in every component, the object's value >= argument's value, returns GreaterThanOrEqual
     * <p>
     * Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
     * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
     * (Or use the method isGreaterThanOrEqual). Similarly for < and <=.
     *
     * @return A PartialOrdering. Uncomparable if in one component holds '>' and in some other '<'.
     */
    public PartialOrdering compare(ResourceReadOnly r) {
        return compare(r.getMoney(),
                r.getCloth(),
                r.getMetal(),
                r.getWood(),
                r.getRum(),
                r.getTobacco());
    }

    /**
     * Compares two resources component wise and returns a PartialOrdering that holds for each of the component
     * <p>
     * The function tries to find the most precise result (in the sense of inclusion)
     * I.e., if, in every component, the object's value > argument's value, returns GreaterThan
     * Then, if, in every component, the object's value equals, returns Equals
     * Then, if, in every component, the object's value >= argument's value, returns GreaterThanOrEqual
     * <p>
     * Thus, result GreaterThanOrEqual means that in at least one component, but not in all, the value is equal.
     * If you want to compare for >= in the usual sense, you have to test if (result == GreaterThan || result == GreaterThanOrEqual || result == Equal)
     * (Or use the method isGreaterThanOrEqual). Similarly for < and <=.
     *
     * @return A PartialOrdering. Uncomparable if in one component holds '>' and in some other '<'.
     */
    public PartialOrdering compare(int money, int cloth, int metal, int wood, int rum, int tobacco) {
        if (this.equals(money, cloth, metal, wood, rum, tobacco)) return PartialOrdering.Equal;
        if (this.getMoney() > money &&
                this.getCloth() > cloth &&
                this.getMetal() > metal &&
                this.getRum() > rum &&
                this.getTobacco() > tobacco &&
                this.getWood() > wood) return PartialOrdering.GreaterThan;
        if (this.getMoney() >= money &&
                this.getCloth() >= cloth &&
                this.getMetal() >= metal &&
                this.getRum() >= rum &&
                this.getTobacco() >= tobacco &&
                this.getWood() >= wood) return PartialOrdering.GreaterThanOrEqual;
        if (this.getMoney() < money &&
                this.getCloth() < cloth &&
                this.getMetal() < metal &&
                this.getRum() < rum &&
                this.getTobacco() < tobacco &&
                this.getWood() < wood) return PartialOrdering.LessThan;
        if (this.getMoney() <= money &&
                this.getCloth() <= cloth &&
                this.getMetal() <= metal &&
                this.getRum() <= rum &&
                this.getTobacco() <= tobacco &&
                this.getWood() <= wood) return PartialOrdering.LessThanOrEqual;
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
        return new Resource(getMoney(),
                getCloth(),
                getMetal(),
                getRum(),
                getTobacco(),
                getWood());
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly times(double value) {
        int money = ((int) (getMoney() * value));
        int cloth = ((int) (getCloth() * value));
        int metal = ((int) (getMetal() * value));
        int rum = ((int) (getRum() * value));
        int tobco = ((int) (getTobacco() * value));
        int wood = ((int) (getWood() * value));
        ResourceReadOnly v = new ResourceReadOnly(money, cloth, metal, rum, tobco, wood);
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly plus(ResourceReadOnly rightOperand) {
        int money = (this.getMoney() + rightOperand.getMoney());
        int cloth = (this.getCloth() + rightOperand.getCloth());
        int metal = (this.getMetal() + rightOperand.getMetal());
        int rum = (this.getRum() + rightOperand.getRum());
        int tobco = (this.getTobacco() + rightOperand.getTobacco());
        int wood = (this.getWood() + rightOperand.getWood());
        ResourceReadOnly v = new ResourceReadOnly(money, cloth, metal, rum, tobco, wood);
        return v;
    }
    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly minus(ResourceReadOnly rightOperand) {
        return this.plus(rightOperand.times(-1));
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public ResourceReadOnly timesComponentWise(ResourceReadOnly rightOperand) {
        int money = (this.getMoney() * rightOperand.getMoney());
        int cloth = (this.getCloth() * rightOperand.getCloth());
        int metal = (this.getMetal() * rightOperand.getMetal());
        int rum = (this.getRum() * rightOperand.getRum());
        int tobco = (this.getTobacco() * rightOperand.getTobacco());
        int wood = (this.getWood() * rightOperand.getWood());
        ResourceReadOnly v = new ResourceReadOnly(money, cloth, metal, rum, tobco, wood);
        return v;
    }

    /**
     * Result is rounded up to nearest integer value (ceil)
     * @param rightOperand
     * @return
     */
    public ResourceReadOnly divideComponentWise(ResourceReadOnly rightOperand) {
        int money = 0;
        if (rightOperand.getMoney() != 0)
            money = (int) ( Math.ceil((float) this.getMoney() / (float) (rightOperand.getMoney())));
        int cloth = 0;
        if (rightOperand.getCloth() != 0)
            cloth = (int) (Math.ceil((float)this.getCloth() / (float) rightOperand.getCloth()));
        int metal = 0;
        if (rightOperand.getMetal() != 0)
            metal = (int) (Math.ceil((float)this.getMetal() / (float) rightOperand.getMetal()));
        int rum = 0;
        if (rightOperand.getRum() != 0)
            rum = (int) Math.ceil((float)this.getRum() / (float) rightOperand.getRum());
        int tobco = 0;
        if (rightOperand.getTobacco() != 0)
            tobco = (int) (Math.ceil((float)this.getTobacco() / (float) rightOperand.getTobacco()));
        int wood = 0;
        if (rightOperand.getWood() != 0)
            wood = (int) Math.ceil((float)this.getWood() / (float) rightOperand.getWood());
        ResourceReadOnly v = new ResourceReadOnly(money, cloth, metal, rum, tobco, wood);
        return v;
    }

    /**
     * fluent syntax for creating new ResourceReadOnly form arithmetic expressions
     */
    public int scalarProduct(ResourceReadOnly rightOperand) {
        int result = 0;
        result += (this.getMoney() * rightOperand.getMoney());
        result += (this.getCloth() * rightOperand.getCloth());
        result += (this.getMetal() * rightOperand.getMetal());
        result += (this.getRum() * rightOperand.getRum());
        result += (this.getTobacco() * rightOperand.getTobacco());
        result += (this.getWood() * rightOperand.getWood());
        return result;
    }

    /**
     *
     * @return true if any of the resource's component is negative
     */
    public boolean anyComponentNegaitve(){
        return getMoney() < 0 ||
                getCloth() < 0 ||
                getMetal() < 0 ||
                getRum() < 0 ||
                getTobacco() < 0 ||
                getWood() < 0 ;
    }

    @Override
    public String toString() {
        char c = ',';
        return "(Money,Cloth,Metal,Rum,Wood):" + '('
                + getMoney() + c
                + getCloth() + c
                + getMetal() + c
                + getRum() + c
                + getWood()
                + ')';
    }

    public static ResourceReadOnly fromMoney(int value) {
        return new ResourceReadOnly(value, 0, 0, 0, 0, 0);
    }

}
