import React from "react";
import {Grid} from "@material-ui/core";
import {ShipAction, ShipActionParam} from "../../models/ShipDetail";
import PlannableAction from "./actions/PlannableAction";
import ApiService from "../../ApiService";
import useError from "../../useError";

interface ActionPlannerProps {
    shipId: string;
    plannableActions: Set<ShipAction>;
    visibleActions: Set<ShipAction>;
    /** wil be invoked by the component after the api call to plan the action suceeeded */
    onActionPlannedOk?: () => void;
}

const ActionPlanner: React.FC<ActionPlannerProps> = ({shipId, plannableActions, ...props}) => {
    const {showDefaultError} = useError();

    const planMe = (action: ShipAction, params?: ShipActionParam): void => {
        ApiService.planAction(shipId, action, params)
            .then(props.onActionPlannedOk)
            .catch(showDefaultError)
    };

    return (
        <Grid container direction="row">
            {Array.from(props.visibleActions.values()).map(action => (
                <Grid item xs={4} key={action}>
                    <PlannableAction
                        onClick={() => planMe(action)}
                        text={action}
                        available={plannableActions.has(action)}
                        key={action}
                    />
                </Grid>
            ))}
        </Grid>
    );
}
export default ActionPlanner;