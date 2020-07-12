import React, {useState} from "react";
import {Tab, Tabs} from "@material-ui/core";
import PlannableAction from "./PlannableAction";
import ApiService from "../../../ApiService";
import useError from "../../../useError";
import './ActionPlanner.css';
import actionLayout from "./actionPlannerLayout";
import {needsParameters, ShipAction, ShipActionKind, ShipActionParam} from "../../../models/ShipActions";
import {isTransaction} from "../../../models/Transactions";
import ActionDetailDialog, {OpenForAction} from "./ActionDetailDialog";

interface ActionPlannerProps {
    shipId: string;
    plannableActions: Set<ShipAction>;
    visibleActions: Set<ShipAction>;
    /** wil be invoked by the component after the api call to plan the action suceeeded */
    onActionPlannedOk?: () => void;
}

const ActionPlanner: React.FC<ActionPlannerProps> = ({shipId, plannableActions, visibleActions, ...props}) => {
    const {showDefaultError} = useError();
    const [tab, setTab] = useState<ShipActionKind>("maneuver");
    const [displayDetailDialog, setDisplayDetailDialog] = useState<OpenForAction>({open: false, action: undefined});

    const handleActionOnClick = (action: ShipAction): void => {
        if (needsParameters(action)) {
            setDisplayDetailDialog({open: true, action});
        } else {
            handlePlanParametrizedAction(action,{});
        }
    };

    const handlePlanParametrizedAction = (action: ShipAction, params: ShipActionParam) => {
        ApiService.planAction(shipId, action, params)
            .then(props.onActionPlannedOk)
            .catch(showDefaultError)
    }

    const anyTransactionPlannable = Array.from(plannableActions.values())
        .filter(isTransaction)
        .length > 0;
    const activeLayout: Array<Array<ShipAction | null>> = actionLayout[tab];
    return (
        <>
            <ActionDetailDialog
                openForAction={displayDetailDialog}
                onClose={() => setDisplayDetailDialog({open: false, action: undefined})}
                onParamSelected={handlePlanParametrizedAction}
            />
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
                                            onClick={() => handleActionOnClick(action)}
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
        </>
    );
}
export default ActionPlanner;