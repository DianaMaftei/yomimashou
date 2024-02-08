import { getCardsInDeck } from '../../service/CardService';
import { getAllDecks, getDeckById } from '../../service/DeckService';


export const getDecksAction = () => {
    return {
        type: 'GET_DECKS',
        payload: getAllDecks()
    };
};

export const getDeckAction = (id: number) => {
    return {
        type: 'GET_DECK',
        payload: getDeckById(id)
    };
};

export const getCardsInDeckAction = (id: number) => {
    return {
        type: 'GET_CARDS_IN_DECK',
        payload: getCardsInDeck(id)
    };
};
