let defaultState = {
    showSummary: false,
    reviewedCard: null,
    cardsDue: []
};

const study = (state = defaultState, action) => {
    switch (action.type) {
        case 'TOGGLE_SUMMARY':
            return {
                ...state,
                showSummary: !state.showSummary
            };

        case 'REVIEW_CARD_PENDING':
            return {
                ...state,
                reviewedCard: null
            };

        case 'REVIEW_CARD_FULFILLED':
            return {
                ...state,
                reviewedCard: action.payload.data
            };

        case 'REVIEW_CARD_REJECTED':
            return {
                ...state,
                error: action.payload
            };

        case 'GET_DUE_CARDS_PENDING':
            return {
                ...state,
                cardsDue: []
            };

        case 'GET_DUE_CARDS_FULFILLED':
            return {
                ...state,
                cardsDue: action.payload.data
            };

        case 'GET_DUE_CARDS_REJECTED':
            return {
                ...state,
                error: action.payload
            };

        default:
            return state
    }
};

export default study;