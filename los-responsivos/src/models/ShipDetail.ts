import Ship from "./Ship";
import mockData from "../losTestos.json";
import {ShipAction, ShipActions} from "./ShipActions";

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

export const getMockShipDetail = (id: string): ShipDetail => {
    const availableActions = new Set<ShipAction>([ShipActions.MoveForward, ShipActions.TurnLeft, ShipActions.TurnRight]);
    const visibleActions = new Set<ShipAction>(Object.keys(ShipActions) as Array<ShipAction>);
    const plannedActions = [ShipActions.MoveForward, ShipActions.MoveForward, ShipActions.TurnRight];

    const ship: Ship = mockData.teams[0].ships.filter(s => s.id === id)[0] as Ship;

    return {
        ...ship,
        visibleActions,
        plannableActions: availableActions,
        plannedActions
    }
}