let defaultState = {
    words: [],
    names: []
};

const addYomi = (state = defaultState, action) => {
    switch (action.type) {
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