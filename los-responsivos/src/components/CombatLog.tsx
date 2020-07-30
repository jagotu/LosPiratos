import React, {ReactElement, useEffect, useState} from "react";
import useError from "../useError";
import ApiService from "../ApiService";
import {Accordion, AccordionDetails, AccordionSummary, Button, CircularProgress, Grid, Table, TableBody, TableCell, TableRow} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import {Link} from "react-router-dom";
import {routes} from "../App";

type MaybeData = { loaded: true, log: string[] } | { loaded: false, log: undefined }

const useStyles = makeStyles({
    row: {
        "&:nth-of-type(odd)": {
            background: "WhiteSmoke"
        }
    }
});

interface Round {
    number: number;
    logs: Array<string>
}

const parseCombatLog = (combatLog: Array<string>): Array<Round> => {
    const result: Array<Round> = [{number: 0, logs: []}];
    let roundIdx = 0;
    for (let i = 0; i < combatLog.length; i++) {
        const item = combatLog[i];
        if (item.startsWith("konec")) {
            const roundNo = Number.parseInt(item.split(" ")[1]);
            roundIdx++
            result[roundIdx] = {number: roundNo, logs: []};
        } else {
            result[roundIdx].logs.push(item);
        }
    }
    return result.filter(round => round.logs.length > 0);
}

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

    const rounds: Array<Round> = parseCombatLog(data.log);

    const renderRound = (round: Round): ReactElement => (
        <Accordion>
            <AccordionSummary>
                {round.number}. kolo
            </AccordionSummary>
            <AccordionDetails>
                <Table size="small" style={{padding: 24}}>
                    <TableBody>
                        {round.logs.map((logitem, i) => (
                            <TableRow key={i} className={classes.row}>
                                <TableCell>
                                    {logitem}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </AccordionDetails>
        </Accordion>
    )

    return (
        <Grid container direction="column" spacing={2}>
            <Grid item>
                <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět na přehled</Button>
            </Grid>
            <Grid item>
                {rounds.map(renderRound)}
            </Grid>
        </Grid>
    )
}

export default CombatLog;