import { scanImages } from '../../service/OcrService';


export const scanImagesAction = (formData: FormData) => {
    return {
        type: 'SCAN_IMAGES',
        payload: scanImages(formData)
    }
}

export const toggleLoaderAction = () => {
    return {
        type: 'TOGGLE_LOADER'
    }
}

export const setSubtitlesAction = (subtitles: string) => {
    return {
        type: 'SET_SUBTITLES',
        subtitles
    }
}

export const setTextTagsAction = (tags: string[]) => {
    return {
        type: 'SET_TEXT_TAGS',
        tags
    }
}

export const setTagInputAction = (tag: string) => {
    return {
        type: 'SET_TAG_INPUT',
        tag
    }
}

export const togglePlaceholderAction = (showTextPlaceholder: boolean) => {
    return {
        type: 'TOGGLE_PLACEHOLDER',
        showTextPlaceholder
    }
}

export const setTextTitleAction = (title: string) => {
    return {
        type: 'SET_TEXT_TITLE',
        title
    }
}

export const setTextContentAction = (content: string) => {
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

export const setSourceTabValueAction = (sourceTabValue: number) => {
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


