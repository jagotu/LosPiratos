import Ship from "./Ship";

export default interface ShipDetail extends Ship {
    availableActions: Map<ShipAction, boolean>;
    plannedActions: Array<ShipAction>;
}

export enum ShipAction {
    MoveForward = "MoveForward",
    TurnLeft = "TurnLeft",
    TurnRight = "TurnRight",
}