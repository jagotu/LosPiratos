import {ShipAction} from "../../../models/ShipActions";
import React, {ReactNode} from "react";

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
    ["MoveForward", {icon: null}],
    ["TurnRight", {icon: null}],
    ["TurnLeft", {icon: null}],
    ["UnloadStorage", {icon: <span className="icon icon-transaction">K</span>}],
    ["BuyCommodity", {icon: <span className="icon icon-transaction">A</span>}],
    ["SellCommodity", {icon: <span className="icon icon-transaction">H</span>}],
    ["BuyNewEnhancement", {icon: <span className="icon icon-transaction">B</span>}],
    ["RepairEnhancement", {icon: <span className="icon icon-transaction">G</span>}],
    ["RepairShipViaDowngrade", {icon: <span className="icon icon-transaction">E</span>}],
    ["RepairShipViaRepayment", {icon: <span className="icon icon-transaction">F</span>}],
    ["UpgradeShip", {icon: <span className="icon icon-transaction">J</span>}],
    ["Plunder", {icon: <span className="icon icon-transaction">D</span>}],
    ["FrontalAssault", {icon: <span className="icon icon-lastminute">A</span>}],
    ["LeftCannonsSimpleVolley", {icon: null}],
    ["LeftCannonsHeavyBallVolley", {icon: null}],
    ["LeftCannonsChainShotVolley", {icon: null}],
    ["MortarShot", {icon: null}],
    ["RightCannonsChainShotVolley", {icon: null}],
    ["RightCannonsHeavyBallVolley", {icon: null}],
    ["RightCannonsSimpleVolley", {icon: null}],
]);
