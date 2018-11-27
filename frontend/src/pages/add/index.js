let defaultState = {
    words: [],
    names: [],
    text: {},
    tagInput: "",
    analyzer: null
};

const addYomi = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_TEXT':
            return {
                ...state,
                text: {
                    ...state.text,
                    content: action.text.content
                }
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
        case 'SET_HIGHLIGHTED_TEXT':
            return {
                ...state,
                text: {
                    ...state.text,
                    content: action.text
                }
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
        case 'SET_ANALYZER':
            return {
                ...state,
                analyzer: action.analyzer
            };
        case 'RESET_TEXT':
            return {
                ...state,
                text: {}
            };
         case 'RESET_DICTIONARIES':
            return {
                ...state,
                words: [],
                names: []
            };

        case 'SET_TEXT_TITLE':
            return {
                ...state,
                text: {
                    ...state.text,
                    title: action.title
                }
            };
        case 'SET_TEXT_TAGS':
            return {
                ...state,
                text: {
                    ...state.text,
                    tags: action.tags
                }
            };
        case 'SET_TAG_INPUT':
            return {
                ...state,
                tagInput: action.tag
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

        case 'PARSE_TEXT_NAMES_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        default:
            return state
    }
};

export default addYomi;