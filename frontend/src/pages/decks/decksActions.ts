import axios from "axios";
import {studyApiUrl} from "../../AppUrl";

export const getDecksAction = () => {
    return {
        type: 'GET_DECKS',
        payload: axios.get(studyApiUrl + '/api/study/deck')
    }
}

export const getDeckAction = (id) => {
    return {
        type: 'GET_DECK',
        payload: axios.get(studyApiUrl + '/api/study/deck/' + id)
    }
}

export const getCardsInDeckAction = (id) => {
    return {
        type: 'GET_CARDS_IN_DECK',
        payload: axios.get(studyApiUrl + '/api/study/card?deckId=' + id)
    }
}
