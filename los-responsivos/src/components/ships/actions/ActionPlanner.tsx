import React, {useState} from "react";
import {Tab, Tabs} from "@material-ui/core";
import PlannableAction from "./PlannableAction";
import ApiService from "../../../ApiService";
import useError from "../../../useError";
import './ActionPlanner.css';
import actionLayout from "./actionPlannerLayout";
import {needsParameters, ShipAction, ShipActionKind, ShipActionParam} from "../../../models/ShipActions";
import {isModificationTransaction, isTransaction} from "../../../models/Transactions";
import ActionDetailDialog, {OpenForAction} from "./ActionDetailDialog";
import TransactionModificationConfirmDialog from "./TransactionModificationConfirmDialog";
import Ship from "../../../models/Ship";

interface ActionPlannerProps {
    ship: Ship;
    plannableActions: Set<ShipAction>;
    visibleActions: Set<ShipAction>;
    /** wil be invoked by the component after the api call to plan the action suceeeded */
    onActionPlannedOk?: () => void;
}

const ActionPlanner: React.FC<ActionPlannerProps> = ({ship, plannableActions, visibleActions, ...props}) => {
    const {showErrorFromEvent} = useError();
    const [tab, setTab] = useState<ShipActionKind>("maneuver");
    const [displayDetailDialog, setDisplayDetailDialog] = useState<OpenForAction>({open: false, action: undefined});
    const [displayConfirmModificationDialog, setDisplayConfirmModificationDialog] = useState<OpenForAction>({open: false, action: undefined});

    const handleActionOnClick = (action: ShipAction): void => {
        if (isModificationTransaction(action)) {
            setDisplayConfirmModificationDialog({open: true, action})
        } else if (needsParameters(action)) {
            setDisplayDetailDialog({open: true, action});
        } else {
            planAction(action, {});
        }
    };
    const handlePlanModificationTransaction = (action: ShipAction) => {
        if (needsParameters(action)) {
            setDisplayDetailDialog({open: true, action});
        } else {
            planAction(action, {})
        }
    };
    const planAction = (action: ShipAction, params: ShipActionParam) => {
        ApiService.planAction(ship.id, action, params, isModificationTransaction(action))
            .then(props.onActionPlannedOk)
            .catch(showErrorFromEvent)
    };

    const anyTransactionPlannable = Array.from(plannableActions.values())
        .filter(isTransaction)
        .length > 0;
    const activeLayout: Array<Array<ShipAction | null>> = actionLayout[tab];
    return (
        <>
            <ActionDetailDialog
                openForAction={displayDetailDialog}
                shipId={ship.id}
                sourceLocation={ship.position}
                onClose={() => setDisplayDetailDialog({open: false, action: undefined})}
                onParamSelected={planAction}
            />
            <TransactionModificationConfirmDialog
                openForAction={displayConfirmModificationDialog}
                onClose={() => setDisplayConfirmModificationDialog({open: false, action: undefined})}
                onConfirmed={handlePlanModificationTransaction}
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