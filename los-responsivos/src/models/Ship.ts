import HexPosition from "./HexPosition";
import Enhancements from "./Enhancements";

export type ShipType = "Schooner" | "Brig" | "Frigate" | "Galleon";

export default interface Ship {
    name: string;
    id: string;
    teamId: string;
    captain: string;
    type: ShipType;
    destroyed: boolean;
    XP: number;
    HP: number;
    orientationDeg: number;
    position: HexPosition;
    customAdditionalHPmax: number;
    customAdditionalCannons: number;
    customAdditionalSpeed: number;
    carriesMetalUnits: number;
    carriesWoodUnits: number;
    carriesClothUnits: number;
    carriesRumUnits: number;
    carriesTobaccoUnits: number;
    carriesMoney: number;
    enhancements: Enhancements;
    customExtensions: any;
    activeMechanics: any[];
}