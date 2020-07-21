import React, {useCallback, useEffect, useState} from "react";
import {Box, Button, Container} from "@material-ui/core";
import {useSnackbar} from "notistack";
import {makeStyles} from "@material-ui/core/styles";
import clsx from "clsx";

interface PexesoProps {
}

type PexesoTile = {
    tupleId: string;
    imgSrc: string;
};

const tile = (index: number): PexesoTile => ({
    tupleId: "" + Math.round(index / 2),
    imgSrc: process.env.PUBLIC_URL + "pexeso_" + index + ".png",
});


const data: Array<Array<PexesoTile>> = [
    [1, 2, 3, 4],
    [5, 6, 7, 8],
    [9, 10, 11, 12],
    [13, 14, 15, 16],
].map(row => row.map(tile));

interface TileProps {
    cell: PexesoTile;
    imageVisible: boolean;
    onClick: (cellClicked: PexesoTile) => void;
}

const tileHeight = 200;
const doNothing = () =>{};

const Tile: React.FC<TileProps> = ({cell, imageVisible, onClick}) => {
    return (
        <Button
            style={{height: tileHeight}}
            fullWidth
            variant="contained"
            color={imageVisible ? "primary" : "secondary"}
            onClick={imageVisible ? doNothing : () => onClick(cell)}
        >
            {imageVisible ? "Obrázek " + cell.tupleId : "?"}
        </Button>
    )
}

const useStyles = makeStyles(() => ({
    transparent: {
        opacity: 0,
        transition: "opacity .5s linear 1s",
    },
}));

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
            } else {
                resetGame();
            }
            setTilesOpened([]);
        }
    }, [tilesOpened, resetGame, markCompleted]);

    const gameWon = <h1 style={{color: "green"}}>Vyhráli jste!</h1>;

    if (tilesCompleted.length === 8) {
        return (
            <Box padding={3}>
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