import React from "react";
import {Button, makeStyles, Tooltip} from "@material-ui/core";
import _ from "lodash";
import {actionDetails, actionTranslations} from "./actionDetails";
import {ShipAction} from "../../../models/ShipActions";
import clsx from "clsx";

interface PlannableActionProps {
    action: ShipAction;
    available: boolean;
    onClick: () => void;
}

const size = 8 * 10;
const useStyles = makeStyles(() => ({
    button: {
        textTransform: "initial",
        width: "100%",
        height: size,
    },
    buttonWithIcon: {
        fontSize: "2.2em"
    },
    root: {
        margin: 8,
    }
}));

const PlannableAction: React.FC<PlannableActionProps> = ({action, ...props}) => {
    const classes = useStyles();

    const icon = actionDetails.get(action)?.icon;
    const translation = actionTranslations.get(action);
    return (
        <div className={classes.root}>
            <Tooltip title={translation ?? ""}>
                <div> {/* This div is needed for the tooltip, so that underlying element is never disabled */}
                    <Button
                        className={clsx(classes.button, {[classes.buttonWithIcon]: icon !== null})}
                        disabled={!props.available}
                        variant="contained"
                        onClick={props.onClick}
                    >
                        {icon ?? translation ?? _.startCase(action)}
                    </Button>
                </div>
            </Tooltip>
        </div>
    );
}

export default PlannableAction;