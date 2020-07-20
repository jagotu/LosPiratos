import React, {useEffect, useMemo} from "react";
import {Link, useHistory} from "react-router-dom";
import {routes} from "../App";
import {Button, CircularProgress, Grid, Typography} from "@material-ui/core";
import HexPosition, {positionsEqual} from "../models/HexPosition";
import Position from "./Position";
import TileProximityView from "./TileProximityView";
import {useGameData} from "../gameDataContext";
import Tile from "../models/Tile";
import translations from "../translations";
import Resources from "./Resources";
import Ship from "./ships/Ship";
import {teamId} from "../userContext";
import {makeStyles} from "@material-ui/core/styles";
import clsx from "clsx";

interface TileDetailProps {
    /**
     * String as matched in the URL, i.e. in format "Q.R"
     */
    coordinates: string;
    fullMap?: boolean;
}

const outOfRange = (location: HexPosition): boolean =>
    Math.abs(location.Q) > 6 || Math.abs(location.R) > 6 || Math.abs(location.Q + location.R) > 6;

const cropToRange = (location: HexPosition): HexPosition => {
    const z = location.Q + location.R;
    if (Math.abs(z) > 6) {
        // There is probably also some mathematically elegant solution to this, using the cubic (x,y,z) system.
        const Q = Math.sign(z) * Math.min(6, Math.abs(location.Q));
        const R = Math.sign(z) * 6 - Q;
        return {Q, R};
    } else return {
        Q: location.Q < -6 ? -6 : location.Q > 6 ? 6 : location.Q,
        R: location.R < -6 ? -6 : location.R > 6 ? 6 : location.R,
    };
}

const useStyles = makeStyles(() => ({
    transparent: {
        opacity: 0,
    },
    opacityTransition: {
        transition: "opacity 0s linear 1s",
    },
}));

const TileDetail: React.FC<TileDetailProps> = ({coordinates, fullMap}) => {
    let location: HexPosition = useMemo(() => ({
        Q: parseInt(coordinates.split(",")[0]),
        R: parseInt(coordinates.split(",")[1]),
    }), [coordinates]);
    const classes = useStyles();
    // const [fullMap, setFullMap] = useState(false);
    const history = useHistory();
    const {data} = useGameData();
    const allShips = useMemo(() =>
            data.loaded ? data.game.teams.flatMap(t => t.ships) : []
        , [data]);

    useEffect(() => {
        if (outOfRange(location)) {
            history.push(routes.factory.tileDetail(cropToRange(location)));
        }
    }, [location, history]);
    if (outOfRange(location))
        location = cropToRange(location);

    if (!data.loaded)
        return <div style={{textAlign: "center"}}><CircularProgress/></div>;

    if (outOfRange(location)) {
        history.replace(routes.factory.tileDetail(cropToRange(location)));
        return null;
    }

    const tile: Tile | undefined = data.game.map.tiles.find(t => positionsEqual(t.location, location));
    if (tile === undefined) {
        console.error("Tile not found in game data.", location, data.game.map);
        return <>Políčko {coordinates} nenalezeno</>;
    }
    const shipsOnThisTile = allShips.filter(s => positionsEqual(location, s.position));

    const detailsOfItemsOnTile = (
        <Grid container direction="column" spacing={1} className={clsx(
            {[classes.transparent]: fullMap},
            {[classes.opacityTransition]: !fullMap}
        )}>
            <Grid item>
                <Typography>Políčko <Position position={location}/>, {translations[tile.content]} {tile.portName}</Typography>
            </Grid>
            {tile.plantationsResource !== null ?
                <Grid item> <Resources resources={tile.plantationsResource}/> </Grid>
                : null
            }
            {shipsOnThisTile.map(ship => (
                // TODO klikani na lode bude fungovat, kdyz @jangocnik doda do ship teamId, #44
                <Grid item key={ship.id}><Ship data={ship} clickable={ship.teamId === teamId()}/></Grid>
            ))}
        </Grid>
    );


    const handleTileSelected = (newPosition: HexPosition) => {
        history.push(routes.factory.tileDetail(newPosition));
    }

    return (
        <>
            <Grid container direction="row" spacing={1}>
                <Grid item>
                    <Button component={Link} to={routes.overview} color="primary" variant="contained">Přehled</Button>
                </Grid>
                {fullMap ? null :
                    <Grid item>
                        <Button component={Link} to={routes.map} color="primary" variant="contained">Velká mapa</Button>
                    </Grid>
                }
            </Grid>
            <TileProximityView
                style={{paddingLeft: 0}}
                onTileSelected={handleTileSelected}
                center={location}
                fullMap={fullMap}
            />
            {detailsOfItemsOnTile}
        </>
    );
}

TileDetail.defaultProps = {
    fullMap: false
}

export default TileDetail;