import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import {SnackbarProvider} from "notistack";
import Pexeso from "./components/edgeOfBreakfast/Pexeso";

ReactDOM.render(
    <React.StrictMode>
        <SnackbarProvider
            anchorOrigin={{
                horizontal: "left",
                vertical: "top"
            }}
        >
            <Pexeso />
        </SnackbarProvider>
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
