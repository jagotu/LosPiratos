import Grid from "@material-ui/core/Grid"
import React, {useState} from "react";
import ApiService from "../ApiService";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import {useHistory} from "react-router-dom";
import {routes} from "../App";
import {useUser} from "../userContext";
import {Typography} from "@material-ui/core";
import useError from "../useError";

const LoginForm: React.FC = () => {
    const [username, setUsername] = useState<string>("1");
    const [password, setPassword] = useState<string>("a");
    const history = useHistory();
    const {user, setUser, setTeamId, logout} = useUser();
    const {showDefaultError, showError} = useError();

    const handleFormSubmit = (e: any) => {
        e.preventDefault();
        ApiService.login(username, password)
            .then(({teamId}) => {
                setUser(teamId);
                setTeamId(teamId);
                history.push(routes.overview);
            })
            .catch(e => {
                if (e.response?.status === 401) {
                    showError(e.response.data);
                } else {
                    throw e;
                }
            })
            .catch(showDefaultError);
    };

    const userElement = (
        <Grid item>
            <Typography>Aktuálně přihlášen: {user}
                <Button
                    onClick={logout}
                    variant="outlined"
                    style={{float: "right"}}
                >
                    Odhlásit
                </Button>
            </Typography>
        </Grid>
    );

    return (
        <form onSubmit={handleFormSubmit}>
            <Grid container direction="column" spacing={3}>
                {user !== null ? userElement : null}
                <Grid item>
                    <TextField
                        fullWidth
                        variant="outlined"
                        label="jméno"
                        value={username}
                        onChange={e => setUsername(e.currentTarget.value)}
                    />
                </Grid>
                <Grid item>
                    <TextField
                        fullWidth
                        variant="outlined"
                        label="heslo"
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.currentTarget.value)}
                    />
                </Grid>
                <Grid item>
                    <Button
                        variant="contained"
                        color="primary"
                        type="submit"
                    >
                        Přihlásit se
                    </Button>
                </Grid>
            </Grid>
        </form>
    )
}

export default LoginForm;