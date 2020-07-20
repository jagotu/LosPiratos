import {useSnackbar} from 'notistack';
import React, {useCallback, useMemo} from "react";

interface useErrorResult {
    showDefaultError: () => void;
    showError: (message: string) => void;
    showErrorFromEvent: (event: any) => void;
}

export default function useError(): useErrorResult {
    const {enqueueSnackbar} = useSnackbar();

    const defaultMessage = useMemo(() => (
        <div>
            Ay! Něco se nepovedlo. Zkuste znovu načíst stránku.<br/>
            Pokud to nepomůže, kontaktujte Gociho, případně Toníka.
        </div>
    ), []);

    const showError = useCallback((message: string) => {
        enqueueSnackbar(
            message,
            {variant: "error"}
        )
    }, [enqueueSnackbar]);

    const showErrorFromEvent = useCallback((event: any) => {
        const message = event.response?.status === 400 ? event.response.data : defaultMessage;
        enqueueSnackbar(
            message,
            {variant: "error"}
        )
    }, [enqueueSnackbar, defaultMessage]);

    const showDefaultError = useCallback(() => {
        enqueueSnackbar(
            defaultMessage,
            {variant: "error"}
        )
    }, [enqueueSnackbar, defaultMessage]);

    return {showDefaultError, showError, showErrorFromEvent};
}