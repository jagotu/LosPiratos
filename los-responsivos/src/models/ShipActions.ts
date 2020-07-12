import Resources from "./Resources";

export const Maneuvers = {
    MoveForward: "MoveForward",
    TurnLeft: "TurnLeft",
    TurnRight: "TurnRight",
} as const;
export type Maneuver = typeof Maneuvers[keyof typeof Maneuvers];

export const Attacks = {
    LeftCannonsChainShotVolley : "LeftCannonsChainShotVolley",
    LeftCannonsHeavyBallVolley : "LeftCannonsHeavyBallVolley",
    LeftCannonsSimpleVolley : "LeftCannonsSimpleVolley",
    RightCannonsChainShotVolley : "RightCannonsChainShotVolley",
    RightCannonsHeavyBallVolley : "RightCannonsHeavyBallVolley",
    RightCannonsSimpleVolley : "RightCannonsSimpleVolley",
    FrontalAssault : "FrontalAssault",
    MortarShot : "MortarShot",
} as const;
export type Attack = typeof Attacks[keyof typeof Attacks];

export const Transactions = {
    BuyCommodity : "BuyCommodity",
    BuyNewEnhancement : "BuyNewEnhancement",
    Plunder : "Plunder",
    RepairEnhancement : "RepairEnhancement",
    RepairShipViaDowngrade : "RepairShipViaDowngrade",
    RepairShipViaRepayment : "RepairShipViaRepayment",
    SellCommodity : "SellCommodity",
    UnloadStorage : "UnloadStorage",
    UpgradeShip : "UpgradeShip"
} as const;
export type Transaction = typeof Transactions[keyof typeof Transactions];

export const ShipActions = {
    ...Maneuvers,
    ...Attacks,
    ...Transactions,
}
export type ShipAction = Attack | Maneuver | Transaction;

export type ShipActionKind = "maneuver" | "attack" | "transaction";

export interface ShipActionParam {
    target?: Position;
    amount?: Resources;
}
