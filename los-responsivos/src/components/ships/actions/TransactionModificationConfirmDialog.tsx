import React from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@material-ui/core";
import {ShipAction} from "../../../models/ShipActions";
import {actionTranslations} from "./actionDetails";
import _ from "lodash";

export type OpenForAction = { open: false, action: undefined } | { open: true, action: ShipAction }

interface TransactionModificationConfirmDialogProps {
    openForAction: OpenForAction
    onClose: () => void;
    onConfirmed: (action: ShipAction) => void;
}

const TransactionModificationConfirmDialog: React.FC<TransactionModificationConfirmDialogProps> = ({openForAction, onClose, onConfirmed}) => {

    if (!openForAction.open) return null;
    const action = openForAction.action;

    const handleSubmit = (): void => {
        onConfirmed(action);
        onClose();
    };

    return (
        <Dialog
            open={openForAction.open}
            onClose={onClose}
        >
            <DialogTitle>
                {_.capitalize(actionTranslations.get(action))}
            </DialogTitle>
            <DialogContent>
                Tato transakce bude ihned provedena. Už ji nepůjde vzít zpět a ostatní týmy budou moci vidět její výsledek. Pokračovat?
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" onClick={onClose}> Zrušit </Button>
                <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    onClick={handleSubmit}
                >
                    Ihned provést
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default TransactionModificationConfirmDialog;