import React, {useContext, useEffect, useState} from 'react';
import useError from "./useError";
import ApiService from "./ApiService";
import EnrichedGame from "./models/EnrichedGame";

type MaybeData = { loaded: true, enrichedGame: EnrichedGame } | { loaded: false, enrichedGame: undefined }

export interface GameContext {
    data: MaybeData;
    invalidateData: () => void;
}

const GameContext = React.createContext<GameContext>({
    data: {loaded: false, enrichedGame: undefined},
    invalidateData: () => ({})
});

export const GameContextProvider: React.FC = ({children}) => {
    const [data, setData] = useState<MaybeData>({loaded: false, enrichedGame: undefined});
    const [dataVersion, setDataVersion] = useState(0);
    const {showDefaultError} = useError();
    const invalidateData = () => setDataVersion(prev => prev+1);

    useEffect(() => {
        ApiService.getGameData()
            .then(enrichedGame => setData({loaded: true, enrichedGame}))
            .catch(showDefaultError);
    }, [dataVersion, showDefaultError]);

    return (
        <GameContext.Provider value={{
            data,
            invalidateData
        }}>
            {children}
        </GameContext.Provider>
    );
};
export const useGameData = (): GameContext => useContext(GameContext);
