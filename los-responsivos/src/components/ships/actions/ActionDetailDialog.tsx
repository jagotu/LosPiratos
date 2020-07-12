import React, {useState} from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select} from "@material-ui/core";
import {Enhancement, Enhancements, needsParameters, ShipAction, ShipActionParam} from "../../../models/ShipActions";
import {isTransaction, Transaction, TransactionsParameters} from "../../../models/Transactions";
import {actionTranslations} from "./actionDetails";
import translations from "../../../translations";
import _ from "lodash";

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
    const handleEnhancementSelected = (event: any): void => {
        const enhancement = event.target.value;
        setActionParam(prev => ({...prev, enhancement}))
    };
    const enhancementPicker = (
        <FormControl>
            <InputLabel id="enhancementPickerInputLabel">Vylepšení</InputLabel>
            <Select
                value={actionParam.enhancement ?? "Ram"}
                labelId="enhancementPickerInputLabel"
                onChange={handleEnhancementSelected}
                style={{minWidth: "16ch"}}
                >
                {Object.values(Enhancements).map((enh: Enhancement) => (
                    <MenuItem key={enh} value={enh}>{
                        // @ts-ignore
                        translations[enh]}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
    const targetPicker = (
        "zvolit cil"
    );

    const handleSubmit = (): void => {
        onParamSelected(action, actionParam);
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
                {needsEnhancement ? enhancementPicker : null}
                {needsTarget ? targetPicker : null}
                {needsAmount ? amountPicker : null}
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" onClick={onClose}> Zrušit </Button>
                <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    onClick={handleSubmit}
                >
                    Naplánovat
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default ActionDetailDialog;