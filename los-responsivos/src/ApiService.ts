import Team from "./models/Team";
import mockData from "./losTestos.json";
import ShipDetail, {ShipAction} from "./models/ShipDetail";
import Ship from "./models/Ship";

export default class ApiService {
    static getTeamData(): Promise<Team>{
        return new Promise<Team>((resolve) => resolve(mockData.teams[0] as Team));
    }

    static getShipDetail(id: string): Promise<ShipDetail>{
        const availableActions = new Map<ShipAction, boolean>([
            [ShipAction.MoveForward, true],
            [ShipAction.TurnLeft, true],
            [ShipAction.TurnRight, false],
        ])
        const plannedActions = [ShipAction.MoveForward,ShipAction.MoveForward,ShipAction.TurnRight]
        const ship: Ship = mockData.teams[0].ships.filter(s => s.id === id)[0] as Ship;
        return new Promise<ShipDetail>((resolve) => resolve({
            ...ship,
            availableActions,
            plannedActions
        }));
    }
}