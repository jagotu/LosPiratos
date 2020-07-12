import {Attack, Maneuver, Transaction} from "../../../models/ShipActions";

const actionLayout: {
    maneuver: Array<Array<Maneuver | null>>;
    attack: Array<Array<Attack | null>>;
    transaction: Array<Array<Transaction | null>>;
} = {
    maneuver: [
        [null, "MoveForward", null],
        ["TurnLeft", null, "TurnRight"],
    ],
    attack: [
        ["LeftCannonsSimpleVolley", "FrontalAssault", "RightCannonsSimpleVolley"],
        ["LeftCannonsChainShotVolley", null, "RightCannonsChainShotVolley"],
        ["LeftCannonsHeavyBallVolley", "MortarShot", "RightCannonsHeavyBallVolley"],
    ],
    transaction: [
        ["UnloadStorage", "Plunder", "BuyCommodity"],
        ["UpgradeShip", "BuyNewEnhancement", "SellCommodity"],
        ["RepairShipViaDowngrade", "RepairShipViaRepayment", "RepairEnhancement"],
    ],
};

export default actionLayout;