import axios from "axios";
import {apiUrl} from "../../../../AppUrl";

export const setPopupInfoAction = (popupInfo) => {
    return {
        type: 'SET_POPUP_INFO',
        popupInfo
    }
}

export const fetchDataAction = (url, list, number, limit) => {
    return {
        type: 'FETCH_DATA',
        payload: axios.get(apiUrl + '/api/dictionary/' + url + '?searchItem=' + list + `&page=${number}&size=${limit}`)
    }
}

export const fetchWordExamplesAction = (url, list, page, limit) => {
    return {
        type: 'FETCH_WORD_EXAMPLES',
        payload: axios.get(
            apiUrl + '/api/dictionary/' + url + '?searchItem=' + list + `&page=${page}&size=${limit}`)
    }
}

export const updateSearchResultAction = (result) => {
    return {
        type: 'UPDATE_SEARCH_RESULT',
        result
    }
}

export const setKanjiLevelsAction = (kanjiLevels) => {
    return {
        type: 'SET_KANJI_LEVELS',
        kanjiLevels
    }
}

export const fetchWordListAction = (sentence) => {
    return {
        type: 'FETCH_WORD_LIST',
        payload: axios.post(apiUrl + '/api/sentence/', sentence)
    }
}

export const fetchTranslationAction = (sentence) => {
    return {
        type: 'FETCH_TRANSLATION',
        payload: axios.get('https://api.mymemory.translated.net/get?q=' + sentence + '&langpair=ja|en')
    }
}

export const updateShowResultAction = (result) => {
    return {
        type: 'UPDATE_SHOW_RESULT',
        result
    }
}