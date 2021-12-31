import { http } from './http';
import { studyApiUrl } from '../AppUrl';
import { AxiosResponse } from 'axios';
import { Deck } from '../model/Deck';


export const getAllDecks = (): Promise<AxiosResponse<Deck[]>> => {
    return http.get<Deck[]>(`${studyApiUrl}/study/deck`);
};

export const getDeckById = (id: number): Promise<AxiosResponse<Deck>> => {
    return http.get<Deck>(`${studyApiUrl}/study/deck/${id}`);
};

export const deleteDeck = (deckId: number): Promise<AxiosResponse<void>> => {
    return http.delete(`${studyApiUrl}/study/deck/${deckId}`);
};


