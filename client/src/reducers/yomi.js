let defaultState = {
    text: null
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
        default:
            return state
    }
};

export default yomi;