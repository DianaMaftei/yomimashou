let defaultState = {
    searchResult: {},
    showResult: {},
    popupInfo: {
        position: {},
        visible: false
    }
};

const popUp = (state = defaultState, action) => {
    switch (action.type) {
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
        case 'FETCH_DATA_PENDING':
            return {
                ...state,
                showResult: { result: null, type: state.searchResult.type }
            };

        case 'FETCH_DATA_FULFILLED':
            return {
                ...state,
                showResult: { result: action.payload.data, type: state.searchResult.type }
            };

        case 'FETCH_DATA_REJECTED':
            return {
                ...state,
                error: action.payload
            };

        case 'SET_POPUP_INFO':
            return {
                ...state,
                popupInfo: action.popupInfo
            };
        default:
            return state
    }
};

export default popUp;