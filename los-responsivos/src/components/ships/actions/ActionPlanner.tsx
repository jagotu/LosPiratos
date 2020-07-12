import React, {useState} from "react";
import {Tab, Tabs} from "@material-ui/core";
import PlannableAction from "./PlannableAction";
import ApiService from "../../../ApiService";
import useError from "../../../useError";
import {makeStyles} from "@material-ui/core/styles";
import './ActionPlanner.css';
import actionLayout from "./actionPlannerLayout";
import {ShipAction, ShipActionKind, ShipActionParam} from "../../../models/ShipActions";

interface ActionPlannerProps {
    shipId: string;
    plannableActions: Set<ShipAction>;
    visibleActions: Set<ShipAction>;
    /** wil be invoked by the component after the api call to plan the action suceeeded */
    onActionPlannedOk?: () => void;
}

const useStyles = (makeStyles(() => {

}));

const ActionPlanner: React.FC<ActionPlannerProps> = ({shipId, plannableActions, ...props}) => {
    const {showDefaultError} = useError();
    const [tab, setTab] = useState<ShipActionKind>("maneuver");
    const classes = useStyles();

    const planMe = (action: ShipAction, params?: ShipActionParam): void => {
        ApiService.planAction(shipId, action, params)
            .then(props.onActionPlannedOk)
            .catch(showDefaultError)
    };

    const activeLayout: Array<Array<ShipAction | null>> = actionLayout[tab];
    return (
        <div>
            <Tabs value={tab} onChange={(e, newTab) => setTab(newTab)}>
                <Tab label="Manévry" value="maneuver"/>
                <Tab label="Útoky" value="attack"/>
                <Tab label="Transakce" value="transaction"/>
            </ Tabs>
            <table className="actionPlannerTable">
                {activeLayout.map((row: Array<ShipAction | null>, i: number) => (
                    <tr key={i}>
                        {row.map((action: ShipAction | null, j: number) => (
                            <td key={j}>
                                {
                                    action ?
                                        <PlannableAction
                                            onClick={() => planMe(action)}
                                            text={action}
                                            available={plannableActions.has(action)}
                                        />
                                        : null
                                }

                            </td>
                        ))}
                    </tr>
                ))}
            </table>


            {/*<TabContext value={tab}>*/}
            {/*    <TabPanel value="maneuvers">*/}
            {/*        <Grid container direction="row">*/}
            {/*            {Array.from(props.visibleActions.values()).map(action => (*/}
            {/*                <Grid item xs={4} key={action}>*/}
            {/*                    <PlannableAction*/}
            {/*                        onClick={() => planMe(action)}*/}
            {/*                        text={action}*/}
            {/*                        available={plannableActions.has(action)}*/}
            {/*                        key={action}*/}
            {/*                    />*/}
            {/*                </Grid>*/}
            {/*            ))}*/}
            {/*        </Grid>*/}
            {/*    </TabPanel>*/}
            {/*</TabContext>*/}
        </div>
    );
}
export default ActionPlanner;