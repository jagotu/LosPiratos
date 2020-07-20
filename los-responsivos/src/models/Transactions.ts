import {ShipAction} from "./ShipActions";

export const Transactions = {
    BuyCommodity: "BuyCommodity",
    BuyNewEnhancement: "BuyNewEnhancement",
    Plunder: "Plunder",
    RepairEnhancement: "RepairEnhancement",
    RepairShipViaDowngrade: "RepairShipViaDowngrade",
    RepairShipViaRepayment: "RepairShipViaRepayment",
    SellCommodity: "SellCommodity",
    UnloadStorage: "UnloadStorage",
    UpgradeShip: "UpgradeShip"
} as const;
export type Transaction = typeof Transactions[keyof typeof Transactions];

export const isTransaction = (a: string): boolean => (Object.values(Transactions) as Array<string>).includes(a);

export interface TransactionParameter {
    needsAmount: boolean,
    needsEnhancement: boolean,
}
export const transactionsParameters = {
    BuyCommodity: {needsAmount: true, needsEnhancement: false},
    SellCommodity: {needsAmount: true, needsEnhancement: false},
    Plunder: {needsAmount: true, needsEnhancement: false},
    BuyNewEnhancement: {needsAmount: false, needsEnhancement: true},
    RepairEnhancement: {needsAmount: false, needsEnhancement: true},
    // no params:
    RepairShipViaDowngrade: {needsAmount: false, needsEnhancement: false},
    RepairShipViaRepayment: {needsAmount: false, needsEnhancement: false},
    UnloadStorage: {needsAmount: false, needsEnhancement: false},
    UpgradeShip: {needsAmount: false, needsEnhancement: false},
}

export const modificationTransactions: Array<Transaction> = [
    Transactions.RepairShipViaDowngrade,
    Transactions.RepairShipViaRepayment,
    Transactions.UpgradeShip,
    Transactions.BuyNewEnhancement,
    Transactions.RepairEnhancement
]
export const isModificationTransaction = (t: ShipAction) => {
    return modificationTransactions.includes(t as Transaction);
}
