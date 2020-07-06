import React, {useEffect, useState} from "react";
import { Grid, Button, Typography } from "@material-ui/core";

// @ts-ignore
const Moznost = ({children, onClick}) => {
    return (
        <Grid item xs={4}  >
            <Button style={{height: 48}} variant="contained" onClick={onClick} >
                {children}
            </Button>

        </Grid>
    )
}

const hlaska = [
    "Tohle nebude fungovat",
    "Muhehe, na tohle jsem příliš silný",
    "Lame pokus!",
    "Hohoho, dobrý pokus. Ale dělám protiútok!",
    "MUHEHEHE, tahle možnost se obrátila přímo proti tobě",
    "Ty se ani nesnažíš mě porazit...",
    "Bez šance... Lol",
    "Cha cha, mě jen tak neporazíš!",
    "Co jsi čekal? Programoval mě Toník, jsem neporazitelný boss!",
];

function EdgeOfBreakfast() {

    const [ha, setHa] = useState(0);
    const [t, setT] = useState();


    // @ts-ignore
    const doHa = (value) => {
        setHa(value);
        clearTimeout(t);
        const tt = setTimeout(() => {
            setHa(0);
            console.log("ha")
        }, 3000)
        setT(tt);
    }

    return (
        <>
            <Typography variant="h4">Super nebezpecna a pokrocila umela inteligence zautocial. Co budes delat?</Typography>
        <Grid container direction="row" spacing={2}>
            <Moznost onClick={() => doHa(1)}>Moznost 1</Moznost>
            <Moznost onClick={() => doHa(2)}>Moznost 2</Moznost>
            <Moznost onClick={() => doHa(3)}>Moznost 3</Moznost>
            <Moznost onClick={() => doHa(4)}>Moznost 4</Moznost>
            <Moznost onClick={() => doHa(5)}>Moznost 5</Moznost>
            <Moznost onClick={() => doHa(6)}>Moznost 6</Moznost>
            <Moznost onClick={() => doHa(7)}>Moznost 7</Moznost>
            <Moznost onClick={() => doHa(8)}>Moznost 8</Moznost>
            <Moznost onClick={() => doHa(9)}>Moznost 9</Moznost>
        </Grid>

            <Typography variant="h6" style={{color: "red", paddingTop: 3*8}}>
                {ha === 0 ? null : hlaska[ha-1]}
                {/*Uáááá, jsem poražen. Jsi na mě příliš chytrý Dejve! Vyhrál jsi. Ale byl to férový a těžký souboj...*/}
            </Typography>
            </>
    )
}

export default EdgeOfBreakfast;