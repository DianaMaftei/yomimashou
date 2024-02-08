import { Text } from '../../model/Text';
import { analyzeText, getTextById, parseTextNames, parseTextWords } from '../../service/TextService';


export const getTextByIdAction = (id: number) => {
    return {
        type: 'GET_TEXT_BY_ID',
        payload: getTextById(id)
    };
};

export const resetDictionariesAction = () => {
    return {
        type: 'RESET_DICTIONARIES'
    };
};

export const parseTextWordsAction = (text: Text) => {
    return {
        type: 'PARSE_TEXT_WORDS',
        payload: parseTextWords(text.title + ' ' + text.content.replace(/<br>/g, ''))
    };
};

export const parseTextNamesAction = (text: Text) => {
    return {
        type: 'PARSE_TEXT_NAMES',
        payload: parseTextNames(text.title + ' ' + text.content.replace(/<br>/g, ''))
    };
};

export const toggleTextActionsMenuAction = () => {
    return {
        type: 'TOGGLE_TEXT_ACTIONS_MENU'
    };
};

export const updateTextSelectInfoAction = (textSelectInfo: object) => {
    return {
        type: 'UPDATE_TEXT_SELECT_INFO',
        textSelectInfo
    };
};

export const setFuriganaTextAction = (text: string) => {
    return {
        type: 'SET_FURIGANA_TEXT',
        text
    };
};

export const setFuriganaTitleAction = (title: string) => {
    return {
        type: 'SET_FURIGANA_TITLE',
        title
    };
};

export const setFuriganaSentenceAction = (sentence: string) => {
    return {
        type: 'SET_FURIGANA_SENTENCE',
        sentence
    };
};

export const switchDictionaryAction = () => {
    return {
        type: 'SWITCH_DICTIONARY'
    };
};

export const analyzeTextAction = (text: Text) => {
    return {
        type: 'ANALYZE_TEXT',
        payload: analyzeText(text)
    };
};
