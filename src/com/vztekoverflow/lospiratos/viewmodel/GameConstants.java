package com.vztekoverflow.lospiratos.viewmodel;

public class GameConstants {

    //region general:
    public static final ResourceReadOnly TEAM_INITIAL_RESOURCE = new ResourceReadOnly(500,10,10,10,0,10);

    //endregion
    //region ships:

    public static final int BASIC_SHIP_WEIGHT = 10000;
    /**
     * How much of the ships buying cost will be part of shipwreck when the ship dies.
     */
    public static final float SHIPS_COST_TO_WRECK_COEFF = 0.2f;

    //region ship attacks:
    public static final double HEAVY_BALL_DAMAGE_COEFFICIENT = 1.5;
    public static final int FRONTAL_ASSAULT_BONUS_DAMAGE_FRIGATE = 5;
    public static final int FRONTAL_ASSAULT_BONUS_DAMAGE_GALLEON = 10;
    public static final int FRONTAL_ASSAULT_BASIC_DAMAGE = 8;
    public static final int FRONTAL_ASSAULT_SPEED_BONUS_DAMAGE = 5;
    public static final int FRONTAL_ASSAULT_BASIC_SELF_DAMAGE = 5;
    public static final int MORTAR_DAMAGE = 12;

    //endregion ship attacks
    //region transactions:
    /**
     * Purchase cost (in money) is determined as scalar product of purchase commodities and this. Neutral coefficient is 1.
     */
    public static final ResourceReadOnly PURCHASE_COEFFICIENTS = new ResourceReadOnly(1, 6, 1000, 1000, 1000, 6);
    /**
     * Sell gain (in money) is determined as scalar product of sold commodities and this. Neutral coefficient is 1.
     */
    public static final ResourceReadOnly SELL_COEFFICIENTS = new ResourceReadOnly(1, 4, 10, 10, 0, 4);
    public static final double REPAIR_ENHANCEMENT_COST_COEFFICIENT = 0.1;
    //endregion transactions

    //endregion
    public static final int COLLISION_DAMAGE = FRONTAL_ASSAULT_BASIC_SELF_DAMAGE;

    //enhancement and ship costs are defined in respective files
    //enhancement and ship bonus values are defined in respective files

    //game board:

    public static final ResourceReadOnly PLANTATION_GENERAL_CAPACITY = new ResourceReadOnly(0, 100, 0, 200, 0, 200);
    public static final int PLANTATION_GENERAL_QUOTIENT = 5;
    public static final ResourceReadOnly PLANTATION_EXTRA_CAPACITY = PLANTATION_GENERAL_CAPACITY.times(2);
    public static final int PLANTATION_EXTRA_QUOTIENT = 6;

    //endregion
}
