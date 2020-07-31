import ShipDetail from "./models/ShipDetail";
import {Enhancement, ShipAction, ShipActionParam} from "./models/ShipActions";
import {isModificationTransaction} from "./models/Transactions";
import {teamId} from "./userContext";
import axios from "axios";
import querystring from "querystring";
import EnrichedGame from "./models/EnrichedGame";
import {ShipType} from "./models/Ship";
import CostResponse from "./models/CostResponse";

const addressPrefix = process.env.REACT_APP_BACKEND_URL;

export const endpoints = {
    game:addressPrefix + "/game", // GET, vraci uplne ta stejna data jako kdyz se dava ulozit hru
    shipDetail: (shipId: string) => addressPrefix + `/shipDetail?ship=${shipId}&team=${teamId()}`,
    planAction: (shipId: string) => addressPrefix + `/planAction?ship=${shipId}&team=${teamId()}`, // POST method
    getActionCost: (shipId: string) => addressPrefix + `/getActionCost?ship=${shipId}&team=${teamId()}`, // POST method
    planAndEvaluateModificationTransaction: (shipId: string) => addressPrefix + `/planActionAndCommit?ship=${shipId}&team=${teamId()}`, // POST method
    deleteActions: (shipId: string) => addressPrefix + `/deleteActions?ship=${shipId}&team=${teamId()}`, // POST method, screw REST
    createShip: () => addressPrefix + `/createShip?team=${teamId()}`, // POST method
    teamReadyState: () => addressPrefix + `/teamIsReady?team=${teamId()}`, // POST or GET method - set or get value
    getAvailableEnhancements: (shipId : string) => addressPrefix + `/getAvailableEnhancements?ship=${shipId}&team=${teamId()}`,
    login: addressPrefix + "/login",
    combatLog: addressPrefix + "/log",

    possibleEnhancements: (shipId: string, action: ShipAction) => `/team/${teamId()}/ship/${shipId}/actions/${action}/plannable-enhancements`, // GET method

}

export default class ApiService {
    static login(username: string, password: string): Promise<{ teamId: string }> {
        return axios.post(
            endpoints.login,
            querystring.stringify({username, password}),
            {withCredentials: true}
        )
            .then(response => response.data)
            .catch(e => {
                console.error("service error: login", e);
                throw e;
            });
    }

    static getGameData(): Promise<EnrichedGame> {
        console.log("service: get game data");
        return axios.get(endpoints.game)
            .then(response => response.data);
    }

    static getCombatLog(): Promise<string[]> {
        console.log("service: get combat log");
        return axios.get(endpoints.combatLog)
            .then(response => response.data);
    }

    static getShipDetail(id: string): Promise<ShipDetail> {
        console.log("service: get detail of ship", id);
        return axios.get(endpoints.shipDetail(id), {withCredentials: true})
            .then(response => ({
                ...response.data,
                plannableActions: new Set(response.data.plannableActions),
                visibleActions: new Set(response.data.visibleActions),
            }))
            .catch(e => {
                console.error("service error: get ship detail.", e);
                throw e;
            });
    }

    static deleteActions(shipId: string, howManyToKeep: number): Promise<void> {

        return axios.post(endpoints.deleteActions(shipId), howManyToKeep
            , {withCredentials: true})
            .then(() => {
            })
            .catch(e => {
                console.error("service error: delete actions", e);
                throw e;
            });
    }

    static planAction(shipId: string, action: ShipAction, actionPayload?: ShipActionParam, performModificationTransaction: boolean = false): Promise<void> {
        if (isModificationTransaction(action) && !performModificationTransaction) {
            console.warn("ApiService, planning modification transaction. Did you mean to plan and perform it instead?", action);
        }

        const endpoint = performModificationTransaction ? endpoints.planAndEvaluateModificationTransaction(shipId) : endpoints.planAction(shipId)

        return axios.post(
            endpoint,
            {action, actionPayload},
            {withCredentials: true}
        )
            .then(() => {
            })
            .catch(e => {
                console.error("service error: plan action", e);
                throw e;
            });
    }

    static getActionCost(shipId: string, action: ShipAction, actionPayload?: ShipActionParam): Promise<CostResponse> {
        return axios.post(
            endpoints.getActionCost(shipId),
            {action, actionPayload},
            {withCredentials: true}
        )
            .then(response => response.data)
            .catch(e => {
                console.error("service error: get action cost", e);
                throw e;
            });
    }

    static getPossibleEnhancementsForPurchase(shipId: string): Promise<Array<Enhancement>> {
        console.log("service: get possible enhancements of ship", shipId);
        return axios.get(endpoints.getAvailableEnhancements(shipId), {withCredentials: true})
            .then(response => response.data)
            .catch(e => {
                console.error("service error: get ship detail.", e);
                throw e;
            });

    }

    static buyNewShip(shipName: string, port: string, type: ShipType): Promise<void> {
        return axios.post(endpoints.createShip(), {
            shipName,port,type}
            , {withCredentials: true})
            .then(() => {
            })
            .catch(e => {
                console.error("service error: buy ship", e);
                throw e;
            });
    }

    static setTeamReadyState(): Promise<void> {
        console.log("service: set team is ready");
        return axios.post(endpoints.teamReadyState())
            .then(() => {})
            .catch(e => {
                console.error("service error: set team ready state", e);
                throw e;
            });
    }

    static getTeamReadyState(): Promise<boolean> {
        return axios.post(endpoints.teamReadyState())
            .then((response) => response.data.ready)
            .catch(e => {
                console.error("service error: get team ready state", e);
                throw e;
            });
    }
}