let defaultState = {
    user: {},
    showError: false
};

const login = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_EMAIL':
            return {
                ...state,
                user: {
                    ...state.user,
                    email: action.email
                }
            };
        case 'SET_PASSWORD':
            return {
                ...state,
                user: {
                    ...state.user,
                    password: action.password
                }
            };
        case 'SHOW_ERROR':
            return {
                ...state,
                showError: action.show
            };
        default:
            return state;
    }
};

export default login;