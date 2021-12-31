import { studyApiUrl } from '../AppUrl';
import { AxiosResponse } from 'axios';
import { http } from './http';
import { Card } from '../model/Card';


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

