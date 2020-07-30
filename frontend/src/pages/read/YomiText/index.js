let defaultState = {
    textSelectInfo: {},
    showTextActions: false
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
        case 'TOGGLE_TEXT_ACTIONS_MENU':
            return {
                ...state,
                showTextActions: !state.showTextActions
            };
        default:
            return state
    }
};

export default yomiText;