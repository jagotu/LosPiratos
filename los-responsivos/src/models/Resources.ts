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
            team.ownedCloth,
            team.ownedMetal,
            team.ownedRum,
            team.ownedWood,
        );
    }

    static fromShip(ship: Ship): Resources {
        return new Resources(
            ship.carriesMoney,
            ship.carriesClothUnits,
            ship.carriesMetalUnits,
            ship.carriesRumUnits,
            ship.carriesWoodUnits,
        );
    }

    static zero(): Resources {
        return new Resources(0, 0, 0, 0, 0);
    }
}