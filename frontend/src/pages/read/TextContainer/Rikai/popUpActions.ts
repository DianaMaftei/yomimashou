import axios from 'axios';
import { fetchData, fetchWordList } from '../../../../service/TextService';


export const setPopupInfoAction = (popupInfo) => {
    return {
        type: 'SET_POPUP_INFO',
        popupInfo
    };
};

export const fetchDataAction = (url: string, searchItem: string, page: number, limit: number) => {
    return {
        type: 'FETCH_DATA',
        payload: fetchData(url, searchItem, page, limit)
    };
};

export const fetchWordExamplesAction = (url: string, searchItem: string, page: number, limit: number) => {
    return {
        type: 'FETCH_WORD_EXAMPLES',
        payload: fetchData(url, searchItem, page, limit)
    };
};

export const updateSearchResultAction = (result) => {
    return {
        type: 'UPDATE_SEARCH_RESULT',
        result
    };
};

export const setKanjiLevelsAction = (kanjiLevels) => {
    return {
        type: 'SET_KANJI_LEVELS',
        kanjiLevels
    };
};

export const fetchWordListAction = (sentence: string) => {
    return {
        type: 'FETCH_WORD_LIST',
        payload: fetchWordList(sentence)
    };
};

export const fetchTranslationAction = (sentence: string) => {
    return {
        type: 'FETCH_TRANSLATION',
        payload: axios.get('https://api.mymemory.translated.net/get?q=' + sentence + '&langpair=ja|en')
    };
};

export const updateShowResultAction = (result) => {
    return {
        type: 'UPDATE_SHOW_RESULT',
        result
    };
};
