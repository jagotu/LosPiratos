import Resources from "./Resources";
import {isTransaction, Transaction, Transactions, transactionsParameters} from "./Transactions";

export const Maneuvers = {
    MoveForward: "MoveForward",
    TurnLeft: "TurnLeft",
    TurnRight: "TurnRight",
} as const;
export type Maneuver = typeof Maneuvers[keyof typeof Maneuvers];

export const Attacks = {
    LeftCannonsChainShotVolley: "LeftCannonsChainShotVolley",
    LeftCannonsHeavyBallVolley: "LeftCannonsHeavyBallVolley",
    LeftCannonsSimpleVolley: "LeftCannonsSimpleVolley",
    RightCannonsChainShotVolley: "RightCannonsChainShotVolley",
    RightCannonsHeavyBallVolley: "RightCannonsHeavyBallVolley",
    RightCannonsSimpleVolley: "RightCannonsSimpleVolley",
    FrontalAssault: "FrontalAssault",
    MortarShot: "MortarShot",
} as const;
export type Attack = typeof Attacks[keyof typeof Attacks];

export const ShipActions = {
    ...Maneuvers,
    ...Attacks,
    ...Transactions,
}
export type ShipAction = Attack | Maneuver | Transaction;
export type ShipActionKind = "maneuver" | "attack" | "transaction";
export const Enhancements = {
    CannonUpgrade: "CannonUpgrade",
    ChainShot: "ChainShot",
    HeavyShot: "HeavyShot",
    HullUpgrade: "HullUpgrade",
    Mortar: "Mortar",
    Ram: "Ram"
}
export type Enhancement = typeof Enhancements[keyof typeof Enhancements];

export const needsParameters = (action: ShipAction): boolean => {
    if (action === "MortarShot")
        return true;
    else if (isTransaction(action)) {
        const transaction = action as Transaction;
        return transactionsParameters[transaction].needsAmount ||
            transactionsParameters[transaction].needsEnhancement;
    } else return false;
}

export interface ShipActionParam {
    target?: Position;
    amount?: Resources;
    enhancement?: Enhancement;
}
