import Team from "./Team";
import Ship from "./Ship";

export default class Resources {

    constructor(
        readonly money: number,
        readonly cloth: number,
        readonly metal: number,
        readonly rum: number,
        readonly wood: number,
    ) {
    }

    static fromTeam(team: Team): Resources {
        return new Resources(
            team.money,
            team.ownedMetal,
            team.ownedWood,
            team.ownedCloth,
            team.ownedRum,
        );
    }

    static fromShip(ship: Ship): Resources {
        return new Resources(
            ship.carriesMoney,
            ship.carriesMetalUnits,
            ship.carriesWoodUnits,
            ship.carriesClothUnits,
            ship.carriesRumUnits,
        );
    }

    static zero(): Resources {
        return new Resources(0, 0, 0, 0, 0);
    }
}