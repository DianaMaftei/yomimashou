let defaultState = {
    visible: false,
    searchResult: {},
    showResult: {},
    limitResults: true
};

const popUp = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_VISIBILITY':
            return {
                ...state,
                visible: action.visibility
            };
        case 'UPDATE_SEARCH_RESULT':
            return {
                ...state,
                searchResult: action.result
            };
        case 'UPDATE_SHOW_RESULT':
            return {
                ...state,
                showResult: action.result
            };
        case 'LIMIT_RESULTS':
            return {
                ...state,
                limitResults: action.limitResults
            };
        case 'FETCH_DATA_PENDING':
            return {
                ...state,
                showResult: {result: null, type: state.searchResult.type}
            };

        case 'FETCH_DATA_FULFILLED':
            return {
                ...state,
                showResult: {result: action.payload.data, type: state.searchResult.type}
            };

        case 'FETCH_DATA_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        default:
            return state
    }
};

export default popUp;

// search should go in the dictionary component, not in popUp, together with highlight
// separate search from display popUp?
// go backwards, forwards