import React, {useEffect, useState} from "react";
import {Button, Checkbox, Dialog, DialogActions, DialogContent, DialogTitle, FormControlLabel, Typography} from "@material-ui/core";
import ApiService from "../ApiService";
import useError from "../useError";

interface ReadyCheckProps {
}

const ReadyCheck: React.FC<ReadyCheckProps> = (props) => {
    const [showDialog, setShowDialog] = useState(false);
    const [checked, setChecked] = useState(false);
    const [dataVersion, setDataVersion]= useState(0);
    const {showDefaultError} = useError();

    const handleCheckboxChange = () => {
        if (!checked) {
            setShowDialog(true);
        }
    }

    useEffect(() => {
        ApiService.getTeamReadyState()
            .then(setChecked);
    },[dataVersion]);

    const onDialogClose = () => setShowDialog(false);

    const handleSubmit = () => {
        ApiService.setTeamReadyState()
            .then(() => setDataVersion(prev => prev+1))
            .catch(showDefaultError);
        onDialogClose();
    }

    const dialog = (
        <Dialog open={showDialog} onClose={onDialogClose}>
            <DialogTitle>Opravdu?</DialogTitle>
            <DialogContent>
                <Typography>
                    Tato akce je pro dané kolo nevratná. Jakmile všechny týmy zaškrtnou, že jsou připraveny, dojde k vyhodnocení kola. Pokud nezaškrtnou všechny týmy, nestane se nic a k vyhodnocení kola dojde podle plánu.
                </Typography>
                <Typography>
                    I po zaškrtnuní lze interagovat se hrou a zadávat a rušit akce.
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button variant="outlined" onClick={onDialogClose}> Zrušit </Button>
                <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    onClick={handleSubmit}
                >
                    Závazně potvrdit
                </Button>
            </DialogActions>
        </Dialog>
    );

    return (
        <>
            {dialog}
            <FormControlLabel
                control={
                    <Checkbox
                        checked={checked}
                        onChange={handleCheckboxChange}
                        name="checkedB"
                        color="primary"
                    />
                }
                label="Jsme připraveni na vyhodnocení kola."
            />
        </>
    );
}

export default ReadyCheck;