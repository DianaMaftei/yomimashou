let defaultState = {
    user: {},
    showError: false,
    showPassword: false
};

const authenticate = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_USERNAME':
            return {
                ...state,
                user: {
                    ...state.user,
                    username: action.username
                }
            };
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
        case 'SHOW_PASSWORD':
            return {
                ...state,
                showPassword: !state.showPassword
            };
        default:
            return state;
    }
};

export default authenticate;
