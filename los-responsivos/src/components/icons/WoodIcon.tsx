import React from "react";
import './icons.css';

const WoodIcon: React.FC = () => {
    return (
        <span
            className="fa fa-tree icon-padding"
            style={{
                fontSize: "17px",
                lineHeight: "17px",
                paddingRight: 3,
            }}
        />
    );
}

export default WoodIcon;