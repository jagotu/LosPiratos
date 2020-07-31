import React, {SyntheticEvent} from "react";
import {Breadcrumbs, Link} from "@material-ui/core";
import _ from "lodash";
import {actionTranslations} from "./actionDetails";
import {ShipAction} from "../../../models/ShipActions";
import NavigateNextIcon from "@material-ui/icons/NavigateNext";

interface PlannedActionsProps {
    actions: Array<ShipAction>;
    onActionClick?: (index: number) => void;
}

const PlannedActions: React.FC<PlannedActionsProps> = ({actions, ...props}) => {

    const getActionName = (action: ShipAction): string =>
        _.capitalize(actionTranslations.get(action) ?? _.startCase(action));

    const handlePlannedActionClick = (index: number, e?: SyntheticEvent) => {
        e?.preventDefault();
        props.onActionClick?.(index);
    }

    return (
        <Breadcrumbs separator={<NavigateNextIcon fontSize="small"/>}>
            {actions.map((action, i) => (
                <Link
                    color="inherit"
                    underline="hover"
                    href="#"
                    onClick={(e: SyntheticEvent) => handlePlannedActionClick(i, e)} key={i}
                >
                    {getActionName(action)}
                </Link>
            ))}
        </Breadcrumbs>
    );
}

export default PlannedActions;