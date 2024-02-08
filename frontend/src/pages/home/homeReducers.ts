let defaultState = {
    texts: [],
    textsStatuses: {}
};

const home = (state = defaultState, action) => {
    switch (action.type) {
        case 'GET_TEXTS_PENDING':
            return {
                ...state,
                texts: []
            };

        case 'GET_TEXTS_FULFILLED':
            return {
                ...state,
                texts: action.payload.data
            };

        case 'GET_TEXTS_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        case 'GET_TEXTS_STATUSES_PENDING':
            return {
                ...state,
                textsStatuses: {}
            };

        case 'GET_TEXTS_STATUSES_FULFILLED':
            return {
                ...state,
                textsStatuses: action.payload.data
            };

        case 'GET_TEXTS_STATUSES_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        default:
            return state
    }
};

export default home;
