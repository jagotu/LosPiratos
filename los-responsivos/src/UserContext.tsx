import React, {useContext, useState} from 'react';

const userKey = "userLogged";
const teamIdKey = "teamId";

export interface UserInfo {
    user: string | null;
    setUser: (name: string) => void;
    teamId: string;
    setTeamId: (name: string) => void;
    logout: () => void;
}

const UserContext = React.createContext<UserInfo>({
    user: null,
    setUser: (name) => ({}),
    teamId: "",
    setTeamId: (teamId) => ({}),
    logout: () => ({})
});

export const UserProvider: React.FC = ({children}) => {

    const [user, setUser] = useState<string | null>(localStorage.getItem(userKey));
    const [teamId, setTeamId] = useState<string>(localStorage.getItem(userKey) ?? "unknownTeam");
    const setUserAndPersist = (user: string) => {
        localStorage.setItem(userKey, user);
        setUser(user);
    }
    const setTeamIdAndPersist = (teamId: string) => {
        localStorage.setItem(teamIdKey, teamId);
        setTeamId(teamId);
    }
    const logout = () => {
        localStorage.removeItem(userKey);
        localStorage.removeItem(teamIdKey);
        setUser(null);
        setTeamId("");
    }

    return (
        <UserContext.Provider value={{user, setUser: setUserAndPersist, logout, teamId, setTeamId: setTeamIdAndPersist}}>
            {children}
        </UserContext.Provider>
    );
};
export const useUser = (): UserInfo => useContext(UserContext);
export const teamId: () => string = () => localStorage.getItem(teamIdKey) ?? "unknownTeam";