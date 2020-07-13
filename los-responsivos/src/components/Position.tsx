import React from "react";
import PositionModel from "../models/Position";
import "./Position.css"

interface PositionProps {
    position: PositionModel
}

const Position: React.FC<PositionProps> = ({position}) => {
    return (
        <span className="position">({position.Q},{position.R})</span>
    );
}

export default Position;