import React, {useCallback, useEffect, useState} from "react";
import {Box, Button, Container, Typography} from "@material-ui/core";
import {useSnackbar} from "notistack";
import {makeStyles} from "@material-ui/core/styles";
import clsx from "clsx";
// @ts-ignore
import Websocket from 'react-websocket';
import axios from "axios";

interface PexesoProps {
}

type PexesoTile = {
    tupleId: string;
    imgSrc: string;
};

const createTile = (index: number): PexesoTile => ({
    tupleId: "" + Math.round(index / 2),
    imgSrc: process.env.PUBLIC_URL + "pexeso/" + Math.round(index / 2) + ".png",
});


const data: Array<Array<PexesoTile>> = [
    // test:
    // [1, 2, 3, 4],
    // [5, 6, 7, 8],
    // [9, 10, 11, 12],
    // [13, 14, 15, 16],

    // production:
    [1, 16, 10, 7],
    [13, 4, 5, 12],
    [15, 6, 2, 8],
    [3, 11, 14, 9],
].map(row => row.map(createTile));

interface TileProps {
    cell: PexesoTile;
    imageVisible: boolean;
    onClick: (cellClicked: PexesoTile) => void;
}

const tileHeight = 200;
const doNothing = () => {
};

const useStyles = makeStyles(() => ({
    transparent: {
        opacity: 0,
        transition: "opacity .5s linear 1s",
    },
    imageTransparent: {
        opacity: 0,
    },
    image: {
        transition: "opacity .5s linear",
    }
}));

const Tile: React.FC<TileProps> = ({cell, imageVisible, onClick}) => {
    const classes = useStyles();

    return (
        <Box p={1}>
            <Button
                style={{height: tileHeight}}
                fullWidth
                variant="contained"
                onClick={imageVisible ? doNothing : () => onClick(cell)}
            >
                <img
                    alt={cell.tupleId}
                    src={cell.imgSrc}
                    className={clsx({[classes.image]: imageVisible}, {[classes.imageTransparent]: !imageVisible})}
                />
                <div style={{position: "absolute", top: "50%", left: "50%"}}>
                    {imageVisible ? null : "?"}
                </div>
            </Button>
        </Box>
    )
}


const Pexeso: React.FC<PexesoProps> = (props) => {
    const [tilesOpened, setTilesOpened] = useState<Array<PexesoTile>>([]);
    const [tilesCompleted, setTilesCompleted] = useState<Array<string>>([]);
    const {enqueueSnackbar} = useSnackbar();
    const classes = useStyles();
    const [gamePaused, setGamePaused] = useState<boolean>(false);

    const handleClick = (cellClicked: PexesoTile) => {
        if (gamePaused) {
            return;
        }
        if (!tilesOpened.includes(cellClicked)) {
            setTilesOpened([...tilesOpened, cellClicked]);
        }
    };

    const doFailGame = useCallback(() => {
        setTilesCompleted([]);
        setGamePaused(true);
        enqueueSnackbar("Špatně!", {
            variant: "info",
            anchorOrigin: {
                vertical: "top",
                horizontal: "center"
            }
        });
    }, [enqueueSnackbar]);

    const doVictory = () => {
        setTilesCompleted([1, 2, 3, 4, 5, 6, 7, 8].map(s => String(s)));
    }
    const doRestart = () => {
        setTilesCompleted([]);
        setGamePaused(false);
        console.log("restart")
    }

    const handleWebsocketMessage = (message: any) => {
        const parsed =  JSON.parse(message);
        console.log(parsed);
        console.log(parsed.type);
        if (parsed.type == "failure") {
            doFailGame();
        }
        if (parsed.type == "victory") {
            doVictory();
        }
        if (parsed.type == "restart") {
            doRestart();
        }
    }

    const doFailGameAndNotify = useCallback(() => {
        axios.post("/api/stanoviste/pexeso/fail")
            .catch(console.error);
        doFailGame();
    }, [doFailGame]);

    const onGameWon = useCallback(() => {
        axios.post("/api/stanoviste/pexeso/finish")
            .catch(console.error);
    }, []);

    const markCompleted = useCallback((tupleId: string): void => {
        setTilesCompleted(prev => [...prev, tupleId]);
    }, []);

    useEffect(() => {
        if (tilesOpened.length === 2) {
            if (tilesOpened[0].tupleId === tilesOpened[1].tupleId) {
                markCompleted(tilesOpened[0].tupleId);
                setTilesOpened([]);
            } else {
                setTimeout(() => {
                    doFailGameAndNotify();
                    setTilesOpened([]);
                }, 1000);
            }
        }
    }, [tilesOpened, doFailGameAndNotify, markCompleted]);

    useEffect(() => {
        if (tilesCompleted.length === 8) {
            onGameWon();
        }
    }, [tilesCompleted, onGameWon]);

    const gameWonMessage = <Typography variant="h1" component="span" style={{color: "green"}}>Vyhráli jste.</Typography>;

    const websocket = <Websocket
        url={"wss://edge.ltmf2020.cz/ws/status"}
        onMessage={handleWebsocketMessage}/>;

    if (tilesCompleted.length === 8) {
        return (
            <Box padding={8}>
                {websocket}
                <Container>
                    {gameWonMessage}
                </Container>
            </Box>
        );
    } else return (
        <Box padding={3}>
            {websocket}
            <Container>
                <table style={{width: "100%"}}>
                    <tbody>
                    {data.map((row, i) => (
                        <tr key={i}>
                            {row.map((cell, j) => (
                                <td
                                    key={j}
                                    className={clsx({[classes.transparent]: tilesCompleted.includes(cell.tupleId)})}
                                    style={{height: tileHeight}}
                                >
                                    <Tile
                                        cell={cell}
                                        imageVisible={tilesOpened.includes(cell) || tilesCompleted.includes(cell.tupleId)}
                                        onClick={handleClick}
                                    />
                                </td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </Container>
        </Box>
    );
}

export default Pexeso;