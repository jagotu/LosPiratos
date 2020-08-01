import Game from "./Game";

export interface ExtendedShipDetail {
    shipId: string;
    maxHP: number;
    cannonsCount: number;
    cargoCapacity: number;
    speed: number;
}


export default interface EnrichedGame {
    game: Game;
    extendedShipDetails: Array<ExtendedShipDetail>;
    roundNo: number;
}