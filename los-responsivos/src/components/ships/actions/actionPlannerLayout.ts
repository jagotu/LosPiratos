import {Attack, Maneuver} from "../../../models/ShipActions";
import {Transaction} from "../../../models/Transactions";

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
        ["Plunder", "UnloadStorage", "BuyCommodity"],
        ["UpgradeShip", "BuyNewEnhancement", "SellCommodity"],
        ["RepairShipViaDowngrade", "RepairShipViaRepayment", "RepairEnhancement"],
    ],
};

export default actionLayout;