let defaultState = {
    text: {},
    words: [],
    names: [],
    analyzer: null,
    textSelectInfo: {},
    showTextActions: false
};

const yomiText = (state = defaultState, action) => {
    switch (action.type) {
        case 'UPDATE_TEXT_SELECT_INFO':
            return {
                ...state,
                textSelectInfo: {
                    ...action.textSelectInfo
                }
            };
        case 'TOGGLE_TEXT_ACTIONS_MENU':
            return {
                ...state,
                showTextActions: !state.showTextActions
            };
        case 'RESET_DICTIONARIES':
            return {
                ...state,
                words: [],
                names: []
            };
        case 'SET_ANALYZER':
            return {
                ...state,
                analyzer: action.analyzer
            };
        case 'PARSE_TEXT_WORDS_PENDING':
            return {
                ...state,
                words: null
            };

        case 'PARSE_TEXT_WORDS_FULFILLED':
            return {
                ...state,
                words: action.payload.data
            };

        case 'PARSE_TEXT_WORDS_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        case 'PARSE_TEXT_NAMES_PENDING':
            return {
                ...state,
                names: null
            };

        case 'PARSE_TEXT_NAMES_FULFILLED':
            return {
                ...state,
                names: action.payload.data
            };
        case 'SET_HIGHLIGHTED_TEXT':
            return {
                ...state,
                text: {
                    ...state.text,
                    content: action.text
                }
            };
        case 'PARSE_TEXT_NAMES_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        case 'GET_TEXT_BY_ID_PENDING':
            return {
                ...state,
                text: {}
            };
        case 'GET_TEXT_BY_ID_FULFILLED':
            return {
                ...state,
                text: action.payload.data
            };
        case 'GET_TEXT_BY_ID_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        case 'SET_FURIGANA_TEXT':
            return {
                ...state,
                text: {
                    ...state.text,
                    furigana: action.text
                }
            };
        case 'SET_FURIGANA_TITLE':
            return {
                ...state,
                text: {
                    ...state.text,
                    furiganaTitle: action.title
                }
            };
        default:
            return state
    }
};

export default yomiText;
