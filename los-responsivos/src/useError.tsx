import {useSnackbar} from 'notistack';
import React, {useCallback, useMemo} from "react";

interface useErrorResult {
    showDefaultError: () => void;
}

export default function useError(): useErrorResult {
    const { enqueueSnackbar, closeSnackbar } = useSnackbar();

    const defaultMessage = useMemo( () => (
        <div>
            Něco se nepovedlo. Zkuste znovu načíst stránku.<br />
            Pokud to nepomůže, kontaktujte Gociho, případně Toníka.
        </div>
    ),[]);

    const showDefaultError = useCallback(() => {
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
    },[enqueueSnackbar, defaultMessage]);

    return { showDefaultError };
}