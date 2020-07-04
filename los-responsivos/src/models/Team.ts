import Ship from "./Ship";

export default interface Team {
    name:         string;
    id:           string;
    color:        string;
    money:        number;
    ownedMetal:   number;
    ownedWood:    number;
    ownedCloth:   number;
    ownedRum:     number;
    ownedTobacco: number;
    ships:        Ship[];
}