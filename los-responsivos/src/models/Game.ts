import Team from "./Team";
import Tile from "./Tile";

export default interface Game {
    teams: Array<Team>;
    map: {
        tiles: Array<Tile>;
    }
}