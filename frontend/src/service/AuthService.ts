import { AxiosResponse } from 'axios';
import { loginUrl, readApiUrl } from '../AppUrl';
import { User } from '../model/User';
import { http } from './http';
import { getItem, removeItem } from './LocalStorageService';


export const loginUser = (user: User): Promise<AxiosResponse<User>> => {
    removeItem('token');
    return http.post(`${loginUrl}/login`, user);
};

export const registerUser = (user: User): Promise<AxiosResponse<string[]>> => {
    removeItem('token');
    return http.post(`${readApiUrl}/users/register`, user);
};

export const isAuthenticated = () => {
    return !!getItem('token');
};

