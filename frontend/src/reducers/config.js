let defaultState = {
    popUp: {
        totalDictionaries: 3,
        currentDictionary: 2,
        limit: 5
    }
};

const getNextDictionary = (popUp) => {
    let nextDictionary = (popUp.currentDictionary + 1) % popUp.totalDictionaries;
    return nextDictionary > 0 ? nextDictionary : popUp.totalDictionaries;
};

const config = (state = defaultState, action) => {
    switch (action.type) {
        case 'SWITCH_DICTIONARY' :
            return {
                ...state,
                popUp: {
                    ...state.popUp,
                    currentDictionary: getNextDictionary(state.popUp)
                }
            };

        default:
            return state
    }
};

export default config;