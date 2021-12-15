import axios from "axios";
import {studyApiUrl} from "../../AppUrl";

export const toggleSummaryAction = () => {
    return {
        type: 'TOGGLE_SUMMARY'
    }
}

export const getDueCardsAction = (deckId) => {
    return {
        type: 'GET_DUE_CARDS',
        payload: axios.get(studyApiUrl + '/api/study/card/due/?deckId=' + deckId)
    }
}

export const reviewCardAction = (cardId, easeOfAnswer) => {
    return {
        type: 'REVIEW_CARD',
        payload: axios.post(studyApiUrl + '/api/study/card/review', null, {
            params: {
                cardId,
                easeOfAnswer
            }
        })
    }
}