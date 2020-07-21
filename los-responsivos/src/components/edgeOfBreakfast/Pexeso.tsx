import React, {useCallback, useEffect, useState} from "react";
import {Box, Button, Container, Typography} from "@material-ui/core";
import {useSnackbar} from "notistack";
import {makeStyles} from "@material-ui/core/styles";
import clsx from "clsx";

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
    [1, 2, 3, 4],
    [5, 6, 7, 8],
    [9, 10, 11, 12],
    [13, 14, 15, 16],
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

    const handleClick = (cellClicked: PexesoTile) => {
        if (!tilesOpened.includes(cellClicked)) {
            setTilesOpened([...tilesOpened, cellClicked]);
        }
    };

    const resetGame = useCallback(() => {
        setTilesCompleted([]);
        enqueueSnackbar("Chyba! Hra se restartovala.", {
            variant: "info",
            anchorOrigin: {
                vertical: "top",
                horizontal: "center"
            }
        });
    }, [enqueueSnackbar]);

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
                    resetGame();
                    setTilesOpened([]);
                }, 1000);
            }
        }
    }, [tilesOpened, resetGame, markCompleted]);

    const gameWon = <Typography variant="h1" component="span" style={{color: "green"}}>Vyhráli jste!</Typography>;

    if (tilesCompleted.length === 8) {
        return (
            <Box padding={8}>
                <Container>
                    {gameWon}
                </Container>
            </Box>
        );
    } else return (
        <Box padding={3}>
            <Container>
                <table style={{width: "100%", tableLayout: "fixed"}}>
                    <tbody>
                    {data.map(row => (
                        <tr>
                            {row.map(cell => (
                                <td
                                    className={clsx({[classes.transparent]: tilesCompleted.includes(cell.tupleId)})}
                                    style={{height: tileHeight}}
                                >
                                    <Tile
                                        // disabled={tilesCompleted.includes(cell.tupleId)}
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