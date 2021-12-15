export const actionSanitizer = (action) => {
    switch (action.type) {
        case 'SET_TEXT_IMAGE':
            return action.textImage ? {...action, textImage: '<<IMAGE_BLOB>>'} : action;
        case 'SET_IMAGE_REF':
            return action.imageRef ? {...action, imageRef: '<<IMAGE_REF>>'} : action;
        case 'SET_SRC':
            return action.src ? {...action, src: '<<IMAGE_SRC>>'} : action;
        default:
            return action
    }
}

export const stateSanitizer = (state) => {
    return {
        ...state,
        add: state.add.textImage ? {...state.add, textImage: '<<IMAGE_BLOB>>'} : state.add,
        image: {
            ...state.image,
            imageRef: state.image.imageRef ? '<<IMAGE_REF>>' : state.image.imageRef,
            src: state.image.src ? '<<IMAGE_SRC>>' : state.image.src
        }
    }
}
