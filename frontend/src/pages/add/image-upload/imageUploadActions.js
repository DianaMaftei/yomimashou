export const setSrcAction = (src) => {
    return {
        type: 'SET_SRC', src
    }
}

export const setImageRefAction = (imageRef) => {
    return {
        type: 'SET_IMAGE_REF', imageRef
    }
}

export const setCroppedImageUrlAction = (croppedImageUrl) => {
    return {
        type: 'SET_CROPPED_IMAGE_URL', croppedImageUrl
    }
}

export const toggleCropAction = () => {
   return {
       type: 'TOGGLE_CROP'
   }
}