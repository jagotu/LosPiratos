import Team from "./models/Team";
import ShipDetail, {getMockShipDetail} from "./models/ShipDetail";
import {Enhancement, Enhancements, ShipAction, ShipActionParam} from "./models/ShipActions";
import {isModificationTransaction, Transaction} from "./models/Transactions";
import {teamId} from "./UserContext";
import axios from "axios";

const addressPrefix = "http://localhost:8001";

export const endpoints = {
    game: addressPrefix+"/game", // GET, vraci uplne ta stejna data jako kdyz se dava ulozit hru
    shipDetail: (shipId: string) => `/team/${teamId()}/ship/${shipId}`,
    planAction: (shipId: string, action: ShipAction) => `/team/${teamId()}/ship/${shipId}/actions/${action}`, // POST method
    planAndEvaluateModificationTransaction: (shipId: string, action: Transaction) => `/team/${teamId()}/ship/${shipId}/modification-transaction/${action}`, // POST method
    deleteAllActions: (shipId: string) => `/team/${teamId()}/ship/${shipId}/actions`, // DELETE method
    deleteSomeActions: (shipId: string, howMany: number) => `/team/${teamId()}/ship/${shipId}/actions/${howMany}`, // DELETE method
    possibleEnhancements: (shipId: string, action: ShipAction) => `/team/${teamId()}/ship/${shipId}/actions/${action}/plannable-enhancements`, // GET method
}

export default class ApiService {
    static login(username: string, password: string): Promise<{ teamId: string }> {
        console.log("login", username, password)
        return new Promise<{ teamId: string }>((resolve, reject) => {
            resolve({teamId: "team1"})
        });
    }

    static getTeamData(): Promise<Team> {
        console.log("service: get team data");
        // return new Promise<Team>((resolve) => resolve(mockData.teams[0] as Team))
        //     .catch(e => {
        //         console.error("service error: get team data.", e);
        //         throw e;
        //     })
        return  axios.get(endpoints.game)
            .then(response => response.data.teams[0] as Team)
    }

    static getShipDetail(id: string): Promise<ShipDetail> {
        console.log("service: get detail of ship", id);

        return new Promise<ShipDetail>
        ((resolve) => resolve(getMockShipDetail(id))
        ).catch(e => {
            console.error("service error: get ship detail.", e);
            throw e;
        });
    }

    static deleteActions(shipId: string): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            console.log("service: deleting all actions on ship", shipId)
            resolve();
        }).catch(e => {
            console.error("service error: delete actions.", e);
            throw e;
        });
    }

    static planAndPerformModificationTransaction
    (shipId: string, action: ShipAction, actionPayload?: ShipActionParam): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            console.log("service: planning and performing ", action, actionPayload, "on ship", shipId);
            resolve();
        }).catch(e => {
            console.error("service error: plan actions.", e);
            throw e;
        });
    }

    static planAction(shipId: string, action: ShipAction, actionPayload?: ShipActionParam): Promise<void> {
        if (isModificationTransaction(action)) {
            console.warn("ApiService, planning modification transaction. Did you mean to pland and perform it instead?", action);
        }
        return new Promise<void>((resolve, reject) => {
            console.log("service: planning ", action, actionPayload, "on ship", shipId);
            resolve();
        }).catch(e => {
            console.error("service error: plan actions.", e);
            throw e;
        });
    }


    static getPossibleEnhancements(shipId: string, action: ShipAction): Promise<Array<Enhancement>> {
        console.log("service: get possible enhancements of ship", shipId, "action", action);
        const result = Object.keys(Enhancements) as Array<Enhancement>;
        return new Promise<Array<Enhancement>>((resolve) => resolve(result))
            .catch(e => {
                console.error("service error: get possible enhancements.", e);
                throw e;
            });
    }
}