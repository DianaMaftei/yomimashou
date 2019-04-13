let defaultState = {
    textSelectInfo: {},
};

const yomiText = (state = defaultState, action) => {
    switch (action.type) {
        case 'UPDATE_TEXT_SELECT_INFO':
            return {
                ...state,
                textSelectInfo: {
                    ...action.textSelectInfo
                }
            };
        default:
            return state
    }
};

export default yomiText;