import React from "react";
import './icons.css';

const alphabet = [
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
    "U",
    "V",
    "W",
    "X",
    "Y",
    "Z",

];

const AllIcons: React.FC = () => {
    return (
        <>
            <div>Basic LosPiratos Font:</div>
            {alphabet.map(char =>
                (<div><span className="icon">{char}</span> {char}</div>)
            )}
            <div>Trans:</div>
            {alphabet.map(char =>
                (<div><span className="icon icon-transaction" style={{fontSize: "2em"}}>{char}</span> {char}</div>)
            )}
            <div>Lastminute:</div>
            <div><span className="icon icon-lastminute">A</span> A</div>
        </>

    );
}

export default AllIcons;