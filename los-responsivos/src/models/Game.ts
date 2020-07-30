import Team from "./Team";
import Tile from "./Tile";
import Wreck from "./Wreck";

export default interface Game {
    teams: Array<Team>;
    map: {
        tiles: Array<Tile>;
    },
    shipwrecks: Array<Wreck>;
}