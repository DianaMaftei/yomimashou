let defaultState = {
    visible: false,
    result: {},
    limitResults: true
};

const popUp = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_VISIBILITY':
            return {
                ...state,
                visible: action.visibility
            };
        case 'UPDATE_RESULT':
            return {
                ...state,
                result: action.result
            };
        case 'LIMIT_RESULTS':
            return {
                ...state,
                limitResults: action.limitResults
            };
        default:
            return state
    }
};

export default popUp;

// search should go in the dictionary component, not in popUp, together with highlight
// separate search from display popUp?
// go backwards, forwards