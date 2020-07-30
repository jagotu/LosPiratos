import React from "react";
import './icons.css';

const MoneyIcon: React.FC = () => {
    return (
        <span
            className="fa fa-money"
            style={{
                fontSize: "18px",
                lineHeight: "17px",
                verticalAlign: "bottom",
                paddingRight: 4,
            }}
        />
    );
}

export default MoneyIcon;