import React from "react";
import {Position as PositionModel} from "../models/commonModels";
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