import Ship from "./Ship";
import mockData from "../losTestos.json";
import Resources from "./Resources";

export default interface ShipDetail extends Ship {
    /**
     * Available for planning, subset of visibleActions. <br />
     * For example, when user has not yet used CannonVolley, it is available.
     */
    plannableActions: Set<ShipAction>;
    /**
     * Visible, but not necessarily available for planning. <br />
     * For example move forward is always visible, even if the ship has already move 3x. <br />
     * On the other hand, MortarShot is not visible until the upgrade is bought.
     */
    visibleActions: Set<ShipAction>;
    /**
     * Actions that have already been planned by the user.
     */
    plannedActions: Array<ShipAction>;
}

export enum ShipAction {
    // maneuvers:
    MoveForward = "MoveForward",
    TurnLeft = "TurnLeft",
    TurnRight = "TurnRight",
    // attacks:
    LeftCannonsChainShotVolley = "LeftCannonsChainShotVolley",
    LeftCannonsHeavyBallVolley = "LeftCannonsHeavyBallVolley",
    LeftCannonsSimpleVolley = "LeftCannonsSimpleVolley",
    RightCannonsChainShotVolley = "RightCannonsChainShotVolley",
    RightCannonsHeavyBallVolley = "RightCannonsHeavyBallVolley",
    RightCannonsSimpleVolley = "RightCannonsSimpleVolley",
    FrontalAssault = "FrontalAssault",
    MortarShot = "MortarShot",
    // transactions:
    BuyCommodity = "BuyCommodity",
    BuyNewEnhancement = "BuyNewEnhancement",
    Plunder = "Plunder",
    RepairEnhancement = "RepairEnhancement",
    RepairShipViaDowngrade = "RepairShipViaDowngrade",
    RepairShipViaRepayment = "RepairShipViaRepayment",
    SellCommodity = "SellCommodity",
    UnloadStorage = "UnloadStorage",
    UpgradeShip = "UpgradeShip"
}

export interface ShipActionParam {
    target?: Position;
    amount?: Resources;
}

export const getMockShipDetail = (id: string): ShipDetail => {
    const availableActions = new Set([ShipAction.MoveForward, ShipAction.TurnLeft, ShipAction.TurnRight]);
    const visibleActions = new Set(Object.keys(ShipAction) as Array<ShipAction>);
    const plannedActions = [ShipAction.MoveForward, ShipAction.MoveForward, ShipAction.TurnRight];

    const ship: Ship = mockData.teams[0].ships.filter(s => s.id === id)[0] as Ship;

    return {
        ...ship,
        visibleActions,
        plannableActions: availableActions,
        plannedActions
    }
}