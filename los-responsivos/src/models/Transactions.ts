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
    hasCost: boolean,
    needsEnhancement: boolean,
    needsCommodity: boolean,
    needsMoney: boolean,
}
export const transactionsParameters: {
    [key: string]: TransactionParameter
} = {
    BuyCommodity: {needsEnhancement: false, hasCost: true, needsCommodity: true, needsMoney: false},
    SellCommodity: {needsEnhancement: false, hasCost: true, needsCommodity: true, needsMoney: false},
    Plunder: {needsEnhancement: false, hasCost: false, needsCommodity: true, needsMoney: true},
    BuyNewEnhancement: {needsEnhancement: true, hasCost: true, needsCommodity: false, needsMoney: false},
    RepairEnhancement: {needsEnhancement: true, hasCost: true, needsCommodity: false, needsMoney: false},
    RepairShipViaDowngrade: {needsEnhancement: false, hasCost: true, needsCommodity: false, needsMoney: false},
    RepairShipViaRepayment: {needsEnhancement: false, hasCost: true, needsCommodity: false, needsMoney: false},
    UnloadStorage: {needsEnhancement: false, hasCost: true, needsCommodity: false, needsMoney: false},
    UpgradeShip: {needsEnhancement: false, hasCost: true,  needsCommodity: false, needsMoney: false},
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
