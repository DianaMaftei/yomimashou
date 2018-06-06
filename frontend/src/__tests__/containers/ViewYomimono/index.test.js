import deepFreeze from 'deepfreeze';
import viewYomi from "../../../../src/containers/ViewYomimono";

describe('ViewYomimonoReducers', function () {

    const initialState = deepFreeze({
        text: null});

    it('should return the default state when no valid action type is passed', () => {

        const action = deepFreeze({
            type: 'FOO_BAR'
        });

        const finalState = viewYomi(initialState, action);

        expect(finalState).toEqual(initialState);
    });

    it('should update the text', () => {
        let newText = deepFreeze("foo bar");
        const action = deepFreeze({
            type: 'SET_TEXT',
            text: newText
        });

        const finalState = viewYomi(initialState, action);

        expect(finalState.text).toEqual(newText);
    });

    it('should reset the text', () => {
        const action = deepFreeze({
            type: 'RESET_TEXT'
        });

        const finalState = viewYomi(initialState, action);

        expect(finalState.text).toEqual(null);
    });

});