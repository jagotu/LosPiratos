import React from "react";
import ResourcesModel from "../models/Resources";
import { Grid } from "@material-ui/core";
import AttachMoneyIcon from '@material-ui/icons/AttachMoney';
import WoodIcon from "./icons/WoodIcon";
import MetalIcon from "./icons/MetalIcon";
import ClothIcon from "./icons/ClothIcon";
import RumIcon from "./icons/RumIcon";
import NatureIcon from '@material-ui/icons/Nature';

interface ResourcesProps {
    resources: ResourcesModel
}

const Resources: React.FC<ResourcesProps> = (props) => {
    const r = props.resources;
    return (
        <Grid container spacing={1} direction="row">
            <Grid item>
                <AttachMoneyIcon />
                {r.money}
            </Grid>
            <Grid item>
                <MetalIcon />
                {r.metal}
            </Grid>
            <Grid item>
                <NatureIcon />
                {r.wood}
            </Grid>
            <Grid item>
                <ClothIcon />
                {r.cloth}
            </Grid>
            <Grid item>
                <RumIcon />
                {r.rum}
            </Grid>
        </Grid>
    );
}

export default Resources;