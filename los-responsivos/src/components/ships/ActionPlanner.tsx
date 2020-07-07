import React from "react";
import {Grid} from "@material-ui/core";
import {ShipAction} from "../../models/ShipDetail";
import PlannableAction from "./actions/PlannableAction";
import ApiService from "../../ApiService";
import useError from "../../useError";

interface ActionPlannerProps {
    shipId: string;
    availableActions: Array<ShipAction>;
    /** wil be invoked by the component after the api call to plan the action suceeeded */
    onActionPlannedOk?: () => void;
}

const allPossibleActions: Array<ShipAction> = Object.keys(ShipAction) as Array<ShipAction>;

const ActionPlanner: React.FC<ActionPlannerProps> = ({shipId, availableActions, ...props}) => {
    const {showDefaultError} = useError();

    const planMe = (action: ShipAction): void => {
        ApiService.planAction(shipId, action)
            .then(props.onActionPlannedOk)
            .catch(showDefaultError)
    };

    return (
        <Grid container direction="row">
            {allPossibleActions.map(action => (
                <Grid item xs={4} key={action}>
                    <PlannableAction
                        onClick={() => planMe(action)}
                        text={action}
                        available={availableActions.includes(action)}
                        key={action}
                    />
                </Grid>
            ))}
        </Grid>
    );
}
export default ActionPlanner;