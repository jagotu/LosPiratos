import Team from "./Team";
import Ship from "./Ship";

export default class Resources {

    constructor(
        readonly money: number,
        readonly metal: number,
        readonly wood: number,
        readonly cloth: number,
        readonly rum: number,
        readonly tobacco: number,
    ) {
    }

    static fromTeam(team: Team): Resources {
        return new Resources(
            team.money,
            team.ownedMetal,
            team.ownedWood,
            team.ownedCloth,
            team.ownedRum,
            team.ownedTobacco
        );
    }

    static fromShip(ship: Ship): Resources {
        return new Resources(
            ship.carriesMoney,
            ship.carriesMetalUnits,
            ship.carriesWoodUnits,
            ship.carriesClothUnits,
            ship.carriesRumUnits,
            ship.carriesTobaccoUnits
        );
    }
}