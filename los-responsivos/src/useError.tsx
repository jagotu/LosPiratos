import {useSnackbar} from 'notistack';
import React, {useCallback, useMemo} from "react";

interface useErrorResult {
    showDefaultError: (e : any) => void;
}

export default function useError(): useErrorResult {
    const { enqueueSnackbar, closeSnackbar } = useSnackbar();

    const defaultMessage = useMemo( () => (
        <div>
            Ay! Něco se nepovedlo. Zkuste znovu načíst stránku.<br />
            Pokud to nepomůže, kontaktujte Gociho, případně Toníka.
        </div>
    ),[]);

    const showDefaultError = useCallback(e => {
        if(e.response && e.response.status == 400)
        {
            enqueueSnackbar(
                e.response.data,
                {
                    variant: "error",
                    anchorOrigin: {
                        vertical: 'top',
                        horizontal: 'center',
                    }
                }
            )
        } else {
        enqueueSnackbar(
                defaultMessage,
                {
                    variant: "error",
                    anchorOrigin: {
                        vertical: 'top',
                        horizontal: 'center',
                    }
                }
            )
        }
    },[enqueueSnackbar, defaultMessage]);

    return { showDefaultError };
}