import React, {useContext, useEffect, useState} from 'react';
import Game from "./models/Game";
import useError from "./useError";
import ApiService from "./ApiService";

type MaybeData = { loaded: true, game: Game } | { loaded: false, game: undefined }

export interface GameContext {
    data: MaybeData;
    invalidateData: () => void;
}

const GameContext = React.createContext<GameContext>({
    data: {loaded: false, game: undefined},
    invalidateData: () => ({})
});

export const GameContextProvider: React.FC = ({children}) => {
    const [data, setData] = useState<MaybeData>({loaded: false, game: undefined});
    const [dataVersion, setDataVersion] = useState(0);
    const {showDefaultError} = useError();
    const invalidateData = () => setDataVersion(prev => prev+1);

    useEffect(() => {
        ApiService.getGameData()
            .then(game => setData({loaded: true, game}))
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
