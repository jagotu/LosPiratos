import React, {useState} from "react";
import {Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from "@material-ui/core";
import {needsParameters, ShipAction, ShipActionParam} from "../../../models/ShipActions";
import {isTransaction, Transaction, TransactionsParameters} from "../../../models/Transactions";
import {actionTranslations} from "./actionDetails";

export type OpenForAction = { open: false, action: undefined } | { open: true, action: ShipAction }

interface ActionDetailDialogProps {
    openForAction: OpenForAction
    onClose: () => void;
    onParamSelected: (action: ShipAction, param: ShipActionParam) => void;
}

const ActionDetailDialog: React.FC<ActionDetailDialogProps> = ({openForAction, onClose, onParamSelected}) => {
    const [actionParam, setActionParam] = useState<ShipActionParam>({});

    if (!openForAction.open) return null;
    const action = openForAction.action;

    if (needsParameters(action)) {
        console.warn("ActionDetailDialog opened for action that does not need parameters.", action);
    }

    let needsAmount = false;
    let needsEnhancement = false;
    let needsTarget = action === "MortarShot";
    if (isTransaction(action)) {
        needsAmount = TransactionsParameters[action as Transaction].needsAmount;
        needsEnhancement = TransactionsParameters[action as Transaction].needsEnhancement;
    }

    const amountPicker = (
        "Zvolit mnozstvi"
    );
    const enhancementPicker = (
        "Zvolit rozsireni"
    );
    const targetPicker = (
        "zvolit cil"
    );

    return (
        <Dialog
            open={openForAction.open}
            onClose={onClose}
        >
            <DialogTitle>
                {actionTranslations.get(action)}
            </DialogTitle>
            <DialogContent>
                <Typography>
                    Zvolte {needsTarget ? "cíl" : null}{needsAmount ? "množství" : null}{needsEnhancement ? "vylepšení" : null}:
                </Typography>
                <Box paddingTop={1}>
                    {needsEnhancement ? enhancementPicker : null}
                    {needsTarget ? targetPicker : null}
                    {needsAmount ? amountPicker : null}
                </Box>
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" onClick={onClose}> Zrušit </Button>
                <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    onClick={() => onParamSelected(action, actionParam)}
                >
                    Naplánovat
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default ActionDetailDialog;