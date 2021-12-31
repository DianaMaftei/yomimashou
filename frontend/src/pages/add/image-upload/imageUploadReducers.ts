let defaultState = {
    src: null,
    croppedImageUrl: null,
    showCrop: false,
    imageRef: null
};

const imageUpload = (state = defaultState, action: object) => {
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
        default:
            return state
    }
};

export default imageUpload;
