import React, {useEffect, useMemo, useState} from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, InputLabel, MenuItem, Select, Typography} from "@material-ui/core";
import {Enhancement, needsParameters, ShipAction, ShipActionParam} from "../../../models/ShipActions";
import {isModificationTransaction, isTransaction, Transaction, transactionsParameters} from "../../../models/Transactions";
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
import ResourcesModel from "../../../models/Resources";
import Resources from "../../Resources";
import Ship from "../../../models/Ship";
import CostResponse from "../../../models/CostResponse";


export type OpenForAction = { open: false, action: undefined } | { open: true, action: ShipAction }

interface ActionDetailDialogProps {
    openForAction: OpenForAction;
    ship: Ship
    /**
     * Position of the ship that is planning this action. Used by for target selection.
     */
    sourceLocation: HexPosition;
    onClose: () => void;
    onParamSelected: (action: ShipAction, param: ShipActionParam) => void;
}

type MaybeCost = { loaded: "true", cost: CostResponse } | { loaded: "loading", cost: undefined } | { loaded: "error", cost: undefined }

const ActionDetailDialog: React.FC<ActionDetailDialogProps> = ({openForAction, onClose, onParamSelected, ship, sourceLocation}) => {
    const [actionParam, setActionParam] = useState<ShipActionParam>({});
    const [availableEnhancements, setAvailableEnhancements] = useState<Array<Enhancement> | null>(null);

    const [maybeCost, setMaybeCost] = useState<MaybeCost>({loaded: "loading", cost: undefined});

    const {showDefaultError} = useError();
    const imgKey = useMemo(uid, [openForAction]); // change every time the dialog opens / closes
    const shipId = ship.id;

    useEffect(() => {
        if (availableEnhancements === null
            && openForAction.open
            && transactionsParameters[openForAction.action as Transaction]?.needsEnhancement
        ) {
            if (openForAction.action === "RepairEnhancement") {
                let enhancements = [];
                for (let enh of Object.keys(ship.enhancements)) {
                    // @ts-ignore
                    if (ship.enhancements[enh] === "destroyed") {
                        enhancements.push(enh);
                    }
                }
                setAvailableEnhancements(enhancements);
            } else {
                ApiService.getPossibleEnhancementsForPurchase(shipId)
                    .then(setAvailableEnhancements)
                    .catch(showDefaultError);
            }
        }
    }, [availableEnhancements, openForAction, showDefaultError, shipId, ship.enhancements]);

    useEffect(() => {
        if (openForAction.open) {
            ApiService.getActionCost(shipId, openForAction.action, actionParam)
                .then(cost => setMaybeCost({loaded: "true", cost}))
                .catch(e => {
                    if (e.response?.status === 400) {
                        setMaybeCost({loaded: "error", cost: undefined})
                    } else {
                        throw e
                    }
                })
                .catch(showDefaultError)
        }
    }, [shipId, openForAction, actionParam, showDefaultError]);

    if (!openForAction.open) return null;
    const action = openForAction.action;

    if (!needsParameters(action)) {
        console.warn("ActionDetailDialog opened for action that does not need parameters.", action);
    }

    let needsMoney = false;
    let needsCommodity = false;
    let needsEnhancement = false;
    let needsTarget = action === "MortarShot";
    let hasCost = false;
    if (isTransaction(action)) {
        needsMoney = transactionsParameters[action as Transaction].needsMoney;
        needsCommodity = transactionsParameters[action as Transaction].needsCommodity;
        needsEnhancement = transactionsParameters[action as Transaction].needsEnhancement;
        hasCost = transactionsParameters[action as Transaction].hasCost;
    }

    if ((needsMoney || needsCommodity) && !actionParam.amount) {
        actionParam.amount = ResourcesModel.zero()
    }

    const amountPicker = (
        <ResourceEdit
            moneyOnly={!needsCommodity}
            noMoney={!needsMoney}
            value={actionParam.amount ?? ResourcesModel.zero()}
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

    let costStatus = (<>Neplatné parametry</>);

    if (maybeCost.loaded === "true") {
        costStatus = (<Resources resources={maybeCost.cost.cost} hideZero />);
    } else if (maybeCost.loaded === "loading") {
        costStatus = (<>Načítání...</>);
    }

    const costDisplay = (
        <Typography style={{paddingTop: 8}}>cena: {costStatus}</Typography>
    )

    const immediateWarning = isModificationTransaction(action) ? (
        <>
            Tato transakce bude ihned provedena. Už ji nepůjde vzít zpět a ostatní týmy budou moci vidět její výsledek.
        </>) : null;

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
                {immediateWarning}
                <form id={formId}>
                    {needsEnhancement ? enhancementPicker : null}
                    {needsTarget ? targetPicker : null}
                    {(needsCommodity || needsMoney) ? amountPicker : null}
                    {hasCost ? costDisplay : null}
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
                    disabled={!maybeCost?.cost?.isSatisfied ?? false}
                >
                    {isModificationTransaction(action) ? "Provést ihned" : "Naplánovat"}
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default ActionDetailDialog;