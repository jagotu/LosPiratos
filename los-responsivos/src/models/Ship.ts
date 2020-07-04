import {CustomExtensions, Position} from "./commonModels";

export default interface Ship {
    name:                    string;
    id:                      string;
    teamId:                  string;
    captain:                 string;
    type:                    "Schooner" | "Brig" | "Frigate" | "Galleon";
    destroyed:               boolean;
    XP:                      number;
    HP:                      number;
    orientationDeg:          number;
    position:                Position;
    customAdditionalHPmax:   number;
    customAdditionalCannons: number;
    customAdditionalSpeed:   number;
    carriesMetalUnits:       number;
    carriesWoodUnits:        number;
    carriesClothUnits:       number;
    carriesRumUnits:         number;
    carriesTobaccoUnits:     number;
    carriesMoney:            number;
    enhancements:            CustomExtensions;
    customExtensions:        CustomExtensions;
    activeMechanics:         any[];
}