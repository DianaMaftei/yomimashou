import deepFreeze from 'deepfreeze';
import popUpReducers from './popUp';
import SearchType from './SearchType';


describe('popUp', function () {

    const initialState = deepFreeze({
        searchResult: {},
        showResult: {},
        popupInfo: {
            position: {},
            visible: false
        }
    });

    it('should return the default state when no valid action type is passed', () => {

        const action = deepFreeze({
            type: 'FOO_BAR'
        });

        const finalState = popUpReducers(initialState, action);

        expect(finalState).toEqual(initialState);
    });

    it('should update the search result', () => {

        let result = deepFreeze({
            type: SearchType.WORD,
            result: []
        });

        const action = deepFreeze({
            type: 'UPDATE_SEARCH_RESULT',
            result: result
        });

        const finalState = popUpReducers(initialState, action);

        expect(finalState.searchResult).toEqual(result);
    });


    it('should update the result to be shown', () => {

        let result = deepFreeze({
            type: SearchType.WORD,
            result: []
        });

        const action = deepFreeze({
            type: 'UPDATE_SHOW_RESULT',
            result: result
        });

        const finalState = popUpReducers(initialState, action);

        expect(finalState.showResult).toEqual(result);
    });

    it('should fetch pending', () => {

        let initialFetchState = deepFreeze({
            ...initialState,
            searchResult: { type: SearchType.WORD, result: {} }
        });

        let result = deepFreeze({
            type: SearchType.WORD,
            result: null
        });

        const action = deepFreeze({
            type: 'FETCH_DATA_PENDING',
            result: result
        });

        const finalState = popUpReducers(initialFetchState, action);

        expect(finalState.showResult).toEqual(result);
    });

    it('should fulfill data fetch', () => {

        let initialFetchState = deepFreeze({
            ...initialState,
            searchResult: { type: SearchType.WORD, result: null }
        });

        let result = deepFreeze({
            type: initialFetchState.searchResult.type,
            result: {
                kanji: "foo", kana: "bar"
            }
        });

        const action = deepFreeze({
            type: 'FETCH_DATA_FULFILLED',
            payload: {
                data: {
                    kanji: "foo", kana: "bar"
                }
            }
        });

        const finalState = popUpReducers(initialFetchState, action);

        expect(finalState.showResult).toEqual(result);
    });


    it('should return an error if the call was rejected', () => {

        let result = deepFreeze({
            error: "some error here"
        });

        const action = deepFreeze({
            type: 'FETCH_DATA_REJECTED',
            payload: {
                error: "some error here"
            }
        });

        const finalState = popUpReducers(initialState, action);

        expect(finalState.error).toEqual(result);
    });

    it('should switch popup visibility', () => {

        const action = deepFreeze({
            type: 'SET_POPUP_INFO',
            popupInfo: {
                position: {},
                visible: true
            }
        });

        const finalState = popUpReducers(initialState, action);

        expect(finalState.popupInfo.visible).toEqual(true);
    });

    it('should update popup position', () => {

        const action = deepFreeze({
            type: 'SET_POPUP_INFO',
            popupInfo: {
                position: { x: 20, y: 30 },
                visible: false
            }
        });

        const finalState = popUpReducers(initialState, action);

        expect(finalState.popupInfo.position).toEqual({ x: 20, y: 30 });
    });

});
