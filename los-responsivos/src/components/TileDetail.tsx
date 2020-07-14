import React from "react";

interface TileDetailProps {
    /**
     * String as matched in the URL, i.e. in format "Q.R"
     */
    position: string
}

const TileDetail: React.FC<TileDetailProps> = ({position}) => {
    return (
        <>
            {position}
        </>
    );
}

export default TileDetail;