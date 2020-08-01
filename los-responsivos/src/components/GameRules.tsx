import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {routes} from "../App";
import {Button, Typography} from "@material-ui/core";
import axios from "axios";
import ReactMarkdown from "react-markdown";

const GameRules: React.FC = (props) => {
    const [markdown, setMarkdown] = useState("");

    useEffect(() => {
        axios.get(process.env.PUBLIC_URL + "/pravidla.md")
            .then(response => response.data)
            .then(setMarkdown);
    })

    return (
        <>
            <Button component={Link} to={routes.overview} color="primary" variant="contained">Zpět</Button>
            <div style={{textAlign: "center", paddingTop: 32}}>
                <Typography variant="h3">Los Piratos de la Casa</Typography>
                <Typography>
                    Interaktivní strategická počítačovo-mobilovo-desková hra<br/>
                    Karibik, 17. století
                </Typography>
            </div>
            <ReactMarkdown source={markdown} escapeHtml={false} />
        </>
    );
}

export default GameRules;