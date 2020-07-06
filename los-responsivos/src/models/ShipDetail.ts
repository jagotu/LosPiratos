import Ship from "./Ship";

export default interface ShipDetail extends Ship {
    availableActions: Array<ShipAction>;
    plannedActions: Array<ShipAction>;
}

export enum ShipAction {
    MoveForward = "MoveForward",
    TurnLeft = "TurnLeft",
    TurnRight = "TurnRight",
}