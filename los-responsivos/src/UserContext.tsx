import React, {useContext, useState} from 'react';

const userKey = "userLogged";

export interface UserInfo {
    user: string | null;
    setUser: (name: string) => void;
    logout: () => void;
}

const UserContext = React.createContext<UserInfo>({
    user: null,
    setUser: (name) => ({}),
    logout: () => ({})
});

export const UserProvider: React.FC = ({children}) => {

    const [user, setUser] = useState<string | null>(localStorage.getItem(userKey));
    const setUserAndPersist = (user: string) => {
        localStorage.setItem(userKey, user);
        setUser(user);
    }
    const logout = () => {
        localStorage.removeItem(userKey);
        setUser(null);
    }

    return (
        <UserContext.Provider value={{user, setUser: setUserAndPersist, logout}}>
            {children}
        </UserContext.Provider>
    );
};
export const useUser = (): UserInfo => useContext(UserContext);