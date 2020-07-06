import React from "react";
import {Grid} from "@material-ui/core";
import {ShipAction} from "../../models/ShipDetail";
import PlannableAction from "./actions/PlannableAction";
import ApiService from "../../ApiService";

interface ActionPlannerProps {
    shipId: string;
    availableActions: Array<ShipAction>;
}

const allPossibleActions: Array<ShipAction> = Object.keys(ShipAction) as Array<ShipAction>;

const ActionPlanner: React.FC<ActionPlannerProps> = ({shipId, availableActions}) => {

    const planMe = (action: ShipAction): void => {
        ApiService.planAction(shipId, action)
            .then( /* TODO somehow refresh data */)
            .catch( /* TODO use notistack */ )
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