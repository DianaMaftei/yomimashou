import { Deck } from './Deck';


export type Card = {
    id: number;
    deck: Deck;
    kanji: string;
    kana: string;
    explanation: string;
    cardItemOrigin: string;
    active: boolean;
    repetitions: number;
    interval: number;
    nextPractice: Date;
};
