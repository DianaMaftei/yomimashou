import axios from "axios";
import {ocrApiUrl} from "../../AppUrl";

export const scanImagesAction = (formData, headers) => {
    return {
        type: 'SCAN_IMAGES',
        payload: axios.post(ocrApiUrl + '/api/ocr/full', formData, {headers})
    }
}

export const toggleLoaderAction = () => {
    return {
        type: 'TOGGLE_LOADER'
    }
}

export const setSubtitlesAction = (subtitles) => {
    return {
        type: 'SET_SUBTITLES',
        subtitles
    }
}

export const setTextTagsAction = (tags) => {
    return {
        type: 'SET_TEXT_TAGS',
        tags
    }
}

export const setTagInputAction = (tag) => {
    return {
        type: 'SET_TAG_INPUT',
        tag
    }
}

export const togglePlaceholderAction = (showTextPlaceholder) => {
    return {
        type: 'TOGGLE_PLACEHOLDER',
        showTextPlaceholder
    }
}

export const setTextTitleAction = (title) => {
    return {
        type: 'SET_TEXT_TITLE',
        title
    }
}

export const setTextContentAction = (content) => {
    return {
        type: 'SET_TEXT_CONTENT',
        content
    }
}

export const resetTextAction = () => {
    return {
        type: 'RESET_TEXT'
    }
}

export const setSourceTabValueAction = (sourceTabValue) => {
    return {
        type: 'SET_SOURCE_TAB_VALUE',
        sourceTabValue
    }
}

export const setTextImageAction = (image) => {
    return {
        type: 'SET_TEXT_IMAGE',
        textImage: image
    }
}


