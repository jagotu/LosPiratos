import HexPosition from "./HexPosition";
import Resources from "./Resources";

export default interface Tile {
    location: HexPosition;
    plantationsResource: null | Resources;
    content: "Sea" | "Shore" | "Port" | "Plantation" | "PlantationExtra";
}