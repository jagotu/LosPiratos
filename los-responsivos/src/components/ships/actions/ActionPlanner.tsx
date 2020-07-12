import React, {useState} from "react";
import {Tab, Tabs} from "@material-ui/core";
import PlannableAction from "./PlannableAction";
import ApiService from "../../../ApiService";
import useError from "../../../useError";
import {makeStyles} from "@material-ui/core/styles";
import './ActionPlanner.css';
import actionLayout from "./actionPlannerLayout";
import {ShipAction, ShipActionKind, ShipActionParam, Transactions} from "../../../models/ShipActions";

interface ActionPlannerProps {
    shipId: string;
    plannableActions: Set<ShipAction>;
    visibleActions: Set<ShipAction>;
    /** wil be invoked by the component after the api call to plan the action suceeeded */
    onActionPlannedOk?: () => void;
}

const useStyles = (makeStyles(() => {

}));

const ActionPlanner: React.FC<ActionPlannerProps> = ({shipId, plannableActions, visibleActions, ...props}) => {
    const {showDefaultError} = useError();
    const [tab, setTab] = useState<ShipActionKind>("maneuver");
    const classes = useStyles();

    const planMe = (action: ShipAction, params?: ShipActionParam): void => {
        ApiService.planAction(shipId, action, params)
            .then(props.onActionPlannedOk)
            .catch(showDefaultError)
    };

    const anyTransactionPlannable = Array.from(plannableActions.values())
        .filter(a => (Object.values(Transactions) as Array<string>).includes(a))
        .length > 0;
    const activeLayout: Array<Array<ShipAction | null>> = actionLayout[tab];
    return (
        <div>
            <Tabs value={tab} onChange={(e, newTab) => setTab(newTab)}>
                <Tab label="Manévry" value="maneuver"/>
                <Tab label="Útoky" value="attack"/>
                <Tab label="Transakce" value="transaction" disabled={!anyTransactionPlannable}/>
            </ Tabs>
            <table className="actionPlannerTable">
                <tbody>
                {activeLayout.map((row: Array<ShipAction | null>, i: number) => (
                    <tr key={i}>
                        {row.map((action: ShipAction | null, j: number) => (
                            <td key={j}>
                                {
                                    action && visibleActions.has(action) ?
                                        <PlannableAction
                                            onClick={() => planMe(action)}
                                            action={action}
                                            available={plannableActions.has(action)}
                                        />
                                        : null
                                }

                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
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