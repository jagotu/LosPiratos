import HexPosition from "./HexPosition";
import Resources from "./Resources";

export default interface Tile {
    location: HexPosition;
    plantationsResource: null | Resources;
    portName: null | string;
    content: "Sea" | "Shore" | "Port" | "Plantation" | "PlantationExtra";
}