let defaultState = {
    text: null,
    highlightTerm: null
};

const yomi = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_TEXT':
            return {
                ...state,
                text: action.text
            };
        case 'RESET_TEXT':
            return {
                ...state,
                text: null
            };
        case 'UPDATE_HIGHLIGHT_TERM':
            return {
                ...state,
                highlightTerm: action.result
            };
        default:
            return state
    }
};

export default yomi;