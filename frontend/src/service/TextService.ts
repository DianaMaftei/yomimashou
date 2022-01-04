import { AxiosResponse } from 'axios';
import { readApiUrl } from '../AppUrl';
import { Text } from '../model/Text';
import { http } from './http';


export const getAllTexts = (): Promise<AxiosResponse<Text[]>> => {
    return http.get<Text[]>(`${readApiUrl}/text`);
};

export const getTextById = (id: number): Promise<AxiosResponse<Text>> => {
    return http.get<Text>(`${readApiUrl}/text/${id}`);
};

export const getTextsStatuses = (): Promise<AxiosResponse<Text[]>> => {
    return http.get<Text[]>(`${readApiUrl}/users/textStatus`);
};

export const parseTextWords = (text: string): Promise<AxiosResponse<string[]>> => {
    return http.post<any>(`${readApiUrl}/text/parse/words`, {text: text});
};

export const parseTextNames = (text: string): Promise<AxiosResponse<string[]>> => {
    return http.post<any>(`${readApiUrl}/text/parse/names`, {text: text});
};

export const markTextAsRead = (id: number): void => {
    http.post<any>(`${readApiUrl}/users/textStatus?progressStatus=READ&textId=${id}`);
};

export const fetchData = (url: string, searchItem: string, page: number, limit: number): Promise<AxiosResponse<string[]>> => {
    return http.get(`${readApiUrl}/dictionary/${url}?searchItem=${searchItem}&page=${page}&size=${limit}`);
};

export const fetchWordList = (sentence: string): Promise<AxiosResponse<string[]>> => {
    return http.post(`${readApiUrl}/sentence/`, sentence);
};

export const createText = (data: FormData): Promise<AxiosResponse<Text>> => {
    return http.post<any>(`${readApiUrl}/text`, data);
};

export const updateText = (text: Text): Promise<AxiosResponse<Text>> => {
    return http.put<Text>(`/text/${text.id}`, text);
};

export const deleteText = (text: Text): Promise<AxiosResponse<Text>> => {
    return http.delete<Text>(`/text/${text.id}`);
};
