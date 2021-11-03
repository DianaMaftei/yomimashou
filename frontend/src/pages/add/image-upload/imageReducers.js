let defaultState = {
    src: null,
    croppedImageUrl: null,
    showCrop: false,
    imageRef: null
};

const image = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_SRC':
            return {
                ...state,
                src: action.src
            };
        case 'SET_IMAGE_REF':
            return {
                ...state,
                imageRef: action.imageRef
            };
        case 'SET_CROPPED_IMAGE_URL':
            return {
                ...state,
                croppedImageUrl: action.croppedImageUrl
            };
        case 'TOGGLE_CROP':
            return {
                ...state,
                showCrop: !state.showCrop
            };
        case 'RESET_STATE':
            return defaultState;
        default:
            return state
    }
};

export default image;
