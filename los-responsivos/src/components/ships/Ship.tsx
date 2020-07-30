import React from "react";
import ShipModel from "../../models/Ship";
import {Box, CircularProgress, Grid, Icon, makeStyles} from "@material-ui/core";
import Resources from "../Resources";
import ResourcesModel from "../../models/Resources";
import translations from "../../translations";
import Position from "../Position";
import clsx from "clsx";
import {useHistory} from "react-router-dom";
import {routes} from "../../App";
import {useGameData} from "../../gameDataContext";
import getContrastColor from "../../util/contrast";
import HPIndicator from "./HPIndicator";
import ShipEnhancementsIndicator from "./ShipEnhancementsIndicator";
import "font-awesome/css/font-awesome.css";


interface ShipProps {
    data: ShipModel,
    clickable: boolean
}

const useStyles = makeStyles(() => ({
    shipButton: {
        textTransform: "initial",
        width: "100%",
        justifyContent: "left",
        lineHeight: 1,
        padding: 0
    },
    shipView: {
        width: 64,
        height: 64,
        padding: 8
    },
    border: {
        borderStyle: "solid",
        borderRadius: 12,
        borderWidth: 4
    },
    header: {
        fontWeight: "bold",
        lineHeight: "30px",
        paddingLeft: 8,
        paddingRight: 0
    },
    content: {
        padding: "8px 8px"
    },
    fixIconAlign: {
        lineHeight: "17px",
        verticalAlign: "top"
    }
}));

const Ship: React.FC<ShipProps> = (props) => {
    const s: ShipModel = props.data;
    const classes = useStyles();
    const history = useHistory();
    const {data} = useGameData();



    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;

    const shipTeam = data.enrichedGame.game.teams.filter(t => t.id === s.teamId)[0];
    const shipExtendedDetails = data.enrichedGame.extendedShipDetails.filter(t => t.shipId === s.id)[0];



    const handleClick = (): void => {
        history.push(`${routes.shipDetail}/${s.id}`);
    };

    const content = (
            <div className={classes.border} style={{borderColor: shipTeam.color}}>
                <div className={classes.header}
                     style={{backgroundColor: shipTeam.color, color: getContrastColor(shipTeam.color)}}>
                    {s.name}
                </div>
                <Grid container direction="column" className={classes.content} spacing={1}>
                    <Grid item>
                        <HPIndicator HP={s.HP} maxHP={shipExtendedDetails.maxHP} />
                    </Grid>

                    <Grid item>
                        <Grid container direction="row">
                            <Box flexGrow={1}>{translations[s.type]}</Box>
                            <Box>
                                <ShipEnhancementsIndicator ship={s} />
                            </Box>
                        </Grid>
                    </Grid>

                    <Grid item>
                        <Grid container direction="row" spacing={2}>
                            <Grid item><span className="icon">E</span> {shipExtendedDetails.cannonsCount}</Grid>
                            <Grid item><span className="icon">F</span> {shipExtendedDetails.cargoCapacity}</Grid>
                            <Grid item><Icon className="fa fa-flash" style={{fontSize: "18px", lineHeight: "17px", verticalAlign: "bottom"}} />{shipExtendedDetails.speed}</Grid>
                            <Box flexGrow={1}/>
                            <Grid item>
                                <span className={classes.fixIconAlign} style={{marginRight: 2}}>
                                    <Position position={s.position}/>
                                </span>
                                <Icon className={clsx("fa", "fa-arrow-right", `deg-${s.orientationDeg}`)} style={{fontSize: "18px"}} />
                            </Grid>
                        </Grid>
                    </Grid>
                    <Grid item>
                        <Resources resources={ResourcesModel.fromShip(s)}/>
                    </Grid>
                </Grid>
            </div>
    )

    if (props.clickable) {
        return (
            <div className={classes.shipButton} onClick={handleClick}>
                {content}
            </div>
        );
    } else {
        return (
            <div>
                {content}
            </div>
        );
    }
}

export default Ship;