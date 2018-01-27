let defaultState = {
    popUp: {
        delay: 0,
        limit: 5
    },
    text: {
        highlight: true,
        disablekeys: false
    }
};

const config = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_DELAY':
            return {
                ...state,
                nush: null //TODO
            };
        default:
            return state
    }
};

export default config;