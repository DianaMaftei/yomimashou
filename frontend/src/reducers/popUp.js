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
                showResult: {...state.showResult, result: null, type: state.searchResult.type}
            };

        case 'FETCH_DATA_FULFILLED':
            return {
                ...state,
                showResult: {...state.showResult, result: action.payload.data, type: state.searchResult.type}
            };

        case 'FETCH_DATA_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        case 'FETCH_WORD_LIST_PENDING':
            return {
                ...state,
                showResult: {...state.showResult, result: null, type: state.searchResult.type}
            };

        case 'FETCH_WORD_LIST_FULFILLED':
            return {
                ...state,
                showResult: {...state.showResult, result: action.payload.data, type: state.searchResult.type}
            };

        case 'FETCH_WORD_LIST_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        case 'FETCH_TRANSLATION_PENDING':
            return {
                ...state,
                showResult: {...state.showResult, result: null, type: state.searchResult.type}
            };

        case 'FETCH_TRANSLATION_FULFILLED':
            return {
                ...state,
                showResult: {...state.showResult, translation: action.payload, type: state.searchResult.type}
            };

        case 'FETCH_TRANSLATION_REJECTED':
            return {
                ...state,
                error: action.payload
            };

        case 'SET_FURIGANA_SENTENCE':
            return {
                ...state,
                showResult: {...state.showResult, furigana: action.sentence, type: state.searchResult.type}
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