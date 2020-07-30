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
    needsAmount: boolean, //obsolete
    needsEnhancement: boolean,
    hasCost: boolean,
    needsResource: boolean,
    needsMoney: boolean
}
export const transactionsParameters = {
    BuyCommodity: {needsAmount: true, needsEnhancement: false, hasCost: true, needsResource: true, needsMoney: false},
    SellCommodity: {needsAmount: true, needsEnhancement: false, hasCost: true, needsResource: true, needsMoney: false},
    Plunder: {needsAmount: true, needsEnhancement: false, hasCost: false, needsResource: true, needsMoney: false},
    BuyNewEnhancement: {needsAmount: false, needsEnhancement: true, hasCost: true, needsResource: false, needsMoney: false},
    RepairEnhancement: {needsAmount: false, needsEnhancement: true, hasCost: true, needsResource: false, needsMoney: false},
    // no params:
    RepairShipViaDowngrade: {needsAmount: false, needsEnhancement: false, hasCost: true, needsResource: false, needsMoney: false},
    RepairShipViaRepayment: {needsAmount: false, needsEnhancement: false, hasCost: true, needsResource: false, needsMoney: false},
    UnloadStorage: {needsAmount: false, needsEnhancement: false, hasCost: true, needsResource: false, needsMoney: false},
    UpgradeShip: {needsAmount: false, needsEnhancement: false, hasCost: true,  needsResource: false, needsMoney: false},
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
