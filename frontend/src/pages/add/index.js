let defaultState = {
    words: [],
    names: [],
    text: {},
    tagInput: ""
};

const addYomi = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_TEXT':
            return {
                ...state,
                text: {
                    ...state.text,
                    plain: action.text.plain,
                    formatted: action.text.formatted
                }
            };
        case 'RESET_TEXT':
            return {
                ...state,
                text: {}
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