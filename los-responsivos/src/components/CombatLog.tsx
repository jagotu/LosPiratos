import React, {useEffect, useState} from "react";
import useError from "../useError";
import ApiService from "../ApiService";
import {Button, CircularProgress, Grid, Table, TableBody, TableCell, TableRow, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import {Link} from "react-router-dom";
import {routes} from "../App";

type MaybeData = { loaded: true, log: string[] } | { loaded: false, log: undefined }

const useStyles = makeStyles({
    row: {
        "&:nth-of-type(even)" : {
            background: "WhiteSmoke"
        }
    }

});


const CombatLog: React.FC = () => {
    const classes = useStyles();
    const {showDefaultError} = useError();

    const [data, setData] = useState<MaybeData>({loaded: false, log: undefined});

    useEffect(() => {
        ApiService.getCombatLog()
            .then(log => log.reverse())
            .then(log => setData({loaded: true, log}))
            .catch(showDefaultError);
    }, [showDefaultError]);

    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;

    const log : string[] = data.log;


    return (
        <Grid container direction="column" spacing={2}>
            <Grid item>
                <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět na přehled</Button>
            </Grid>
            <Grid item>
                <Typography variant="caption">(Nejnovější nahoře)</Typography>
            </Grid>
            <Grid item>
                <Table size="small">
                    <TableBody>
                        {log.map((logitem,i) => (
                            <TableRow key={i} className={classes.row}>
                                <TableCell>
                                    {logitem}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Grid>
        </Grid>
    )
}

export default CombatLog;