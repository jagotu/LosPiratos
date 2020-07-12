import {ShipActions} from "../../../models/ShipActions";

const actionLayout = {
    maneuver: [
        [null, ShipActions.MoveForward, null],
        [ShipActions.TurnLeft, null, ShipActions.TurnRight],
    ],
    attack: [
        [ShipActions.LeftCannonsSimpleVolley, ShipActions.FrontalAssault, ShipActions.RightCannonsSimpleVolley],
        [ShipActions.LeftCannonsChainShotVolley, null, ShipActions.RightCannonsChainShotVolley],
        [ShipActions.LeftCannonsHeavyBallVolley, ShipActions.MortarShot, ShipActions.RightCannonsHeavyBallVolley],
    ],
    transaction: [
        [ShipActions.UnloadStorage, ShipActions.Plunder, ShipActions.BuyCommodity],
        [ShipActions.UpgradeShip, ShipActions.BuyNewEnhancement, ShipActions.SellCommodity],
        [ShipActions.RepairShipViaDowngrade, ShipActions.RepairShipViaRepayment, ShipActions.RepairEnhancement],
    ],
};

export default actionLayout;