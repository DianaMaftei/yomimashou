let defaultState = {
    text: {},
    textImage: null,
    tagInput: "",
    sourceTabValue: 0,
    subtitles: "",
    showLoader: false,
    showTextPlaceholder: true
};

const add = (state = defaultState, action) => {
    switch (action.type) {
        case 'SET_TEXT_CONTENT':
            return {
                ...state,
                text: {
                    ...state.text,
                    content: action.content
                }
            };
        case 'SET_TEXT_IMAGE':
            return {
                ...state,
                textImage: action.textImage
            };
        case 'SET_SUBTITLES':
            return {
                ...state,
                subtitles: action.subtitles
            };
        case 'TOGGLE_LOADER':
            return {
                ...state,
                showLoader: !state.showLoader
            };
        case 'TOGGLE_PLACEHOLDER':
            return {
                ...state,
                showTextPlaceholder: !state.showTextPlaceholder
            };

        case 'RESET_TEXT':
            return {
                ...state,
                text: {}
            };
        case 'SET_TEXT_TITLE':
            return {
                ...state,
                text: {
                    ...state.text,
                    title: action.title
                }
            };
        case 'SET_TEXT_TAGS':
            return {
                ...state,
                text: {
                    ...state.text,
                    tags: action.tags
                }
            };
        case 'SET_TAG_INPUT':
            return {
                ...state,
                tagInput: action.tag
            };

        case 'SET_SOURCE_TAB_VALUE':
            return {
                ...state,
                sourceTabValue: action.sourceTabValue
            };

        case 'SCAN_IMAGES_PENDING':
            return {
                ...state,
                text: {
                    ...state.text,
                    content: null
                }
            };

        case 'SCAN_IMAGES_FULFILLED':
            return {
                ...state,
                text: {
                    ...state.text,
                    content: action.payload.data
                }
            };

        case 'SCAN_IMAGES_REJECTED':
            return {
                ...state,
                error: action.payload
            };
        default:
            return state
    }
};

export default add;
