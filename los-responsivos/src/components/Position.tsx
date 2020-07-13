import React from "react";
import PositionModel from "../models/HexPosition";
import "./Position.css"

interface PositionProps {
    position?: PositionModel
}

const Position: React.FC<PositionProps> = ({position}) => {
    if (!position) return null;
    return (
        <span className="position">({position.Q},{position.R})</span>
    );
}

export default Position;