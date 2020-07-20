import React, {useEffect, useMemo, useState} from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select, Typography} from "@material-ui/core";
import {Enhancement, needsParameters, ShipAction, ShipActionParam} from "../../../models/ShipActions";
import {isTransaction, Transaction, transactionsParameters} from "../../../models/Transactions";
import {actionTranslations} from "./actionDetails";
import translations from "../../../translations";
import _ from "lodash";
import ApiService from "../../../ApiService";
import useError from "../../../useError";
import TileProximityView from "../../TileProximityView";
import HexPosition from "../../../models/HexPosition";
import Position from "../../Position";
import uid from "../../../util/uid";
import ResourceEdit from "../../ResourceEdit";
import Resources from "../../../models/Resources";


export type OpenForAction = { open: false, action: undefined } | { open: true, action: ShipAction }

interface ActionDetailDialogProps {
    openForAction: OpenForAction;
    shipId: string;
    /**
     * Position of the ship that is planning this action. Used by for target selection.
     */
    sourceLocation: HexPosition;
    onClose: () => void;
    onParamSelected: (action: ShipAction, param: ShipActionParam) => void;
}

const ActionDetailDialog: React.FC<ActionDetailDialogProps> = ({openForAction, onClose, onParamSelected, shipId, sourceLocation}) => {
    const [actionParam, setActionParam] = useState<ShipActionParam>({});
    const [availableEnhancements, setAvailableEnhancements] = useState<Array<Enhancement> | null>(null);
    const {showDefaultError} = useError();
    const imgKey = useMemo(uid, [openForAction]); // change every time the dialog opens / closes

    useEffect(() => {
        if (availableEnhancements === null
            && openForAction.open
            && transactionsParameters[openForAction.action as Transaction]?.needsEnhancement
        ) {
            ApiService.getPossibleEnhancements(shipId, openForAction.action)
                .then(setAvailableEnhancements)
                .catch(showDefaultError);
        }
    }, [availableEnhancements, openForAction, showDefaultError, shipId]);

    if (!openForAction.open) return null;
    const action = openForAction.action;

    if (!needsParameters(action)) {
        console.warn("ActionDetailDialog opened for action that does not need parameters.", action);
    }

    let needsAmount = false;
    let needsEnhancement = false;
    let needsTarget = action === "MortarShot";
    if (isTransaction(action)) {
        needsAmount = transactionsParameters[action as Transaction].needsAmount;
        needsEnhancement = transactionsParameters[action as Transaction].needsEnhancement;
    }

    if(needsAmount && !actionParam.amount)
    {
        actionParam.amount = Resources.zero()
    }

    const amountPicker = (
        <ResourceEdit
            value={actionParam.amount ?? Resources.zero()}
            onValueChange={amount => setActionParam(prev => ({...prev, amount}))}
        />
    );

    const handleEnhancementSelected = (event: any): void => {
        const enhancement = {"enhancement": event.target.value};
        setActionParam(prev => ({...prev, enhancement}))
    };
    const enhancementPicker = (
        <FormControl>
            <InputLabel id="enhancementPickerInputLabel">Vylepšení</InputLabel>
            <Select
                value={actionParam.enhancement?.enhancement ?? ""}
                labelId="enhancementPickerInputLabel"
                onChange={handleEnhancementSelected}
                style={{minWidth: "16ch"}}
            >
                {availableEnhancements?.map((enh: Enhancement) => (
                    <MenuItem key={enh} value={enh}>{
                        // @ts-ignore
                        translations[enh]}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );

    const handleTargetSelected = (target: HexPosition) => {
        setActionParam(prev => ({...prev, target}))
    };
    const targetPicker = (
        <>
            <Typography component="span">Vyber cíl:</Typography>
            <TileProximityView
                mortar
                center={sourceLocation}
                style={{padding: 0}}
                onTileSelected={handleTargetSelected}
                imgKey={imgKey}
            />
            <Typography component="span">Střed: <Position position={sourceLocation}/></Typography>
            <Typography style={{float: "right"}} component="span">
                Vybráno: <Position position={actionParam.target}/>
            </Typography>
        </>
    );

    const handleSubmit = (): void => {
        onParamSelected(action, actionParam);
        setActionParam({}) // reset values
        onClose();
    };

    const formId = "actionDetailDialog-" + action;
    return (
        <Dialog
            open={openForAction.open}
            onClose={onClose}
        >
            <DialogTitle>
                {_.capitalize(actionTranslations.get(action))}
            </DialogTitle>
            <DialogContent>
                <form id={formId}>
                    {needsEnhancement ? enhancementPicker : null}
                    {needsTarget ? targetPicker : null}
                    {needsAmount ? amountPicker : null}
                </form>
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" onClick={onClose}> Zrušit </Button>
                <Button
                    form={formId}
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