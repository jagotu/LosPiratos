import React from "react";
import ResourcesModel from "../models/Resources";
import {Grid} from "@material-ui/core";
import MetalIcon from "./icons/MetalIcon";
import ClothIcon from "./icons/ClothIcon";
import RumIcon from "./icons/RumIcon";
import "font-awesome/css/font-awesome.css";
import MoneyIcon from "./icons/MoneyIcon";
import WoodIcon from "./icons/WoodIcon";

interface ResourcesProps {
    resources: ResourcesModel,
    hideZero?: boolean
}

const Resources: React.FC<ResourcesProps> = (props) => {
    const r = props.resources;
    const hideZero = props.hideZero ?? false;

    console.log(r);
    return (
        <Grid container spacing={1} direction="row">

            {hideZero && r.money === 0 ? null : (
                <Grid item>
                    <MoneyIcon />
                    {r.money}
                </Grid>
            )}
            {hideZero && r.metal === 0 ? null : (
                <Grid item>
                    <MetalIcon/>
                    {r.metal}
                </Grid>
            )}
            {hideZero && r.wood === 0 ? null : (
                <Grid item>
                    <WoodIcon />
                    {r.wood}
                </Grid>
            )}
            {hideZero && r.cloth === 0 ? null : (
                <Grid item>
                    <ClothIcon/>
                    {r.cloth}
                </Grid>
            )}
            {hideZero && r.rum === 0 ? null : (
                <Grid item>
                    <RumIcon/>
                    {r.rum}
                </Grid>
            )}
        </Grid>
    );
}

export default Resources;