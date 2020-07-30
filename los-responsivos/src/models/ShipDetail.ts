import Ship from "./Ship";
import {ShipAction} from "./ShipActions";
import Resources from "./Resources";

export default interface ShipDetail {
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
    upgradeCost: Resources;
    repairCost: Resources;
    ship: Ship;
}