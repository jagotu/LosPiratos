import React from "react";
import Resources from "../models/Resources";
import {Grid} from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import MetalIcon from "./icons/MetalIcon";
import ClothIcon from "./icons/ClothIcon";
import RumIcon from "./icons/RumIcon";

interface ResourceEditProps {
    value: Resources,
    onValueChange: (newValue: Resources) => void;
    moneyOnly?: boolean;
    noMoney?: boolean;
}

const resourceIcons = {
    money: <>$ </>,
    metal: <MetalIcon/>,
    wood: <>W </>,
    cloth: <ClothIcon/>,
    rum: <RumIcon/>,
}

type Resource = keyof Resources;
const ResourceEdit: React.FC<ResourceEditProps> = ({value, onValueChange, moneyOnly, noMoney}) => {

    const setValue = (key: Resource) =>
        (e: any) =>
            onValueChange({...value, [key]: parseInt(e.currentTarget.value.trim())});

    let availableResources: Array<Resource> = ["money", "metal", "wood", "cloth", "rum"];
    if (moneyOnly) {
        availableResources = ["money"];
    } else if (noMoney) {
        availableResources = availableResources.filter(r => r !== "money");
    }

    return (
        <>
            <Grid container direction="column" spacing={1}>
                {availableResources.map(r => (
                    <Grid item key={r}>
                        <TextField
                            fullWidth
                            type="number"
                            variant="outlined"
                            InputProps={{
                                startAdornment: <span style={{paddingRight: 8}}>{resourceIcons[r]}</span>
                            }}
                            value={Number.isNaN(value[r]) ? "" : value[r]}
                            onChange={setValue(r)}
                        />
                    </Grid>
                ))}
            </Grid>
        </>
    );
}

ResourceEdit.defaultProps = {
    moneyOnly: false,
    noMoney: false
}

export default ResourceEdit;