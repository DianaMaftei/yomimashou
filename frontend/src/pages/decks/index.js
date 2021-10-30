let defaultState = {
  decks: null,
  deck: null,
  cardsInDeck: []
};

const decks = (state = defaultState, action) => {
  switch (action.type) {
    case 'GET_DECKS_PENDING':
      return {
        ...state,
        decks: null
      };

    case 'GET_DECKS_FULFILLED':
      return {
        ...state,
        decks: action.payload.data
      };

    case 'GET_DECKS_REJECTED':
      return {
        ...state,
        error: action.payload
      };

    case 'GET_DECK_PENDING':
      return {
        ...state,
        deck: null
      };

    case 'GET_DECK_FULFILLED':
      return {
        ...state,
        deck: action.payload.data
      };

    case 'GET_DECK_REJECTED':
      return {
        ...state,
        error: action.payload
      };

    case 'GET_CARDS_IN_DECK_PENDING':
      return {
        ...state,
        cardsInDeck: []
      };

    case 'GET_CARDS_IN_DECK_FULFILLED':
      return {
        ...state,
        cardsInDeck: action.payload.data
      };

    case 'GET_CARDS_IN_DECK_REJECTED':
      return {
        ...state,
        error: action.payload
      };
    default:
        return state
  }
};

export default decks;
