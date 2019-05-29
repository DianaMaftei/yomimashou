export const isAuthenticated = () => {
    return !!localStorage.getItem('token');
};

export const withHeaders = () => {
    return { headers: {Authorization: localStorage.getItem('token')}}
};