// src/utils/tokenUtils.js
const TOKEN_KEY = 'token';

export const setToken = (tokenData) => {
    if (tokenData?.accessToken) {
        const token = `Bearer ${tokenData.accessToken}`;
        localStorage.setItem(TOKEN_KEY, token);
        return true;
    }
    return false;
};

export const getToken = () => localStorage.getItem(TOKEN_KEY);
export const removeToken = () => localStorage.removeItem(TOKEN_KEY);