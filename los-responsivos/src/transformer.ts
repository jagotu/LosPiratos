import Team from "./models/Team";
import Ship from "./models/Ship";
import {Position} from "./models/commonModels";

export class Transformer {
    static transformTeam(input: any): Team {
        return {
            ...input,
            ships: input.ships.map(Transformer.transformShip)
        }
    }

    static transformShip(input: any): Ship {
        return {
            ...input,
            position: Transformer.transformPosition(input.position)
        }
    }

    static transformPosition(input: any): Position {
        return {
            ...input
        }
    }
}