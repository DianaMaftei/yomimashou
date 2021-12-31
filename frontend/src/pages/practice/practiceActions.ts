import { getCardsDue, reviewCard } from '../../service/CardService';


export const toggleSummaryAction = () => {
    return {
        type: 'TOGGLE_SUMMARY'
    };
};

export const getDueCardsAction = (deckId: number) => {
    return {
        type: 'GET_DUE_CARDS',
        payload: getCardsDue(deckId)
    };
};

export const reviewCardAction = (cardId: number, easeOfAnswer: number) => {
    return {
        type: 'REVIEW_CARD',
        payload: reviewCard(cardId, easeOfAnswer)
    };
};
