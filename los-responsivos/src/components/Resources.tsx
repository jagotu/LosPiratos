import React from "react";
import ResourcesModel from "../models/Resources";
import {Grid, Icon} from "@material-ui/core";
import MetalIcon from "./icons/MetalIcon";
import ClothIcon from "./icons/ClothIcon";
import RumIcon from "./icons/RumIcon";
import "font-awesome/css/font-awesome.css";

interface ResourcesProps {
    resources: ResourcesModel
}

const Resources: React.FC<ResourcesProps> = (props) => {
    const r = props.resources;
    return (
        <Grid container spacing={1} direction="row">
            <Grid item>
                <Icon className="fa fa-money" style={{fontSize: "18px", lineHeight: "17px", verticalAlign: "bottom"}} /> {r.money}
            </Grid>
            <Grid item>
                <MetalIcon /> {r.metal}
            </Grid>
            <Grid item>
                <Icon className="fa fa-tree" style={{fontSize: "17px", lineHeight: "17px", verticalAlign: "bottom"}} /> {r.wood}
            </Grid>
            <Grid item>
                <ClothIcon /> {r.cloth}
            </Grid>
            <Grid item>
                <RumIcon /> {r.rum}
            </Grid>
        </Grid>
    );
}

export default Resources;