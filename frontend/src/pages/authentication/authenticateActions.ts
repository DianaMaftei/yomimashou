export const setUsernameAction = (username) => {
    return {
        type: 'SET_USERNAME',
        username
    }
}

export const setEmailAction = (email) => {
    return {
        type: 'SET_EMAIL',
        email
    }
}

export const setPasswordAction = (password) => {
    return {
        type: 'SET_PASSWORD', 
        password
    }
}

export const showPasswordAction = () => {
    return {
        type: 'SHOW_PASSWORD'
    }
}

export const showErrorAction = (show) => {
    return {
        type: 'SHOW_ERROR', 
        show
    }
}
