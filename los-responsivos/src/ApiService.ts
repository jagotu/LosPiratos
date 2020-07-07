import Team from "./models/Team";
import mockData from "./losTestos.json";
import ShipDetail, {ShipAction} from "./models/ShipDetail";
import Ship from "./models/Ship";

export const endpoints = {
    team: "/team",
    shipDetail: (id: string) => `/ship/${id}`,
    planAction: (shipId: string, action: ShipAction) => `/ship/${shipId}/actions/${action}`, // POST method
    deleteActions: (shipId: string) => `/ship/${shipId}/actions`, // DELETE method
}

export default class ApiService {
    static getTeamData(): Promise<Team>{
        console.log("service: get team data");
        return new Promise<Team>((resolve) => resolve(mockData.teams[0] as Team))
            .catch(e => {
                console.error("service error: get team data.",e);
                throw e;
            })
    }

    static getShipDetail(id: string): Promise<ShipDetail>{
        console.log("service: get detail of ship", id);
        const availableActions = [ShipAction.MoveForward,ShipAction.TurnLeft,ShipAction.TurnRight]
        const plannedActions = [ShipAction.MoveForward,ShipAction.MoveForward,ShipAction.TurnRight]
        const ship: Ship = mockData.teams[0].ships.filter(s => s.id === id)[0] as Ship;
        return new Promise<ShipDetail>((resolve) => resolve({
            ...ship,
            availableActions,
            plannedActions
        })).catch(e => {
            console.error("service error: get ship detail.",e);
            throw e;
        });
    }

    static deleteActions(shipId: string): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            console.log("service: deleting all actions on ship", shipId)
            resolve();
        }).catch(e => {
            console.error("service error: delete actions.",e);
            throw e;
        });
    }

    static planAction(shipId: string, action: ShipAction): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            console.log("service: planning ", action, "on ship", shipId);
            resolve();
        }).catch(e => {
            console.error("service error: plan actions.",e);
            throw e;
        });
    }
}