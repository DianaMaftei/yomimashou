let defaultState = {
    popUp: {
        totalDictionaries: 2,
        // totalDictionaries: 3,
        currentDictionary: 2
    },
    kanjiLevels: {
        N5: false,
        N4: false,
        N3: false,
        N2: false,
        N1: false
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
                    currentDictionary: state.popUp.currentDictionary === 2 ? 1 : 2
                }
            };
        case 'SET_KANJI_LEVEL' :
            return {
                ...state,
                kanjiLevels: action.kanjiLevels
            };

        default:
            return state
    }
};

export default config;