// pop-up
export const popUpSetVisibility = visibility => {
    return {
        type: 'SET_VISIBILITY',
        visibility
    }
};

export const updateSearchResult = result => {
    return {
        type: 'UPDATE_SEARCH_RESULT',
        result
    }
};

export const updateHighlightTerm = result => {
    return {
        type: 'UPDATE_HIGHLIGHT_TERM',
        result
    }
};

export const updateShowResult = result => {
    return {
        type: 'UPDATE_SHOW_RESULT',
        result
    }
};

export const setResultsLimitation = limitResults => {
    return {
        type: 'LIMIT_RESULTS',
        limitResults
    }
};

// yomi
export const setText = text => {
    return {
        type: 'SET_TEXT',
        text
    }
};

export const resetText = () => {
    return {
        type: 'RESET_TEXT'}
};



