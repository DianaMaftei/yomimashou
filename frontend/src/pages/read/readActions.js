import axios from "axios";
import {apiUrl} from "../../AppUrl";

export const getTextByIdAction = (id) => {
    return {
        type: 'GET_TEXT_BY_ID',
        payload: axios.get(apiUrl + '/api/text/' + id)
    }
}

export const resetDictionariesAction = () => {
    return {
        type: 'RESET_DICTIONARIES'
    }
}

export const parseTextWordsAction = (text) => {
    return {
        type: 'PARSE_TEXT_WORDS',
        payload: axios.post(apiUrl + '/api/text/parse/words', text.title + " " + text.content.replace(/<br>/g, ""))
    }
}

export const parseTextNamesAction = (text) => {
    return {
        type: 'PARSE_TEXT_NAMES',
        payload: axios.post(apiUrl + '/api/text/parse/names', text.title + " " + text.content.replace(/<br>/g, ""))
    }
}

export const toggleTextActionsMenuAction = () => {
    return {
        type: 'TOGGLE_TEXT_ACTIONS_MENU'
    }
}

export const updateTextSelectInfoAction = (textSelectInfo) => {
    return {
        type: 'UPDATE_TEXT_SELECT_INFO',
        textSelectInfo
    }
}

export const setAnalyzerAction = (analyzer) => {
    return {
        type: 'SET_ANALYZER',
        analyzer
    }
}

export const setFuriganaTextAction = (text) => {
    return {
        type: 'SET_FURIGANA_TEXT',
        text
    }
}

export const setFuriganaTitleAction = (title) => {
    return {
        type: 'SET_FURIGANA_TITLE',
        title
    }
}

export const setFuriganaSentenceAction = (sentence) => {
    return {
        type: 'SET_FURIGANA_SENTENCE',
        sentence
    }
}

export const switchDictionaryAction = () => {
    return {
        type: 'SWITCH_DICTIONARY'
    }
}