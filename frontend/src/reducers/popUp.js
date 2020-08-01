import SearchType from "../pages/read/YomiText/Rikai/SearchType";
import PopupType from "../pages/read/YomiText/Rikai/PopupType";

const defaultPagination = {
  first: false,
  last: false,
  number: 0,
  totalPages: 0,
  limit: 10
};

let defaultState = {
  searchResult: {},
  showResult: {},
  previousSearchResult: {},
  previousShowResult: {},
  wordExamples: {},
  popupInfo: {
    position: {},
    type: PopupType.WORD,
    visible: false,
    closed: false,
    disableOutsideClickHandler: false,
    ...defaultPagination
  }
};

const getResult = (searchType, action, state) => {
  switch (searchType) {
    case SearchType.KANJI:
      return action.payload.data;
    case SearchType.EXAMPLE:
      if (state.previousSearchResult.result !== state.searchResult.result) {
        return action.payload.data;
      } else {
        return state.previousShowResult.result.concat(action.payload.data);
      }
    default:
      if (state.previousSearchResult.result !== state.searchResult.result) {
        return action.payload.data.content;
      } else {
        return state.previousShowResult.result.concat(
            action.payload.data.content);
      }
  }
};

const popUp = (state = defaultState, action) => {
  switch (action.type) {
    case 'UPDATE_SEARCH_RESULT':
      return {
        ...state,
        previousSearchResult: state.searchResult,
        searchResult: action.result
      };
    case 'UPDATE_SHOW_RESULT':
      return {
        ...state,
        showResult: action.result
      };
    case 'FETCH_WORD_EXAMPLES_PENDING':
      return {
        ...state,
        wordExamples: {}
      };
    case 'FETCH_WORD_EXAMPLES_FULFILLED':
      return {
        ...state,
        wordExamples: action.payload.data
      };
    case 'FETCH_WORD_EXAMPLES_REJECTED':
      return {
        ...state,
        error: action.payload
      };
    case 'FETCH_DATA_PENDING':
      return {
        ...state,
        popupInfo: {
          ...state.popupInfo,
          ...defaultPagination
        },
        previousShowResult: state.showResult,
        showResult: {
          ...state.showResult,
          result: null,
          type: state.searchResult.type
        }
      };

    case 'FETCH_DATA_FULFILLED':
      return {
        ...state,
        popupInfo: {
          ...state.popupInfo,
          first: action.payload.data.first,
          last: action.payload.data.last,
          number: action.payload.data.number,
          totalPages: action.payload.data.totalPages,
          limit: action.payload.data.size
        },
        showResult: {
          ...state.showResult,
          result: getResult(state.searchResult.type, action, state),
          type: state.searchResult.type
        }
      };

    case 'FETCH_DATA_REJECTED':
      return {
        ...state,
        error: action.payload
      };
    case 'FETCH_WORD_LIST_PENDING':
      return {
        ...state,
        showResult: {
          ...state.showResult,
          result: null,
          type: state.searchResult.type
        }
      };

    case 'FETCH_WORD_LIST_FULFILLED':
      return {
        ...state,
        showResult: {
          ...state.showResult,
          result: action.payload.data,
          type: state.searchResult.type
        }
      };

    case 'FETCH_WORD_LIST_REJECTED':
      return {
        ...state,
        error: action.payload
      };
    case 'FETCH_TRANSLATION_PENDING':
      return {
        ...state,
        showResult: {
          ...state.showResult,
          result: null,
          type: state.searchResult.type
        }
      };

    case 'FETCH_TRANSLATION_FULFILLED':
      return {
        ...state,
        showResult: {
          ...state.showResult,
          translation: action.payload,
          type: state.searchResult.type
        }
      };

    case 'FETCH_TRANSLATION_REJECTED':
      return {
        ...state,
        error: action.payload
      };

    case 'SET_FURIGANA_SENTENCE':
      return {
        ...state,
        showResult: {
          ...state.showResult,
          furigana: action.sentence,
          type: state.searchResult.type
        }
      };

    case 'SET_POPUP_INFO':
      return {
        ...state,
        popupInfo: {
          ...state.popupInfo,
          ...action.popupInfo
        }
      };
    default:
      return state
  }
};

export default popUp;