import {ShipAction} from "../../../models/ShipActions";
import React, {ReactNode} from "react";
import "font-awesome/css/font-awesome.css";

type ShipActionDetail = {
    icon: ReactNode;
}

export const actionTranslations: Map<ShipAction, string> = new Map<ShipAction, string>([
    ["MoveForward", "pohyb kupředu"],
    ["TurnRight", "otočení vpravo"],
    ["UnloadStorage", "vyložení nákladu"],
    ["BuyCommodity", "nákup komodit"],
    ["SellCommodity", "prodej komodit"],
    ["BuyNewEnhancement", "zakoupení vylepšení"],
    ["RepairEnhancement", "oprava vylepšení"],
    ["RepairShipViaDowngrade", "oprava lodě pomocí downgrade"],
    ["RepairShipViaRepayment", "oprava lodě pomocí zaplacení"],
    ["UpgradeShip", "vylepšení typu lodi"],
    ["Plunder", "plundrování"],
    ["FrontalAssault", "čelní útok"],
    ["RightCannonsSimpleVolley", "salva děl na pravoboku"],
    ["RightCannonsHeavyBallVolley", "salva těžkou kulí na pravoboku"],
    ["RightCannonsChainShotVolley", "salva řetězovou střelou na pravoboku"],
    ["MortarShot", "střela z houfnice"],
    ["LeftCannonsChainShotVolley", "salva řetězovou střelou na levoboku"],
    ["LeftCannonsHeavyBallVolley", "salva těžkou kulí na levoboku"],
    ["LeftCannonsSimpleVolley", "salva děl na levoboku"],
    ["TurnLeft", "otočení vlevo"],
]);

export const actionDetails: Map<ShipAction, ShipActionDetail> = new Map<ShipAction, ShipActionDetail>([
    ["MoveForward", {icon: <span style={{fontSize: "1.5em"}} className="fa fa-angle-double-up"/>}],
    ["TurnRight", {icon: <span className="fa fa-repeat"/>}],
    ["TurnLeft", {icon: <span className="fa fa-undo"/>}],
    ["UnloadStorage", {icon: <span className="icon icon-transaction">K</span>}],
    ["BuyCommodity", {icon: <span className="icon icon-transaction larger">A</span>}],
    ["SellCommodity", {icon: <span className="icon icon-transaction larger">H</span>}],
    ["BuyNewEnhancement", {icon: <span className="icon icon-transaction larger">B</span>}],
    ["RepairEnhancement", {icon: <span className="icon icon-transaction">G</span>}],
    ["RepairShipViaDowngrade", {icon: <span className="icon icon-transaction larger">E</span>}],
    ["RepairShipViaRepayment", {icon: <span className="icon icon-transaction larger">F</span>}],
    ["UpgradeShip", {icon: <span className="icon icon-transaction">J</span>}],
    ["Plunder", {icon: <span className="icon icon-transaction">D</span>}],
    ["FrontalAssault",
        {icon: <span style={{transform: "rotate(120deg) scaleY(-1)"}} className="icon icon-lastminute">A</span>}
    ],
    ["LeftCannonsSimpleVolley", {icon: <span className="icon flip-X">E</span>}],
    ["RightCannonsSimpleVolley", {icon: <span className="icon">E</span>}],
    ["LeftCannonsHeavyBallVolley",
        {icon: <><span className="icon smaller">I</span><span className="icon miniLeft">E</span></>}
    ],
    ["LeftCannonsChainShotVolley",
        {icon: <><span className="icon smaller flip-X">H</span><span className="icon miniLeft">E</span></>}
    ],
    ["RightCannonsChainShotVolley",
        {icon: <><span className="icon smaller">H</span><span className="icon miniRight">E</span></>}
    ],
    ["RightCannonsHeavyBallVolley",
        {icon: <><span className="icon smaller">I</span><span className="icon miniRight">E</span></>}
    ],
    ["MortarShot", {icon: <span className="icon">K</span>}],
]);
