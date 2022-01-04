import { AxiosResponse } from 'axios';
import { studyApiUrl } from '../AppUrl';
import { Card } from '../model/Card';
import { http } from './http';


export const getCardsInDeck = (deckId: number): Promise<AxiosResponse<Card[]>> => {
    return http.get<Card[]>(`${studyApiUrl}/study/card?deckId=${deckId}`);
};

export const getCardsDue = (deckId: number): Promise<AxiosResponse<Card[]>> => {
    return http.get<Card[]>(`${studyApiUrl}/study/card/due/?deckId=${deckId}`);
};

export const reviewCard = (cardId: number, easeOfAnswer: number): Promise<AxiosResponse<Card>> => {
    return http.post<Card>(`${studyApiUrl}/study/card/review`, null, {
        params: {
            cardId,
            easeOfAnswer
        }
    });
};

export const deleteCard = (cardId: number): Promise<AxiosResponse<void>> => {
    return http.delete(`${studyApiUrl}/study/card/${cardId}`);
};

